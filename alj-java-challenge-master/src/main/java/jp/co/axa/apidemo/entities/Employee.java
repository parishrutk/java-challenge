package jp.co.axa.apidemo.entities;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.Digits;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;

@Entity
@Table(name = "EMPLOYEE")
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
@ApiModel(value = "Employee")
public class Employee implements Serializable {

    private static final long serialVersionUID = 3133486035379940317L;

    @Id
    @NotNull
    @ApiModelProperty(notes = "A unique ID which identifies an employee.")
    private Long id;

    @NotNull(message = "Employee name is mandatory")
    @Size(min = 3, message = "Name should have at least 3 characters")
    @Column(name = "EMPLOYEE_NAME")
    @ApiModelProperty(notes = "Name that could also be used to identify an employee and it should be of at least 3 characters.")
    private String name;

    @NotNull(message = "Salary value is mandatory")
    @Digits(fraction = 0, integer = 8, message = "Invalid Salary amount provided")
    @Column(name = "EMPLOYEE_SALARY")
    @ApiModelProperty(notes = "Salary of that employee. The maximum of 10 digits salary an employee can get.")
    private Integer salary;

    @NotNull(message = "Department value is Mandatory")
    @Size(min = 2, message = "Department should have at least 2 characters")
    @Column(name = "DEPARTMENT")
    @ApiModelProperty(notes = "Department in which the employee works and it should at least have 2 characters in it.")
    private String department;

}
