package jp.co.axa.apidemo.utils;

import jp.co.axa.apidemo.excpetions.ApiRuntimeException;
import org.springframework.http.HttpStatus;

public class ExceptionUtil {

    /**
     * Util class to create instances of ApiRuntimeException depending upon the parameters provided during construction of it.
     */
    private ExceptionUtil() {
        // don't allow to create any object.
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
