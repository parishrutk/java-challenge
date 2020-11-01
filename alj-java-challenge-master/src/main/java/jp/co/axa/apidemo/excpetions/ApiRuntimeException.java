package jp.co.axa.apidemo.excpetions;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
@ToString
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ApiRuntimeException extends RuntimeException {

    private static final long serialVersionUID = -531421314087148673L;

    private String errorMessage;
    private HttpStatus errorStatus;
    private String timeStamp;
    private String detailedErrorMessage;
    //used for field level validation details
    private ApiErrorResponse apiErrorResponse;

    public ApiRuntimeException(String errorMessage, HttpStatus errorStatus) {
        this.errorMessage = errorMessage;
        this.errorStatus = errorStatus;
        this.timeStamp = getLocalTimeStamp();
    }

    public ApiRuntimeException(String errorMessage, HttpStatus errorStatus, String detailedErrorMessage) {
        this(errorMessage,errorStatus);
        this.detailedErrorMessage = detailedErrorMessage;
    }

    public ApiRuntimeException(String errorMessage, HttpStatus errorStatus, ApiErrorResponse apiErrorResponse) {
        this(errorMessage,errorStatus);
        this.apiErrorResponse = apiErrorResponse;
    }

    public ApiRuntimeException(String errorMessage, HttpStatus errorStatus, Throwable exception) {
        this(errorMessage,errorStatus);
        this.detailedErrorMessage = exception.getLocalizedMessage();
    }

    private String getLocalTimeStamp() {
        return DateTimeFormatter.ISO_LOCAL_DATE_TIME.format(LocalDateTime.now());
    }


}
