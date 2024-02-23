package com.book.demo.common.token;

import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.Value;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.stream.Collectors;

@Slf4j
@Component
public class JwtTokenProvider {

    private final Key key;

    private String secretKey = "5bbcad98a858362ddfaa6111952e878baf1b1c11f74957faa70cd7ef1fce177e";

    // accessToken 30분
    private final long accessTokenValidTime = 30 * 60 * 1000L;

    // resfreshToken 2주
    LocalDate today = LocalDate.now();
    LocalDate next2Week = today.plus(2, ChronoUnit.WEEKS);
    Instant instant = next2Week.atStartOfDay(ZoneId.systemDefault()).toInstant();
    private final Date refreshTokenVaildTime = Date.from(instant);

    public JwtTokenProvider() {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        this.key = Keys.hmacShaKeyFor(keyBytes);
    }

    // JWT Access 토큰 생성
    public String makeAccessJwtToken(Long accountId) {
        Date now = new Date();
        return Jwts.builder()
                .setHeaderParam(Header.TYPE, Header.JWT_TYPE)
                .setIssuer("fresh")
                .setIssuedAt(now)
                .setExpiration(new Date(now.getTime() + accessTokenValidTime)) // 30분
//                .setExpiration(new Date(now.getTime() + 10)) // 30분
                .claim("id", accountId)
                .signWith(SignatureAlgorithm.HS256, this.secretKey)
                .compact();
    }

    // JWT Refresh 토큰 생성
    public String makeRefreshJwtToken() {
        Date now = new Date();
        return Jwts.builder()
                .setIssuedAt(now)
                .setExpiration(refreshTokenVaildTime) // 2주
                .signWith(SignatureAlgorithm.HS256, this.secretKey)
                .compact();
    }

    // 토큰 만료시간 확인
    public boolean validToken(String token) {
        try {
            Jws<Claims> claims = Jwts.parser().setSigningKey(this.secretKey).parseClaimsJws(token);
            return !claims.getBody().getExpiration().before(new Date());
        } catch (ExpiredJwtException e) {
            throw e;
        }
    }
}
