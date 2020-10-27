package jp.co.axa.apidemo.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.Digits;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Entity
@Table(name="EMPLOYEE")
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Employee {

    @Id
    //TODO: remove generatedType and add custom implementation of Id.
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    @NotNull
    @NotBlank(message = "Employee id can not be empty")
    private Long id;

    @NotNull(message = "Employee name is mandatory")
    @NotBlank(message = "Employee name can not be empty")
    @Size(min=3, message="Name should have at least 3 characters")
    @Column(name="EMPLOYEE_NAME")
    private String name;

    @NotNull(message = "Salary value is mandatory")
    @Digits(fraction = 0, integer = 10,message = "Invalid Salary amount provided")
    @Column(name="EMPLOYEE_SALARY")
    private Integer salary;

    @NotNull(message = "Department value is Mandatory")
    @NotBlank(message = "Employee name can not be empty")
    @Size(min=2, message="Department should have at least 2 characters")
    @Column(name="DEPARTMENT")
    private String department;

}
