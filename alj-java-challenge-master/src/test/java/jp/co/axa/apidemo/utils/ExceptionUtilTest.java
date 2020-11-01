package jp.co.axa.apidemo.utils;

import jp.co.axa.apidemo.excpetions.ApiRuntimeException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@RunWith(PowerMockRunner.class)
@PrepareForTest(ExceptionUtil.class)
public class ExceptionUtilTest {

    private static final String DELETE_ERROR_MESSAGE = "Delete operation failed!";
    private static final String GET_ERROR_MESSAGE = "Get operation failed!";

    @Test
    public void testPrepareExceptionDetailsWith_errorMessage_httpStatus_throwable() {
        ApiRuntimeException apiRuntimeException = ExceptionUtil.prepareExceptionDetails(DELETE_ERROR_MESSAGE, HttpStatus.INTERNAL_SERVER_ERROR, new RuntimeException("Delete operation failed!"));
        assertThat(apiRuntimeException).isNotNull();
        assertThat(apiRuntimeException.getErrorMessage()).isEqualTo(DELETE_ERROR_MESSAGE);
    }

    @Test
    public void testPrepareExceptionDetailsWith_errorMessage_httpStatus_detailedErrorMessage() {
        ApiRuntimeException apiRuntimeException = ExceptionUtil.prepareExceptionDetails(GET_ERROR_MESSAGE, HttpStatus.INTERNAL_SERVER_ERROR, "Something went wrong!");
        assertThat(apiRuntimeException).isNotNull();
        assertThat(apiRuntimeException.getErrorStatus()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR);
        assertThat(apiRuntimeException.getErrorMessage()).isEqualTo(GET_ERROR_MESSAGE);
        assertThat(apiRuntimeException.getDetailedErrorMessage()).isEqualTo("Something went wrong!");
    }

    @Test
    public void testPrepareExceptionDetailsWith_errorMessage_httpStatus() {
        Long employeeId = 123L;
        ApiRuntimeException apiRuntimeException = ExceptionUtil.prepareExceptionDetails("Record already exists in the System for given Id [" + employeeId + "]", HttpStatus.BAD_REQUEST);
        assertThat(apiRuntimeException).isNotNull();
        assertThat(apiRuntimeException.getErrorStatus()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(apiRuntimeException.getErrorMessage()).isEqualTo("Record already exists in the System for given Id [" + employeeId + "]");
    }
}
