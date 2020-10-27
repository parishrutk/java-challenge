package jp.co.axa.apidemo.services;

import jp.co.axa.apidemo.entities.Employee;
import jp.co.axa.apidemo.repositories.EmployeeRepository;
import jp.co.axa.apidemo.utils.ExceptionUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service(value = "EmployeeServiceImpl")
public class EmployeeServiceImpl implements EmployeeService {

    @Autowired
    private EmployeeRepository employeeRepository;

    private Logger logger = LoggerFactory.getLogger(EmployeeServiceImpl.class);

    public List<Employee> retrieveEmployees() {
        List<Employee> employees;
        try {
            employees = employeeRepository.findAll();
            if (Objects.isNull(employees)) {
                logger.warn("No records found for Employees.");
            }
        } catch (Throwable t) {
            throw ExceptionUtil.prepareExceptionDetails("Something went wrong !", HttpStatus.INTERNAL_SERVER_ERROR, t);
        }
        return employees;
    }

    public Employee getEmployee(Long employeeId) {
        Optional<Employee> optEmp = employeeRepository.findById(employeeId);
        Employee employee = optEmp.orElseThrow(() ->
                new EntityNotFoundException("Given employee Id [" + employeeId + "] is invalid or record does not exist"));
        return employee;
    }

    public void saveEmployee(Employee employee) {
        try {
            employeeRepository.save(employee);
        } catch (Throwable t) {
            logger.error("Unable to add a new record for Employee [{}]", employee);
            throw ExceptionUtil.prepareExceptionDetails("Something went wrong !", HttpStatus.INTERNAL_SERVER_ERROR, t);
        }

    }

    public void deleteEmployee(Long employeeId) {
        try {
            employeeRepository.deleteById(employeeId);
        } catch (Throwable t) {
            logger.error("Unable to delete the record for Employee [{}]", employeeId);
            throw ExceptionUtil.prepareExceptionDetails("Something went wrong !", HttpStatus.INTERNAL_SERVER_ERROR, t);
        }
    }

    public void updateEmployee(Employee employee) {
        try {
            employeeRepository.save(employee);
        } catch (Throwable t) {
            logger.error("Unable to update the record for Employee [{}] with employee details [{}]", employee.getId(), employee);
            throw ExceptionUtil.prepareExceptionDetails("Something went wrong !", HttpStatus.INTERNAL_SERVER_ERROR, t);
        }
    }
}