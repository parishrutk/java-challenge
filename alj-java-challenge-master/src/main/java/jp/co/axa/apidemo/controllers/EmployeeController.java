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
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/v1")
public class EmployeeController {

    @Autowired
    private EmployeeService employeeService;

    Logger logger = LoggerFactory.getLogger(EmployeeController.class);

    @GetMapping("/employees")
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
            @ApiResponse(code = 404, message = "Service not found"),
            @ApiResponse(code = 500, message = "Internal Server error",
                    response = Employee.class, responseContainer = "List")})
    public List<Employee> getEmployees() {
        if (logger.isDebugEnabled()) {
            logger.debug("Getting all employee details.");
        }
        List<Employee> employees = employeeService.listAllEmployees();
        return employees;
    }

    @GetMapping("/employees/{employeeId}")
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
            @ApiResponse(code = 404, message = "Service not found"),
            @ApiResponse(code = 500, message = "Internal Server error",
                    response = Employee.class)})
    public Employee getEmployee(@ApiParam(value = "employeeId",
            required = true) @PathVariable(name = "employeeId") Long employeeId) {
        if (logger.isDebugEnabled()) {
            logger.debug("Getting Employee details for [{}]", employeeId);
        }
        return employeeService.getEmployee(employeeId);
    }

    @PostMapping("/employees")
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
            @ApiResponse(code = 404, message = "Service not found"),
            @ApiResponse(code = 500, message = "Internal Server error",
                    response = Void.class)})
    public void saveEmployee(@Valid @RequestBody Employee employee, HttpServletResponse response) {
        if (logger.isDebugEnabled()) {
            logger.debug("Saving Employee details with ID [{}]", employee.getId());
        }
        employeeService.saveEmployee(employee);
        logger.debug("Employee record added successfully for Employee [{}].", employee.getId());
        response.setStatus(HttpServletResponse.SC_CREATED);
    }

    @PutMapping("/employees/{employeeId}")
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
            @ApiResponse(code = 404, message = "Service not found"),
            @ApiResponse(code = 500, message = "Internal Server error",
                    response = Void.class)})
    public void updateEmployee(@ApiParam(value = "employeeId",
            required = true,allowMultiple = false,type = "Long") @Valid @RequestBody Employee employee,
                               @PathVariable(name = "employeeId") Long employeeId, HttpServletResponse response) {
        if (logger.isDebugEnabled()) {
            logger.debug("Updating employee details for ID [{}]", employeeId);
        }
        employeeService.updateEmployee(employeeId, employee);
        logger.debug("Updated employee details successfully.");
        response.setStatus(HttpServletResponse.SC_NO_CONTENT);
    }

    @DeleteMapping("/employees/{employeeId}")
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
            @ApiResponse(code = 404, message = "Service not found"),
            @ApiResponse(code = 500, message = "Internal Server error",
                    response = Void.class)})
    public void deleteEmployee(@ApiParam(value = "employeeId",
            required = true,allowMultiple = false,type = "Long") @PathVariable(name = "employeeId") Long employeeId) {
        if (logger.isDebugEnabled()) {
            logger.debug("Deleting employee details with ID [{}]", employeeId);
        }
        employeeService.deleteEmployee(employeeId);
        logger.debug("Deletion successful.");
    }

}
