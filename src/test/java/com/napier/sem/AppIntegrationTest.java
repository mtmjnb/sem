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
}
