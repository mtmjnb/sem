package com.napier.sem;

import java.sql.*;

public class App {
    /**
     * Connection to MySQL database.
     */
    private Connection connection = null;

    public static void main(String[] args) {
        // Create new Application
        App app = new App();

        // Connect to database
        app.connect();
        // Get Employee
        Employee employee = app.getEmployee(255530);
        // Display results
        app.displayEmployee(employee);

        // Get Salary by Role
        Employee employee2 = app.getSalary("Engineer");
        // Display results
        app.displaySalary(employee2);

        // Disconnect from database
        app.disconnect();
    }

    /**
     * Connect to the MySQL database.
     */
    public void connect() {
        try {
            // Load Database driver
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            System.out.println("Could not load SQL driver");
            System.exit(-1);
        }

        int retries = 10;
        for (int i = 0; i < retries; ++i) {
            System.out.println("Connecting to database...");
            try {
                // Wait a bit for db to start
                Thread.sleep(30000);
                // Connect to database
                connection = DriverManager.getConnection("jdbc:mysql://db:3306/employees?useSSL=false", "root", "example");
                System.out.println("Successfully connected");
                break;
            } catch (SQLException sqle) {
                System.out.println("Failed to connect to database attempt " + Integer.toString(i));
                System.out.println(sqle.getMessage());
            } catch (InterruptedException ie) {
                System.out.println("Thread interrupted? Should not happen.");
            }
        }
    }

    /**
     * Disconnect from the MySQL database.
     */
    public void disconnect() {
        if (connection != null) {
            try {
                // Close connection
                connection.close();
            } catch (Exception e) {
                System.out.println("Error closing connection to database");
            }
        }
    }

    public Employee getEmployee(int ID) {
        try {
            // Create an SQL statement
            Statement statement = connection.createStatement();
            // Create string for SQL statement
            String select = "SELECT employees.emp_no, employees.first_name, employees.last_name, titles.title, "
                                + "salaries.salary, departments.dept_name, "
                                + "manager_info.first_name AS manager_first_name, "
                                + "manager_info.last_name AS manager_last_name "
                             + "FROM employees "
                                + "JOIN titles ON (employees.emp_no = titles.emp_no) "
                                + "JOIN salaries ON (employees.emp_no = salaries.emp_no) "
                                + "JOIN dept_emp ON (employees.emp_no = dept_emp.emp_no) "
                                + "JOIN departments ON (departments.dept_no = dept_emp.dept_no) "
                                + "JOIN dept_manager ON (departments.dept_no = dept_manager.dept_no) "
                                + "JOIN employees AS manager_info ON (dept_manager.emp_no = manager_info.emp_no) "
                             + "WHERE employees.emp_no = " + ID;
            // Execute SQL statement
            ResultSet resultSet = statement.executeQuery(select);
            // Return new employee if valid.
            // Check one is returned
            if (resultSet.next()) {
                Employee employee = new Employee();
                employee.employee_no = resultSet.getInt("emp_no");
                employee.first_name = resultSet.getString("first_name");
                employee.last_name = resultSet.getString("last_name");
                employee.title = resultSet.getString("title");
                employee.salary = resultSet.getInt("salary");
                employee.department_name = resultSet.getString("dept_name");
                employee.manager = resultSet.getString("manager_first_name") + " "
                        + resultSet.getString("manager_last_name");
                return employee;
            } else {
                return null;
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
            System.out.println("Failed to get employee details");
            return null;
        }
    }

    public Employee getSalary(String title) {
        try {
            // Create an SQL statement
            Statement statement = connection.createStatement();
            // Create string for SQL statement
            String select = "SELECT employees.emp_no, employees.first_name, employees.last_name, salaries.salary "
                             + "FROM employees, salaries, titles "
                             + "WHERE employees.emp_no = salaries.emp_no "
                                + "AND employees.emp_no = titles.emp_no "
                                + "AND salaries.to_date = '9999-01-01' "
                                + "AND titles.to_date = '9999-01-01' "
                                + "AND titles.title = " + title + "' "
                             + "ORDER BY employees.emp_no ASC";
            // Execute SQL statement
            ResultSet resultSet = statement.executeQuery(select);
            // Return new employee if valid.
            // Check one is returned
            if (resultSet.next()) {
                Employee employee = new Employee();
                employee.employee_no = resultSet.getInt("emp_no");
                employee.first_name = resultSet.getString("first_name");
                employee.last_name = resultSet.getString("last_name");
                employee.salary = resultSet.getInt("salary");
                return employee;
            } else {
                return null;
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
            System.out.println("Failed to get employee details");
            return null;
        }
    }

    public void displayEmployee(Employee employee) {
        if (employee != null) {
            System.out.println(
                    employee.employee_no + " "
                            + employee.first_name + " "
                            + employee.last_name + "\n"
                            + employee.title + "\n"
                            + "Salary:" + employee.salary + "\n"
                            + employee.department_name + "\n"
                            + "Manager: " + employee.manager + "\n");
        }
    }

    public void displaySalary(Employee employee) {
        if (employee != null) {
            System.out.println(
                    employee.employee_no + "\t"
                            + employee.first_name + "\t"
                            + employee.last_name + "\t"
                            + employee.salary);
        }
    }
}