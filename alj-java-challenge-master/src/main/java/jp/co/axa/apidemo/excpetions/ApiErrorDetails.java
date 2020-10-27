package jp.co.axa.apidemo.excpetions;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ApiErrorDetails {

    private String fieldName;
    private String fieldValue;
    private String validationMsg;

}
