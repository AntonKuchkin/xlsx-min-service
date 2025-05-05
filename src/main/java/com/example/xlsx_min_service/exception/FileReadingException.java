package com.example.xlsx_min_service.exception;

public class FileReadingException extends RuntimeException {

    public FileReadingException(String message, Throwable cause) {
        super(message, cause);
    }
}
