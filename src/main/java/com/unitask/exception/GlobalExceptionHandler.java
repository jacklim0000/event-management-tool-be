package com.unitask.exception;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.MessageSource;
import org.springframework.context.NoSuchMessageException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import org.springframework.web.util.WebUtils;

import java.util.HashMap;

@Slf4j
@RequiredArgsConstructor
@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {
    private final MessageSource messageSource;

    private String getI18nMessage(String code, String fallbackCode, WebRequest request, Object... args) {
        try {
            return messageSource.getMessage(code, args, request.getLocale());
        } catch (NoSuchMessageException nsme) {
            return (fallbackCode != null)
                    ? messageSource.getMessage(fallbackCode, new String[]{code}, request.getLocale())
                    : code;
        }
    }

    /* ServiceApp RELATED Exception ========================= */
    @ExceptionHandler(value = {ServiceAppException.class})
    protected ResponseEntity<Object> handleServiceAppException(ServiceAppException ex, WebRequest request) {
        final HttpStatus httpStatus = HttpStatus.valueOf(ex.getServiceAppStatusCode());

        if (HttpStatus.INTERNAL_SERVER_ERROR == httpStatus) {
            request.setAttribute(WebUtils.ERROR_EXCEPTION_ATTRIBUTE, ex, RequestAttributes.SCOPE_REQUEST);
        }

        final String i18nMessage = getI18nMessage(ex.getMessage(), "ServiceAppException.noMessage",
                request, ex.getMessageArgs());
        HashMap<String, String> errorMessage = new HashMap<>();
        errorMessage.put("ErrorCode", ex.getMessage());
        errorMessage.put("ErrorMessage", i18nMessage);
        return ResponseEntity.status(httpStatus).body(new ResponseEntity(errorMessage, httpStatus));
    }

    @ExceptionHandler(value = {AuthenticationException.class})
    protected ResponseEntity<Object> handleAuthenticationException(AuthenticationException ex, WebRequest request) {
        final HttpStatus httpStatus = HttpStatus.UNAUTHORIZED;
        HashMap<String, String> errorMessage = new HashMap<>();
        errorMessage.put("ErrorCode", HttpStatus.UNAUTHORIZED.getReasonPhrase());
        errorMessage.put("ErrorMessage", ex.getMessage());
        return ResponseEntity.status(httpStatus).body(new ResponseEntity(errorMessage, httpStatus));
    }
}
