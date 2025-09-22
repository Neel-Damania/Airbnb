package com.neel.projects.airBnbApp.advice;

import java.time.LocalDateTime;

import lombok.Data;

@Data
public class ApiResponse<T> {
    private LocalDateTime timestamp;
    private T data;
    private ApiError error;

    public ApiResponse(){
        this.timestamp = LocalDateTime.now();
    }

    public ApiResponse(T data){
        this();
        this.data = data;
    }
    public ApiResponse(ApiError error){
        this();
        this.error = error;
    }
}
