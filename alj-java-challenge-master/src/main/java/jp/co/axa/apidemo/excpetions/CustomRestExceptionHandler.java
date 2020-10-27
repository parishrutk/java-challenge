package jp.co.axa.apidemo.excpetions;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.persistence.EntityNotFoundException;

public class CustomRestExceptionHandler extends ResponseEntityExceptionHandler {

    //define all the different types of exception your employee service can throw. Then accordingly add the custom handling code.

    //handleEmployeeNotFoundException
    //handleDuplicateRecordException
    //handleInvalidInputException
    //handleValidationErrorsForEmployee.

    @Override
    protected ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        String error = "Malformed JSON request";
        return buildResponseEntity(new CustomApiException(error, status, ex));
    }

    private ResponseEntity<Object> buildResponseEntity(CustomApiException customApiException) {
        return new ResponseEntity<>(customApiException, customApiException.getErrorStatus());
    }

    @ExceptionHandler(EntityNotFoundException.class)
    protected ResponseEntity<Object> handleEntityNotFoundException(EntityNotFoundException exception) {
        return buildResponseEntity(new CustomApiException(exception.getMessage(), HttpStatus.NOT_FOUND, exception));
    }

    @ExceptionHandler(CustomApiException.class)
    protected ResponseEntity<Object> handleEntityNotFoundException(CustomApiException exception) {
        return buildResponseEntity(exception);
    }
}
