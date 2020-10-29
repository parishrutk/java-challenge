package jp.co.axa.apidemo.services;

import jp.co.axa.apidemo.entities.Employee;

import java.util.List;

public interface EmployeeService {

    List<Employee> listAllEmployees();

    Employee getEmployee(Long employeeId);

    void saveEmployee(Employee employee);

    void deleteEmployee(Long employeeId);

    void updateEmployee(Long employeeId, Employee employee);
}