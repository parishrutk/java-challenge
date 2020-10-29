package jp.co.axa.apidemo.utils;

import jp.co.axa.apidemo.excpetions.ApiRuntimeException;
import org.springframework.http.HttpStatus;

public class ExceptionUtil {

    private ExceptionUtil() {

    }

    public static ApiRuntimeException prepareExceptionDetails(String errorMessage, HttpStatus httpStatus, Throwable throwable) {
        return new ApiRuntimeException(errorMessage, httpStatus, throwable);
    }

    public static ApiRuntimeException prepareExceptionDetails(String errorMessage, HttpStatus httpStatus, String detailedErrorMessage) {
        return new ApiRuntimeException(errorMessage, httpStatus, detailedErrorMessage);
    }

    public static ApiRuntimeException prepareExceptionDetails(String errorMessage, HttpStatus httpStatus) {
        return new ApiRuntimeException(errorMessage, httpStatus);
    }
}
