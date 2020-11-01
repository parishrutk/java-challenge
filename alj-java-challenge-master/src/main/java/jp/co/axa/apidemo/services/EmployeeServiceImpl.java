package jp.co.axa.apidemo.services;

import jp.co.axa.apidemo.entities.Employee;
import jp.co.axa.apidemo.excpetions.ApiRuntimeException;
import jp.co.axa.apidemo.repositories.EmployeeRepository;
import jp.co.axa.apidemo.utils.ExceptionUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service(value = "EmployeeServiceImpl")
public class EmployeeServiceImpl implements EmployeeService {

    @Autowired
    private EmployeeRepository employeeRepository;

    private Logger logger = LoggerFactory.getLogger(EmployeeServiceImpl.class);

    /**
     * @return - a list of All employees present in the System.
     */
    @Cacheable(value = "employees")
    public List<Employee> listAllEmployees() {
        List<Employee> employees = null;
        try {
            employees = employeeRepository.findAll();
            if (Objects.isNull(employees) || CollectionUtils.isEmpty(employees)) {
                logger.warn("No records found for Employees.");
            }
        } catch (Throwable t) {
            logger.error("Error occurred while fetching Employee records.");
            t.printStackTrace();
            throw ExceptionUtil.prepareExceptionDetails("Get all operation Failed!", HttpStatus.INTERNAL_SERVER_ERROR, t.getLocalizedMessage());
        }
        return employees;
    }

    /**
     * @param employeeId - for retrieving the record of an employee for given employeeId.
     * @return - an employee record using a ResponseEntity object.
     */
    @Cacheable(cacheNames = "employees", key = "#employeeId")
    public Employee getEmployee(Long employeeId) {
        Employee employee;
        try {
            employee = findFirst(employeeId, false);
        } catch (EntityNotFoundException entityNotFoundException) {
            throw entityNotFoundException;
        } catch (Throwable t) {
            logger.error("Unable to fetch record for Employee [{}]", employeeId);
            t.printStackTrace();
            throw ExceptionUtil.prepareExceptionDetails("Get operation failed!", HttpStatus.INTERNAL_SERVER_ERROR, t.getLocalizedMessage());
        }
        return employee;
    }

    /**
     * @param employee - for saving the record of an Employee in the System.
     * @return - Only the HttpStatus as CREATED using ResponseEntity object.
     */
    public Employee saveEmployee(Employee employee) {
        try {
            if (employee != null && findFirst(employee.getId(), true) != null) {
                logger.error("Employee record already exists for id [{}]", employee.getId());
                throw ExceptionUtil.prepareExceptionDetails("Record already exists in the System for given Id [" + employee.getId() + "]", HttpStatus.BAD_REQUEST);
            }
            employee = employeeRepository.save(employee);
        } catch (ApiRuntimeException apiRuntimeException) {
            throw apiRuntimeException;
        } catch (Throwable t) {
            logger.error("Unable to add a new record for Employee [{}]", employee);
            t.printStackTrace();
            throw ExceptionUtil.prepareExceptionDetails("Save operation failed!", HttpStatus.INTERNAL_SERVER_ERROR, t.getLocalizedMessage());
        }
        return employee;
    }

    /**
     * @param employeeId - for updating a particular employee record in the System.
     * @param employee   - for updating the record of an Employee in the System for a given EmployeeId.
     * @return - Only the HttpStatus as NO_CONTENT using ResponseEntity object.
     */
    @CachePut(cacheNames = "employees", key = "#employeeId")
    public Employee updateEmployee(Long employeeId, Employee employee) {
        try {
            if (!Objects.equals(employeeId, employee.getId())) {
                throw ExceptionUtil.prepareExceptionDetails("Invalid Payload provided, please check if EmployeeId is correct or not.", HttpStatus.BAD_REQUEST);
            }
            if (findFirst(employeeId, false) != null)
                employeeRepository.save(employee);
        } catch (EntityNotFoundException | ApiRuntimeException apiException) {
            throw apiException;
        } catch (Throwable t) {
            logger.error("Unable to update the record for Employee [{}] with employee details [{}]", employee.getId(), employee);
            t.printStackTrace();
            throw ExceptionUtil.prepareExceptionDetails("Update operation failed!", HttpStatus.INTERNAL_SERVER_ERROR, t.getLocalizedMessage());
        }
        return employee;
    }

    /**
     * @param employeeId - Delete the record of an employee from the System given an employeeId
     * @return only the HttpStatus as OK using the ResponseEntity object.
     */
    @CacheEvict(cacheNames = "employees", key = "#employeeId", allEntries = false)
    public Boolean deleteEmployee(Long employeeId) {
        try {
            Employee employeeOptional = findFirst(employeeId, false);
            if (employeeOptional != null)
                employeeRepository.delete(employeeOptional);
        } catch (EntityNotFoundException entityNotFoundException) {
            throw entityNotFoundException;
        } catch (Throwable t) {
            logger.error("Unable to delete the record for Employee [{}]", employeeId);
            t.printStackTrace();
            throw ExceptionUtil.prepareExceptionDetails("Delete operation failed!", HttpStatus.INTERNAL_SERVER_ERROR, t.getLocalizedMessage());
        }
        return Boolean.TRUE;
    }

    /**
     * @param employeeId      - for checking if an employee record exists in the system for given employeeId.
     * @param isSaveOperation - decider flag to decide if to throw an EntityNotFoundException in case of save/update operation.
     * @return
     */
    private Employee findFirst(Long employeeId, boolean isSaveOperation) {
        Optional<Employee> optionalOfEmployee = employeeRepository.findById(employeeId);
        if (!isSaveOperation) {
            return optionalOfEmployee.orElseThrow(() ->
                    new EntityNotFoundException("Given employee Id [" + employeeId + "] is invalid or record does not exist."));
        } else {
            return optionalOfEmployee.orElse(null);
        }
    }
}