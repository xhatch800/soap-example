package com.example.soap.client;

import jakarta.xml.ws.WebServiceException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class SoapErrorHandler {

    private static final Logger log = LoggerFactory.getLogger(SoapErrorHandler.class);

    @ExceptionHandler(WebServiceException.class)
    public ResponseEntity<ErrorResponse> onSoapFailure(WebServiceException e) {
        log.warn("SOAP call failed: {}", e.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_GATEWAY)
                .body(new ErrorResponse("SOAP service call failed", e.getMessage()));
    }

    public record ErrorResponse(String error, String detail) {
    }
}
