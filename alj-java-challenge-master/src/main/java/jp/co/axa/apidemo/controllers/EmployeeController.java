package jp.co.axa.apidemo.controllers;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import jp.co.axa.apidemo.entities.Employee;
import jp.co.axa.apidemo.services.EmployeeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/v1")
public class EmployeeController {

    @Autowired
    private EmployeeService employeeService;

    /*public void setEmployeeService(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }*/

    Logger logger = LoggerFactory.getLogger(EmployeeController.class);

    @GetMapping("/employees")
    @PreAuthorize("hasRole('ROLE_VIEWER') or hasRole('ROLE_EDITOR') or hasRole('ROLE_ADMIN')")
    @ApiOperation(value = "This API will get all the employees present in the System."
            , consumes = MediaType.APPLICATION_JSON_VALUE
            , produces = MediaType.APPLICATION_JSON_VALUE
            , notes = "Presents a list of Employees."
            , httpMethod = "GET"
            , response = Employee.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successful Retrieval"),
            @ApiResponse(code = 400, message = "Bad Request"),
            @ApiResponse(code = 401, message = "Unauthorized"),
            @ApiResponse(code = 403, message = "Forbidden"),
            @ApiResponse(code = 404, message = "Resource not found"),
            @ApiResponse(code = 500, message = "Internal Server error",
                    response = Employee.class, responseContainer = "List")})
    public ResponseEntity<List<Employee>> getEmployees() {
        if (logger.isDebugEnabled()) {
            logger.debug("Getting all employee details.");
        }
        List<Employee> employeeList = employeeService.listAllEmployees();
        return new ResponseEntity<List<Employee>>(employeeList, HttpStatus.OK);
    }

    @GetMapping("/employees/{employeeId}")
    @PreAuthorize("hasRole('ROLE_VIEWER') or hasRole('ROLE_EDITOR') or hasRole('ROLE_ADMIN')")
    @ApiOperation(value = "This API will get an employee by its Id"
            , consumes = MediaType.APPLICATION_JSON_VALUE
            , produces = MediaType.APPLICATION_JSON_VALUE
            , notes = "It will present an employee when an employeeId is provided."
            , httpMethod = "GET"
            , response = Employee.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successful Retrieval"),
            @ApiResponse(code = 400, message = "Bad Request"),
            @ApiResponse(code = 401, message = "Unauthorized"),
            @ApiResponse(code = 403, message = "Forbidden"),
            @ApiResponse(code = 404, message = "Resource not found"),
            @ApiResponse(code = 500, message = "Internal Server error",
                    response = Employee.class)})
    public ResponseEntity<Employee> getEmployee(@ApiParam(value = "employeeId",
            required = true) @PathVariable(name = "employeeId") Long employeeId) {
        if (logger.isDebugEnabled()) {
            logger.debug("Getting Employee details for [{}]", employeeId);
        }
        Employee employee = employeeService.getEmployee(employeeId);
        return new ResponseEntity<Employee>(employee, HttpStatus.OK);

    }

    @PostMapping("/employees")
    @PreAuthorize("hasRole('ROLE_EDITOR') or hasRole('ROLE_ADMIN')")
    @ApiOperation(value = "This API will create an employee record when a valid payload is provided."
            , consumes = MediaType.APPLICATION_JSON_VALUE
            , produces = MediaType.APPLICATION_JSON_VALUE
            , notes = "It will add an employee record when a valid payload is provided."
            , httpMethod = "POST"
            , response = Void.class)
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Successful Creation"),
            @ApiResponse(code = 400, message = "Bad Request"),
            @ApiResponse(code = 401, message = "Unauthorized"),
            @ApiResponse(code = 403, message = "Forbidden"),
            @ApiResponse(code = 404, message = "Resource not found"),
            @ApiResponse(code = 500, message = "Internal Server error",
                    response = Void.class)})
    public ResponseEntity<Void> saveEmployee(@Valid @RequestBody Employee employee, HttpServletResponse response) {
        if (logger.isDebugEnabled()) {
            logger.debug("Saving Employee details with ID [{}]", employee.getId());
        }
        Employee savedEmployee = employeeService.saveEmployee(employee);
        if (logger.isDebugEnabled()) {
            logger.debug("Employee record added successfully for Employee [{}].", savedEmployee.getId());
        }
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @PutMapping("/employees/{employeeId}")
    @PreAuthorize("hasRole('ROLE_EDITOR') or hasRole('ROLE_ADMIN')")
    @ApiOperation(value = "This API will update an employee record when a valid payload is provided with a valid employeeId."
            , consumes = MediaType.APPLICATION_JSON_VALUE
            , produces = MediaType.APPLICATION_JSON_VALUE
            , notes = "It will update an employee record when a valid payload is provided."
            , httpMethod = "PUT"
            , response = Void.class)
    @ApiResponses(value = {
            @ApiResponse(code = 204, message = "Successful Modification"),
            @ApiResponse(code = 400, message = "Bad Request"),
            @ApiResponse(code = 401, message = "Unauthorized"),
            @ApiResponse(code = 403, message = "Forbidden"),
            @ApiResponse(code = 404, message = "Resource not found"),
            @ApiResponse(code = 500, message = "Internal Server error",
                    response = Void.class)})
    public ResponseEntity<Void> updateEmployee(@ApiParam(value = "employeeId",
            required = true, allowMultiple = false, type = "Long") @Valid @RequestBody Employee employee,
                                               @PathVariable(name = "employeeId") Long employeeId, HttpServletResponse response) {
        if (logger.isDebugEnabled()) {
            logger.debug("Updating employee details for ID [{}]", employeeId);
        }
        Employee updatedEmployee = employeeService.updateEmployee(employeeId, employee);
        if (logger.isDebugEnabled()) {
            logger.debug("Updated employee details successfully.");
        }
        return new ResponseEntity<Void>(HttpStatus.NO_CONTENT);
    }

    @DeleteMapping("/employees/{employeeId}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @ApiOperation(value = "This API will delete an employee record when a valid employeeId is provided."
            , consumes = MediaType.APPLICATION_JSON_VALUE
            , produces = MediaType.APPLICATION_JSON_VALUE
            , notes = "It will delete an employee record when a valid employeeId is provided."
            , httpMethod = "DELETE"
            , response = Void.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successful Deletion"),
            @ApiResponse(code = 401, message = "Unauthorized"),
            @ApiResponse(code = 403, message = "Forbidden"),
            @ApiResponse(code = 404, message = "Resource not found"),
            @ApiResponse(code = 500, message = "Internal Server error",
                    response = Void.class)})
    public ResponseEntity<Void> deleteEmployee(@ApiParam(value = "employeeId",
            required = true, allowMultiple = false, type = "Long") @PathVariable(name = "employeeId") Long employeeId) {
        if (logger.isDebugEnabled()) {
            logger.debug("Deleting employee details with ID [{}]", employeeId);
        }
        Boolean result = employeeService.deleteEmployee(employeeId);
        if (logger.isDebugEnabled()) {
            logger.debug("Deletion successful with Result: " + result);
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }

}
