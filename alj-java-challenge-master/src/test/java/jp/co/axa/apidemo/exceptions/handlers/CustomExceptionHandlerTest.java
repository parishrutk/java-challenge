package jp.co.axa.apidemo.exceptions.handlers;

import com.fasterxml.jackson.databind.ObjectMapper;
import jp.co.axa.apidemo.entities.Employee;
import lombok.SneakyThrows;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

@SpringBootTest
@RunWith(SpringJUnit4ClassRunner.class)
public class CustomExceptionHandlerTest {

    @Autowired
    private WebApplicationContext webApplicationContext;

    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Before
    public void setUp() throws Exception {
        mockMvc = webAppContextSetup(this.webApplicationContext).build();
    }

    @SneakyThrows
    @Test
    public void testGetEmployee_entityNotFoundExceptionThrown() {
        int employeeId = 123234;
        this.mockMvc.perform(get("/api/v1/employees/{employeeId}", employeeId).accept(MediaType.APPLICATION_JSON)).andExpect(status().isNotFound());
    }

    @SneakyThrows
    @Test
    public void testUpdateEmployee_entityNotFoundExceptionThrown() {
        int employeeId = 123;
        Employee testEmployee = new Employee(123L, "Tech11", 123423, "TECH");
        this.mockMvc.perform(put("/api/v1/employees/{employeeId}", employeeId, testEmployee).contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(testEmployee)));
    }

    @SneakyThrows
    @Test
    public void testDeleteEmployee_entityNotFoundExceptionThrown() {
        int employeeId = 123234;
        this.mockMvc.perform(delete("/api/v1/employees/{employeeId}", employeeId).accept(MediaType.APPLICATION_JSON)).andExpect(status().isNotFound());
    }

}
