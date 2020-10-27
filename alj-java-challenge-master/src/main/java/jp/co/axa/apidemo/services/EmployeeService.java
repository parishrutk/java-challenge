package jp.co.axa.apidemo.services;

import jp.co.axa.apidemo.entities.Employee;
import org.springframework.stereotype.Service;

import java.util.List;

public interface EmployeeService {

    List<Employee> retrieveEmployees();

    Employee getEmployee(Long employeeId);

    void saveEmployee(Employee employee);

    void deleteEmployee(Long employeeId);

    void updateEmployee(Employee employee);
}