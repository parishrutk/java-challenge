package jp.co.axa.apidemo.services;

import jp.co.axa.apidemo.entities.Employee;
import jp.co.axa.apidemo.excpetions.ApiRuntimeException;
import jp.co.axa.apidemo.repositories.EmployeeRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.api.support.membermodification.MemberMatcher;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;

import javax.persistence.EntityNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.powermock.api.mockito.PowerMockito.when;

@SpringBootTest
@RunWith(PowerMockRunner.class)
@PrepareForTest(EmployeeServiceImpl.class)
public class EmployeeServiceImplTest {

    @InjectMocks
    EmployeeServiceImpl employeeService;

    @Mock
    EmployeeRepository employeeRepository;

    List<Employee> employeeList = new ArrayList<>();

    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);
        Employee employee = new Employee(911L, "HR1", 100000, "HR");
        Employee employee1 = new Employee(440L, "Tech11", 72333, "TECH");

        employeeList.add(employee);
        employeeList.add(employee1);
    }

    @Test
    public void getEmployeeTest_success() {
        Long empId = 123L;
        when(employeeRepository.findById(empId)).thenReturn(java.util.Optional.ofNullable(employeeList.get(0)));
        Employee employee = employeeService.getEmployee(empId);

        assertThat(employee).isNotNull();
        assertThat(employee).isEqualTo(employeeList.get(0));
    }

    @Test
    public void getEmployeeTest_entityNotFoundExceptionThrown() {
        Long empId = 123L;
        when(employeeRepository.findById(empId)).thenReturn(Optional.empty());
        try {
            Employee employee = employeeService.getEmployee(empId);
        } catch (EntityNotFoundException entityNotFoundException) {
            assertThat(entityNotFoundException).isInstanceOf(EntityNotFoundException.class);
            assertThat(entityNotFoundException.getMessage()).isEqualTo("Given employee Id [" + empId + "] is invalid or record does not exist.");
        }
    }

    @Test
    public void getEmployeeTest_runtimeExceptionThrown() {
        Long empId = 123L;
        when(employeeRepository.findById(empId)).thenThrow(new RuntimeException("Something went wrong."));
        try {
            Employee employee = employeeService.getEmployee(empId);
        } catch (Throwable throwable) {
            assertThat(throwable).isInstanceOf(ApiRuntimeException.class);
            ApiRuntimeException runtimeException = (ApiRuntimeException) throwable;
            assertThat(runtimeException.getErrorMessage()).isEqualTo("Get operation failed!");
            assertThat(runtimeException.getErrorStatus()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Test
    public void getAllEmployeesTest_success() {
        when(employeeRepository.findAll()).thenReturn(employeeList);
        List<Employee> allEmployees = employeeService.listAllEmployees();

        assertThat(allEmployees).isNotNull();
        assertThat(allEmployees.size()).isEqualTo(employeeList.size());
    }

    @Test
    public void getAllEmployeesTest_emptyList_success() {
        List<Employee> emptyList = new ArrayList<>();
        when(employeeRepository.findAll()).thenReturn(emptyList);
        List<Employee> allEmployees = employeeService.listAllEmployees();

        assertThat(allEmployees).isNotNull();
        assertThat(allEmployees).isEmpty();
    }

    @Test
    public void getAllEmployeesTest_runtimeExceptionThrown() {
        Long empId = 123L;
        when(employeeRepository.findAll()).thenThrow(new RuntimeException("Get all operation Failed!"));
        try {
            List<Employee> employees = employeeService.listAllEmployees();
        } catch (Throwable throwable) {
            assertThat(throwable).isInstanceOf(ApiRuntimeException.class);
            ApiRuntimeException runtimeException = (ApiRuntimeException) throwable;
            assertThat(runtimeException.getErrorMessage()).isEqualTo("Get all operation Failed!");
            assertThat(runtimeException.getErrorStatus()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Test
    public void saveEmployeesTest_success() {
        Employee saveEmployee = new Employee(103L, "Test1", 100000, "QAD");
        when(employeeRepository.save(saveEmployee)).thenReturn(employeeList.get(0));
        Employee savedEmployee = employeeService.saveEmployee(saveEmployee);

        assertThat(savedEmployee).isNotNull();
        assertThat(savedEmployee).isEqualTo(employeeList.get(0));
    }

    @Test
    public void saveEmployeesTest_recordAlreadyExists_runtimeExceptionThrown() throws Exception {
        Employee newEmployee = null;
        EmployeeServiceImpl service = new EmployeeServiceImpl();
        EmployeeServiceImpl serviceSpy = PowerMockito.spy(service);
        Employee saveEmployee = new Employee(911L, "Test1", 100000, "QAD");
        when(employeeRepository.findById(saveEmployee.getId())).thenReturn(Optional.ofNullable(saveEmployee));
        when(employeeRepository.save(saveEmployee)).thenReturn(employeeList.get(0));
        try {
            PowerMockito.doReturn(saveEmployee).when(serviceSpy, MemberMatcher.method(EmployeeServiceImpl.class, "findFirst")).withArguments(saveEmployee.getId(), true);
            newEmployee = employeeService.saveEmployee(saveEmployee);
        } catch (ApiRuntimeException apiRuntimeException) {
            assertThat(apiRuntimeException.getErrorStatus()).isEqualTo(HttpStatus.BAD_REQUEST);
            assertThat(apiRuntimeException.getErrorMessage()).isEqualTo("Record already exists in the System for given Id [" + saveEmployee.getId() + "]");
        }
    }

    @Test
    public void saveEmployeesTest_runtimeExceptionThrown() throws Exception {
        Employee newEmployee = null;
        EmployeeServiceImpl service = new EmployeeServiceImpl();
        EmployeeServiceImpl serviceSpy = PowerMockito.spy(service);
        Employee saveEmployee = new Employee(911L, "Test1", 100000, "QAD");
        when(employeeRepository.findById(saveEmployee.getId())).thenReturn(Optional.empty());
        when(employeeRepository.save(saveEmployee)).thenThrow(new RuntimeException("Save operation failed!"));
        try {
            PowerMockito.doReturn(saveEmployee).when(serviceSpy, MemberMatcher.method(EmployeeServiceImpl.class, "findFirst")).withArguments(saveEmployee.getId(), true);
            newEmployee = employeeService.saveEmployee(saveEmployee);
        } catch (ApiRuntimeException apiRuntimeException) {
            assertThat(apiRuntimeException.getErrorStatus()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR);
            assertThat(apiRuntimeException.getErrorMessage()).isEqualTo("Save operation failed!");
        }
    }

    @Test
    public void updateEmployeesTest_success() {
        Long empId = 911L;
        Employee saveEmployee = new Employee(911L, "My Test", 200000, "QAT");
        when(employeeRepository.save(saveEmployee)).thenReturn(employeeList.get(0));
        when(employeeRepository.findById(empId)).thenReturn(Optional.ofNullable(employeeList.get(0)));
        Employee updatedEmployee = employeeService.updateEmployee(empId, saveEmployee);

        assertThat(updatedEmployee).isNotNull();
        assertThat(updatedEmployee).isEqualTo(saveEmployee);
    }

    @Test
    public void updateEmployeesTest_invalidEmployeeIdProvided_runtimeExceptionThrown() {
        Long empId = 951L;
        Employee saveEmployee = new Employee(911L, "My Test", 200000, "QAT");
        when(employeeRepository.save(saveEmployee)).thenReturn(employeeList.get(0));
        when(employeeRepository.findById(empId)).thenReturn(Optional.ofNullable(employeeList.get(0)));
        try {
            Employee updatedEmployee = employeeService.updateEmployee(empId, saveEmployee);
        } catch (ApiRuntimeException apiRuntimeException) {
            assertThat(apiRuntimeException.getErrorStatus()).isEqualTo(HttpStatus.BAD_REQUEST);
            assertThat(apiRuntimeException.getErrorMessage()).isEqualTo("Invalid Payload provided, please check if EmployeeId is correct or not.");
        }
    }

    @Test
    public void updateEmployeesTest_entityNotFoundExceptionThrown() {
        Long empId = 911L;
        Employee saveEmployee = new Employee(911L, "My Test", 200000, "QAT");
        when(employeeRepository.save(saveEmployee)).thenReturn(employeeList.get(0));
        when(employeeRepository.findById(empId)).thenReturn(Optional.empty());
        try {
            Employee updatedEmployee = employeeService.updateEmployee(empId, saveEmployee);
        } catch (EntityNotFoundException apiRuntimeException) {
            assertThat(apiRuntimeException).isInstanceOf(EntityNotFoundException.class);
            EntityNotFoundException entityNotFoundException = (EntityNotFoundException) apiRuntimeException;
            assertThat(entityNotFoundException.getLocalizedMessage()).isEqualTo("Given employee Id [" + empId + "] is invalid or record does not exist.");
        }
    }

    @Test
    public void updateEmployeesTest_runtimeExceptionThrown() {
        Long empId = 911L;
        Employee saveEmployee = new Employee(911L, "My Test", 200000, "QAT");
        when(employeeRepository.save(saveEmployee)).thenThrow(new RuntimeException("Update operation failed!"));
        when(employeeRepository.findById(empId)).thenReturn(Optional.ofNullable(saveEmployee));
        try {
            Employee updatedEmployee = employeeService.updateEmployee(empId, saveEmployee);
        } catch (Throwable apiRuntimeException) {
            assertThat(apiRuntimeException).isInstanceOf(ApiRuntimeException.class);
            ApiRuntimeException apiException = (ApiRuntimeException) apiRuntimeException;
            assertThat(apiException.getErrorMessage()).isEqualTo("Update operation failed!");
            assertThat(apiException.getErrorStatus()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Test
    public void deleteEmployeeTest_success() {
        Long empId = 123L;
        when(employeeRepository.findById(empId)).thenReturn(Optional.ofNullable(employeeList.get(0)));
        Boolean result = employeeService.deleteEmployee(empId);

        assertThat(result).isNotNull();
        assertThat(result).isTrue();
    }

    @Test
    public void deleteEmployeeTest_entityNotFoundExceptionThrown() {
        Long empId = 123L;
        when(employeeRepository.findById(empId)).thenReturn(Optional.empty());
        try {
            Boolean result = employeeService.deleteEmployee(empId);
        } catch (EntityNotFoundException entityNotFoundException) {
            assertThat(entityNotFoundException).isInstanceOf(EntityNotFoundException.class);
            EntityNotFoundException apiException = (EntityNotFoundException) entityNotFoundException;
            assertThat(apiException.getMessage()).isEqualTo("Given employee Id [" + empId + "] is invalid or record does not exist.");
        }
    }

   /* @Test
    public void deleteEmployeesTest_runtimeExceptionThrown() {
        Long empId = 911L;
        EmployeeServiceImpl service = new EmployeeServiceImpl();
        EmployeeServiceImpl serviceSpy = PowerMockito.spy(service);
        Employee saveEmployee = employeeList.get(0);
        when(employeeRepository.findById(empId)).thenReturn(Optional.ofNullable(saveEmployee));
        try {
            PowerMockito.doReturn(null).when(serviceSpy, MemberMatcher.method(EmployeeServiceImpl.class, "findFirst")).withArguments(saveEmployee.getId(), true);
            Boolean result = employeeService.deleteEmployee(empId);
        } catch (Throwable apiRuntimeException) {
            assertThat(apiRuntimeException).isInstanceOf(ApiRuntimeException.class);
            ApiRuntimeException apiException = (ApiRuntimeException) apiRuntimeException;
            assertThat(apiException.getErrorMessage()).isEqualTo("Delete operation failed!");
            assertThat(apiException.getErrorStatus()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }*/
}
