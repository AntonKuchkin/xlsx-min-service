package com.example.xlsx_min_service.exception;

import static com.example.xlsx_min_service.exception.ExceptionConst.INVALID_FORMAT;

public class InvalidCellFormatException extends RuntimeException {

    public InvalidCellFormatException(int rowNumber) {
        super(String.format(INVALID_FORMAT, rowNumber));
    }

    public InvalidCellFormatException(String customMessage) {
        super(customMessage);
    }
}
