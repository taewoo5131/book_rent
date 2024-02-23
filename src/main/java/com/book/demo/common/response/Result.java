package com.book.demo.common.response;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

@Getter
@Setter
public class Result {
    private HttpStatus status;
    private Object result;
    @JsonIgnore
    private String errorCode;

    public Result() {
        this.status = HttpStatus.BAD_REQUEST;
        this.result = null;
    }

    public Result(HttpStatus status, String msg) {
        this.status = status;
        this.result = msg;
    }

    // 정상
    public static ResponseEntity<Result> makeResult(HttpStatus status, Object object) {
        HttpHeaders headers= new HttpHeaders();
        headers.setContentType(new MediaType("application", "json", StandardCharsets.UTF_8));

        Result message = new Result();
        message.setStatus(status);
        message.setResult(object);

        return new ResponseEntity<>(message, headers, status);
    }

    public static ResponseEntity<Result> makeError(Error error) {
        HttpHeaders headers= new HttpHeaders();
        headers.setContentType(new MediaType("application", "json", StandardCharsets.UTF_8));

        Result message = new Result();
        message.setStatus(error.getHttpStatus());
        message.setResult(error.getMsg());
        message.setErrorCode(error.getErrorCode());

        return new ResponseEntity<>(message, headers, error.getHttpStatus());
    }

    public static Map<String, ? extends Object> makeResultMap(String key, Object value) {
        Map map = new HashMap<>();
        map.put(key, value);
        return map;
    }
}
