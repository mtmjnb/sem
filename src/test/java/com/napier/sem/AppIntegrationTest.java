package com.napier.sem;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class AppIntegrationTest {
    static App app;

    @BeforeAll
    static void init() {
        app = new App();
        app.connect("localhost:33060", 30000);
    }

    @Test
    void testGetEmployee() {
        Employee employee = app.getEmployee(255530);
        assertEquals(employee.employee_no, 255530);
        assertEquals(employee.first_name, "Ronghao");
        assertEquals(employee.last_name, "Garigliano");
    }

    @Test
    void testAddEmployee() {
        Employee employee = new Employee();
        employee.employee_no = 500000;
        employee.first_name = "John";
        employee.last_name = "Doe";
        app.addEmployee(employee);
        employee = app.getBasicEmployee(500000);
        assertEquals(employee.employee_no, 500000);
        assertEquals(employee.first_name, "John");
        assertEquals(employee.last_name, "Doe");
    }
}
