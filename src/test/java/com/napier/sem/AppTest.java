package com.napier.sem;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

class AppTest {
    static App app;

    @BeforeAll
    static void init() {
        app = new App();
    }

    @Test
    void printSalariesTestNull() {
        app.printSalaries(null);
    }

    @Test
    void printSalariesTestEmpty() {
        ArrayList<Employee> employees = new ArrayList<>();
        app.printSalaries(employees);
    }

    @Test
    void printSalariesTestContainsNull() {
        ArrayList<Employee> employees = new ArrayList<>();
        employees.add(null);
        app.printSalaries(employees);
    }

    @Test
    void printSalaries() {
        ArrayList<Employee> employees = new ArrayList<>();
        Employee employee = new Employee();
        employee.employee_no = 1;
        employee.first_name = "John";
        employee.last_name = "Doe";
        employee.title = "Engineer";
        employee.salary = 55000;
        employees.add(employee);
        app.printSalaries(employees);
    }
}
