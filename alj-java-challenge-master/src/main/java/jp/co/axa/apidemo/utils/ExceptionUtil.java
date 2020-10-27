package jp.co.axa.apidemo.utils;

import jp.co.axa.apidemo.excpetions.CustomApiException;
import org.springframework.http.HttpStatus;

public class ExceptionUtil {

    private ExceptionUtil() {

    }

    public static CustomApiException prepareExceptionDetails(String errorMessage, HttpStatus httpStatus, Throwable t) {
        return new CustomApiException(errorMessage, httpStatus, t);
    }
}
