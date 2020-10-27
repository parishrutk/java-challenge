package jp.co.axa.apidemo.excpetions;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class CustomApiException extends RuntimeException {

    private static final long serialVersionUID = -531421314087148673L;

    private String errorMessage;
    private HttpStatus errorStatus;
    private String timeStamp;
    private String detailedErrorMessage;
    //used for field level validation details
    private ApiErrorDetails apiErrorDetails;

    public CustomApiException(String errorMessage, HttpStatus errorStatus) {
        this.errorMessage = errorMessage;
        this.errorStatus = errorStatus;
        this.timeStamp = getLocalTimeStamp();
    }

    public CustomApiException(String errorMessage, HttpStatus errorStatus, String detailedErrorMessage) {
        this(errorMessage,errorStatus);
        this.detailedErrorMessage = detailedErrorMessage;
    }

    public CustomApiException(String errorMessage, HttpStatus errorStatus, ApiErrorDetails apiErrorDetails) {
        this(errorMessage,errorStatus);
        this.apiErrorDetails = apiErrorDetails;
    }

    public CustomApiException(String errorMessage, HttpStatus errorStatus, Throwable exception) {
        this(errorMessage,errorStatus);
        this.detailedErrorMessage = exception.getLocalizedMessage();
    }

    private String getLocalTimeStamp() {
        return DateTimeFormatter.ISO_LOCAL_DATE_TIME.format(LocalDateTime.now());
    }


}
