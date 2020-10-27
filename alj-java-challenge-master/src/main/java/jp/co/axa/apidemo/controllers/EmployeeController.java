package jp.co.axa.apidemo.controllers;

import jp.co.axa.apidemo.entities.Employee;
import jp.co.axa.apidemo.services.EmployeeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1")
public class EmployeeController {

    @Autowired
    private EmployeeService employeeService;

    Logger logger = LoggerFactory.getLogger(EmployeeController.class);

    @GetMapping("/employees")
    public List<Employee> getEmployees() {
        if (logger.isDebugEnabled()) {
            logger.debug("Getting all employee details.");
        }
        List<Employee> employees = employeeService.retrieveEmployees();
        return employees;
    }

    @GetMapping("/employees/{employeeId}")
    public Employee getEmployee(@PathVariable(name = "employeeId") Long employeeId) {
        if (logger.isDebugEnabled()) {
            logger.debug("Getting Employee details for [{}]", employeeId);
        }
        return employeeService.getEmployee(employeeId);
    }

    @PostMapping("/employees")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_EDITOR')")
    public void saveEmployee(@RequestBody Employee employee) {
        if (logger.isDebugEnabled()) {
            logger.debug("Saving Employee details with ID [{}]", employee.getId());
        }
        employeeService.saveEmployee(employee);
        logger.debug("Employee record added successfully for Employee [{}].", employee.getId());
    }

    @DeleteMapping("/employees/{employeeId}")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    public void deleteEmployee(@PathVariable(name = "employeeId") Long employeeId) {
        if (logger.isDebugEnabled()) {
            logger.debug("Deleting employee details with ID [{}]", employeeId);
        }
        employeeService.deleteEmployee(employeeId);
        logger.debug("Deletion successful.");
    }

    @PutMapping("/employees/{employeeId}")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    public void updateEmployee(@RequestBody Employee employee,
                               @PathVariable(name = "employeeId") Long employeeId) {
        logger.debug("Updating employee details for ID [{}]", employeeId);
        Employee emp = employeeService.getEmployee(employeeId);
        if (emp != null) {
            employeeService.updateEmployee(employee);
            logger.debug("Updated employee details successfully.");
        }
    }

}
