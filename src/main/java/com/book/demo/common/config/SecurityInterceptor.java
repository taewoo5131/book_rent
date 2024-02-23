package com.book.demo.common.config;

import com.book.demo.account.service.AccountService;
import com.book.demo.common.token.JwtTokenProvider;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.web.servlet.HandlerInterceptor;

import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@RequiredArgsConstructor
public class SecurityInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String jwt = request.getHeader(HttpHeaders.AUTHORIZATION);
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        if (jwt == null) {
            this.sendResponse(response, HttpStatus.BAD_REQUEST, "토큰 없음.");
            return false;
        }
        JwtTokenProvider jwtTokenProvider = new JwtTokenProvider();

        boolean isValid = false;
        try {
            isValid = jwtTokenProvider.validToken(jwt);
        } catch (ExpiredJwtException e) {
            log.error("토큰 만료");
        }
        if (!isValid) {
            this.sendResponse(response, HttpStatus.UNAUTHORIZED, "유효하지 않은 토큰.");
            return false;
        }

        return true;
    }

    private void sendResponse(HttpServletResponse response,HttpStatus httpStatus, String msg) throws Exception{
        ObjectMapper objectMapper = new ObjectMapper();
        PrintWriter out = response.getWriter();
        response.setStatus(httpStatus.value());
        Map<String, Object> map = new HashMap<>();
        map.put("status", httpStatus);
        map.put("result", msg);
        out.print(objectMapper.writeValueAsString(map));
        out.flush();
    }
}
