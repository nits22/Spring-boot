package com.example.sample.library;

import com.example.sample.dto.Response;

public class Utility {

    public static <T> Response<T> failureResponse(String message) {
        return Response.<T>builder()
                .message(message)
                .status(false)
                .build();
    }

    public static <T, E> Response<T> successResponse(String message) {
        return successResponse(null, message);
    }

    public static <T, E> Response<T> successResponse(T data) {
        return successResponse(data, null);
    }

    public static <T, E> Response<T> successResponse(T data, String message) {
        return Response.<T>builder()
                .status(true)
                .data(data)
                .message(message)
                .build();
    }
}
