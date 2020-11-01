package jp.co.axa.apidemo.excpetions.handlers;

import jp.co.axa.apidemo.excpetions.ApiErrorResponse;
import jp.co.axa.apidemo.excpetions.ApiRuntimeException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.persistence.EntityNotFoundException;

@ControllerAdvice
public class CustomRestExceptionHandler extends ResponseEntityExceptionHandler {

    final Logger logger = LoggerFactory.getLogger(CustomRestExceptionHandler.class);

    //define all the different types of exception your employee service can throw. Then accordingly add the custom handling code.

    @Override
    protected ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException ex, HttpHeaders headers, HttpStatus status, WebRequest webRequest) {
        logger.debug("Triggering exception handler for HttpMessageNotReadableException type.");
        String error = "Malformed JSON request. Please check your request body.";
        return buildResponseEntity(new ApiRuntimeException(error, HttpStatus.BAD_REQUEST, ex.getLocalizedMessage()), webRequest);
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatus status, WebRequest webRequest) {
        logger.debug("Triggering exception handler for MethodArgumentNotValidException type.");
        String error = "Validation failed for given Payload. Please check your request input.";
        return buildResponseEntity(new ApiRuntimeException(error, HttpStatus.BAD_REQUEST, ex.getBindingResult().toString()), webRequest);
    }

    @ExceptionHandler(value = {EntityNotFoundException.class})
    protected ResponseEntity<Object> handleEntityNotFoundException(EntityNotFoundException exception, WebRequest webRequest) {
        logger.debug("Triggering exception handler for EntityNotFoundException type.");
        return buildResponseEntity(new ApiRuntimeException(exception.getMessage(), HttpStatus.NOT_FOUND), webRequest);
    }

    @ExceptionHandler(value = {ApiRuntimeException.class})
    protected ResponseEntity<Object> handleApiRuntimeException(ApiRuntimeException exception, WebRequest webRequest) {
        logger.debug("Triggering exception handler for ApiRuntimeException type.");
        return buildResponseEntity(exception, webRequest);
    }

    private ResponseEntity<Object> buildResponseEntity(ApiRuntimeException apiRuntimeException, WebRequest webRequest) {
        ApiErrorResponse response = new ApiErrorResponse(apiRuntimeException.getErrorMessage(), apiRuntimeException.getErrorStatus().value(),
                apiRuntimeException.getTimeStamp(), apiRuntimeException.getDetailedErrorMessage(),
                ((ServletWebRequest) webRequest).getRequest().getRequestURI());
        return new ResponseEntity<>(response, apiRuntimeException.getErrorStatus());
    }
}
