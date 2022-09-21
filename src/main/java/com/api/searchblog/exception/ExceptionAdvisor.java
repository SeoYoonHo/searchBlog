package com.api.searchblog.exception;

import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;

@ControllerAdvice
@RestController
public class ExceptionAdvisor {
    String code = "";
    String description = "";
    String detail = "";

    @ExceptionHandler({BindException.class})
    public ErrorResponse errorValid(BindException exception) {
        BindingResult bindingResult = exception.getBindingResult();

        StringBuilder stringBuilder = new StringBuilder();

        for (FieldError fieldError : bindingResult.getFieldErrors()) {
            stringBuilder.append(fieldError.getField()).append(" : ");
            stringBuilder.append(fieldError.getDefaultMessage());
            stringBuilder.append(", ");
        }

        detail = stringBuilder.toString();

        String bindResultCode = bindingResult.getFieldError().getCode();

        switch (bindResultCode) {
            case "NotEmpty" -> {
                code = ErrorCode.NOT_EMPTY.getCode();
                description = ErrorCode.NOT_EMPTY.getDescription();
            }
            case "NotNull" -> {
                code = ErrorCode.NOT_NULL.getCode();
                description = ErrorCode.NOT_NULL.getDescription();
            }
            case "Min" -> {
                code = ErrorCode.MIN_VALUE.getCode();
                description = ErrorCode.MIN_VALUE.getDescription();
            }
        }

        return new ErrorResponse(code, detail, description);
    }

    @ExceptionHandler({MethodArgumentNotValidException.class})
    public ErrorResponse errorArgumentValid(MethodArgumentNotValidException exception) {
        BindingResult bindingResult = exception.getBindingResult();

        StringBuilder stringBuilder = new StringBuilder();

        for (FieldError fieldError : bindingResult.getFieldErrors()) {
            stringBuilder.append(fieldError.getField()).append(" : ");
            stringBuilder.append(fieldError.getDefaultMessage());
            stringBuilder.append(", ");
        }

        detail = stringBuilder.toString();

        String bindResultCode = bindingResult.getFieldError().getCode();

        switch (bindResultCode) {
            case "NotEmpty" -> {
                code = ErrorCode.NOT_EMPTY.getCode();
                description = ErrorCode.NOT_EMPTY.getDescription();
            }
            case "NotNull" -> {
                code = ErrorCode.NOT_NULL.getCode();
                description = ErrorCode.NOT_NULL.getDescription();
            }
            case "Min" -> {
                code = ErrorCode.MIN_VALUE.getCode();
                description = ErrorCode.MIN_VALUE.getDescription();
            }
        }

        return new ErrorResponse(code, detail, description);
    }
}