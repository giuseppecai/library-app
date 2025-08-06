package net.giuse.biblioteca.core;

import java.time.LocalDateTime;

public class ResponseWrapper<T> {
    private boolean success;
    private String message;
    private T data;
    private LocalDateTime timestamp;

    public ResponseWrapper() {
    }

    public ResponseWrapper(boolean success, String message, T data) {
        this.success = success;
        this.message = message;
        this.data = data;
        this.timestamp = LocalDateTime.now();
    }

    public static <T> ResponseWrapper<T> success(T data) {
        return new ResponseWrapper<>(true, "Success operation.", data);
    }

    public static <T> ResponseWrapper<T> success(String message, T data) {
        return new ResponseWrapper<>(true, message, data);
    }

    public static <T> ResponseWrapper<T> failure(String message) {
        return new ResponseWrapper<>(false, message, null);
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }
}
