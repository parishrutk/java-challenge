package jp.co.axa.apidemo.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import jp.co.axa.apidemo.entities.Employee;
import jp.co.axa.apidemo.services.EmployeeService;
import lombok.SneakyThrows;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.AutoConfigureDataJpa;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.stereotype.Controller;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.nio.file.AccessDeniedException;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

@RunWith(SpringRunner.class)
@WebMvcTest(EmployeeController.class)
public class EmployeeControllerTest {

    public static final String APP_CONTEXT_PATH = "/api/v1/employees/";

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private EmployeeService employeeService;

    @Autowired
    ObjectMapper objectMapper;

    private List<Employee> employeeList = new ArrayList<>();

    @Before
    public void setup() {
        Employee employee = new Employee(123L, "HR1", 123123, "HR");
        Employee employee1 = new Employee(132L, "Tech11", 123423, "TECH");

        employeeList.add(employee);
        employeeList.add(employee1);
    }

    /**
     * This configuration is required for running the test successfully using the JPA and controller.
     */
    @TestConfiguration
    @AutoConfigureDataJpa
    @ComponentScan(basePackageClasses = EmployeeController.class,
            useDefaultFilters = false,
            includeFilters = {
                    @ComponentScan.Filter(
                            type = FilterType.ASSIGNABLE_TYPE,
                            value = Controller.class
                    )
            }
    )
    static class TestConfig {
    }

    @SneakyThrows
    @Test
    @WithMockUser(value = "viewer", password = "viewer", roles = {"VIEWER"})
    public void testGetAllEmployees_sucess200Ok() {
        given(employeeService.listAllEmployees()).willReturn(employeeList);
        MvcResult mvcResult = this.mockMvc.perform(get("/api/v1/employees")).andReturn();
        List<Employee> list = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), List.class);
        assertThat(list).isNotNull();
        assertThat(list).isNotEmpty();
        assertThat(list).hasSameSizeAs(employeeList);
    }

    @SneakyThrows
    @Test
    @WithMockUser(value = "viewer", password = "viewer", roles = {"VIEWER"})
    public void testGetEmployee() {
        given(employeeService.getEmployee(123L)).willReturn(employeeList.get(0));
        MockHttpServletResponse mockHttpServletResponse = this.mockMvc.perform(get("/api/v1/employees/{employeeId}", 123).accept(MediaType.APPLICATION_JSON)).andReturn().getResponse();
        Employee e = objectMapper.readValue(mockHttpServletResponse.getContentAsString(), Employee.class);
        assertThat(e).isNotNull();
        assertThat(e).isEqualTo(employeeList.get(0));
    }

    @SneakyThrows
    @Test
    @WithMockUser(value = "admin", password = "admin", roles = {"ADMIN"})
    public void testSaveEmployee_success201() {
        Employee test = new Employee(125L, "ACC1", 23332, "ACC");

        // Mockito.when(employeeService.updateEmployee(Mockito.anyLong(), Mockito.any(Employee.class))).thenReturn(new ResponseEntity<>(HttpStatus.CREATED));

        given(employeeService.saveEmployee(test)).willReturn(test);
        MvcResult mvcResult = mockMvc.perform(post("/api/v1/employees/").contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(test))
                .accept(MediaType.APPLICATION_JSON)).andReturn();
        assertThat(mvcResult.getResponse().getStatus()).isEqualTo(HttpStatus.CREATED.value());
    }

    @SneakyThrows
    @Test
    @WithMockUser(value = "admin", password = "admin", roles = {"ADMIN"})
    public void testUpdateEmployee_success204() {
        Employee testEmployee = new Employee(123L, "HR1_Updated", 222222, "HR_Updated");

        // Mockito.when(employeeService.updateEmployee(Mockito.anyLong(), Mockito.any(Employee.class))).thenReturn(new ResponseEntity<>(HttpStatus.CREATED));

        given(employeeService.updateEmployee(123L, testEmployee)).willReturn(testEmployee);
        MvcResult mvcResult = mockMvc.perform(put("/api/v1/employees/{employeeId}", 123).contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(testEmployee))
                .accept(MediaType.APPLICATION_JSON)).andReturn();
        assertThat(mvcResult.getResponse().getStatus()).isEqualTo(HttpStatus.NO_CONTENT.value());
    }

    @SneakyThrows
    @Test
    @WithMockUser(value = "viewer", password = "viewer", roles = {"VIEWER"})
    public void testUpdateEmployee_accessDenied403() {
        Employee testEmployee = new Employee(123L, "HR1_Updated", 222222, "HR_Updated");

        // Mockito.when(employeeService.updateEmployee(Mockito.anyLong(), Mockito.any(Employee.class))).thenReturn(new ResponseEntity<>(HttpStatus.CREATED));

        given(employeeService.updateEmployee(123L, testEmployee)).willReturn(testEmployee);
        try {
            MvcResult mvcResult = mockMvc.perform(put("/api/v1/employees/{employeeId}", 123).contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(testEmployee))
                    .accept(MediaType.APPLICATION_JSON)).andReturn();
        } catch (AccessDeniedException accessDeniedException) {
            assertThat(accessDeniedException.getLocalizedMessage()).isEqualTo("Access is denied");
        }

    }

    @SneakyThrows
    @Test
    @WithMockUser(value = "admin", password = "admin", roles = {"ADMIN"})
    public void testDeleteEmployee_success200() {
        given(employeeService.deleteEmployee(123L)).willReturn(Boolean.TRUE);
        MockHttpServletResponse mockHttpServletResponse = this.mockMvc.perform(delete("/api/v1/employees/{employeeId}", 123).accept(MediaType.APPLICATION_JSON)).andReturn().getResponse();
        assertThat(mockHttpServletResponse.getStatus()).isEqualTo(HttpStatus.OK.value());
    }
}
