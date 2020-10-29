package jp.co.axa.apidemo.services;

import jp.co.axa.apidemo.entities.Employee;
import jp.co.axa.apidemo.excpetions.ApiRuntimeException;
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

    //TODO: Make this cache conditional
    //@Cacheable("employee")
    public List<Employee> listAllEmployees() {
        List<Employee> employees = null;
        try {
            employees = employeeRepository.findAll();
            if (Objects.isNull(employees)) {
                logger.warn("No records found for Employees.");
            }
        } catch (Throwable t) {
            logger.error("Error occurred while fetching Employee records.");
            t.printStackTrace();
            ExceptionUtil.prepareExceptionDetails("Something went wrong!", HttpStatus.INTERNAL_SERVER_ERROR, t.getLocalizedMessage());
        }
        return employees;
    }

    //@Cacheable(cacheNames = "employee", key = "#employeeId")
    public Employee getEmployee(Long employeeId) {
        Employee employee = null;
        try {
            employee = findFirst(employeeId, false);
        } catch (EntityNotFoundException entityNotFoundException) {
            throw entityNotFoundException;
        } catch (Throwable t) {
            logger.error("Unable to fetch record for Employee [{}]", employeeId);
            t.printStackTrace();
            ExceptionUtil.prepareExceptionDetails("Get operation failed!", HttpStatus.INTERNAL_SERVER_ERROR, t.getLocalizedMessage());
        }
        return employee;
    }

    public void saveEmployee(Employee employee) {
        try {
            if (employee != null && findFirst(employee.getId(), true) != null) {
                logger.error("Employee record already exists for id [{}]", employee.getId());
                throw ExceptionUtil.prepareExceptionDetails("Record already exists in the System.", HttpStatus.BAD_REQUEST);
            }
            employeeRepository.save(employee);
        } catch (ApiRuntimeException apiRuntimeException) {
            throw apiRuntimeException;
        } catch (Throwable t) {
            logger.error("Unable to add a new record for Employee [{}]", employee);
            t.printStackTrace();
            ExceptionUtil.prepareExceptionDetails("Save operation failed!", HttpStatus.INTERNAL_SERVER_ERROR, t.getLocalizedMessage());
        }
    }

    //@CachePut(cacheNames = "employee", key = "#employee.id")
    public void updateEmployee(Long employeeId, Employee employee) {
        try {
            if (!Objects.equals(employeeId, employee.getId())) {
                throw ExceptionUtil.prepareExceptionDetails("Invalid Payload provided, please check if EmployeeID is correct or not.", HttpStatus.BAD_REQUEST);
            }
            if (findFirst(employeeId, true) != null)
                employeeRepository.save(employee);
        } catch (ApiRuntimeException apiRuntimeException) {
            throw apiRuntimeException;
        } catch (Throwable t) {
            logger.error("Unable to update the record for Employee [{}] with employee details [{}]", employee.getId(), employee);
            t.printStackTrace();
            ExceptionUtil.prepareExceptionDetails("Update operation failed!", HttpStatus.INTERNAL_SERVER_ERROR, t.getLocalizedMessage());
        }
    }

    // @CacheEvict(cacheNames = "employee", key = "#employeeId")
    public void deleteEmployee(Long employeeId) {
        try {
            if (findFirst(employeeId, false) != null)
                employeeRepository.deleteById(employeeId);
        } catch (Throwable t) {
            logger.error("Unable to delete the record for Employee [{}]", employeeId);
            ExceptionUtil.prepareExceptionDetails("Delete operation failed!", HttpStatus.INTERNAL_SERVER_ERROR, t.getLocalizedMessage());
            t.printStackTrace();
        }
    }

    private Employee findFirst(Long employeeId, boolean isSaveOperation) {
        Optional<Employee> optEmp = employeeRepository.findById(employeeId);
        if (!isSaveOperation) {
            return optEmp.orElseThrow(() ->
                    new EntityNotFoundException("Given employee Id [" + employeeId + "] is invalid or record does not exist"));
        } else {
            return optEmp.orElse(null);
        }
    }
}