package com.kratos.entity;

import lombok.Data;
import org.springframework.http.HttpStatus;


@SuppressWarnings("unused")
@Data
public class CommonResponse<T> {

    private boolean success = false;

    private String message;

    private int code = -1;

    private T entity;

    public static <T> CommonResponse<T> ERR(int code, String message) {
        CommonResponse<T> response = new CommonResponse<>();
        response.code = code;
        response.message = message;
        return response;
    }

    public static <T> CommonResponse<T> STATUS(HttpStatus status) {
        CommonResponse<T> response = new CommonResponse<>();
        response.code = status.value();
        response.message = status.getReasonPhrase();
        return response;
    }

    public static <T> CommonResponse<T> OK() {
        CommonResponse<T> response = new CommonResponse<>();
        response.code = HttpStatus.OK.value();
        response.success = true;
        return response;
    }

    public static <T> CommonResponse<T> OK(T entity) {
        CommonResponse<T> response = new CommonResponse<>();
        response.code = HttpStatus.OK.value();
        response.success = true;
        response.entity = entity;
        return response;
    }

    public static <T> CommonResponse<T> OK(int code, T entity) {
        CommonResponse<T> response = new CommonResponse<>();
        response.code = code;
        response.success = true;
        response.entity = entity;
        return response;
    }

    public static <T> CommonResponse<T> OK(String message) {
        CommonResponse<T> response = new CommonResponse<>();
        response.code = HttpStatus.OK.value();
        response.success = true;
        response.message = message;
        return response;
    }

    public static <T> CommonResponse<T> OK(int code) {
        CommonResponse<T> response = new CommonResponse<>();
        response.code = code;
        response.success = true;
        return response;
    }

}
