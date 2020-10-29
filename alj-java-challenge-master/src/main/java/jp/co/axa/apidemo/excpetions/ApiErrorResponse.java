package jp.co.axa.apidemo.excpetions;


import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ApiErrorResponse {

    private String errorMessage;
    private int errorStatusCode;
    private String timeStamp;
    private String detailedErrorMessage;
    private String URI;


    public ApiErrorResponse(String localizedMessage, int errorStatusCode, String URI) {
        this.detailedErrorMessage = localizedMessage;
        this.errorStatusCode = errorStatusCode;
        this.URI = URI;
    }
}
