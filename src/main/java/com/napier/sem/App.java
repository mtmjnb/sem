package com.napier.sem;

import java.sql.*;
import java.util.ArrayList;

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

        // Extract employee salary information
        ArrayList<Employee> employees = app.getAllSalaries();

        // Display results
        app.printSalaries(employees);

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

    public ArrayList<Employee> getSalaries(String title) {
        try {
            ArrayList<Employee> salaries = new ArrayList<>();
            // Create an SQL statement
            Statement statement = connection.createStatement();
            // Create string for SQL statement
            String select = "SELECT employees.emp_no, employees.first_name, employees.last_name, salaries.salary "
                             + "FROM employees, salaries, titles "
                             + "WHERE employees.emp_no = salaries.emp_no "
                                + "AND employees.emp_no = titles.emp_no "
                                + "AND salaries.to_date = '9999-01-01' "
                                + "AND titles.to_date = '9999-01-01' "
                                + "AND titles.title = '" + title + "' "
                             + "ORDER BY employees.emp_no ASC";
            // Execute SQL statement
            ResultSet resultSet = statement.executeQuery(select);
            // Return new employee if valid.
            // Check one is returned
            while (resultSet.next()) {
                Employee employee = new Employee();
                employee.employee_no = resultSet.getInt("emp_no");
                employee.first_name = resultSet.getString("first_name");
                employee.last_name = resultSet.getString("last_name");
                employee.salary = resultSet.getInt("salary");
                salaries.add(employee);
            }
            return salaries;
        } catch (Exception e) {
            System.out.println(e.getMessage());
            System.out.println("Failed to get employee details");
            return null;
        }
    }

    /**
     * Gets all the current employees and salaries.
     * @return A list of all employees and salaries, or null if there is an error.
     */
    public ArrayList<Employee> getAllSalaries() {
        try {
            // Create an SQL statement
            Statement statement = connection.createStatement();
            // Create string for SQL statement
            String select =
                    "SELECT employees.emp_no, employees.first_name, employees.last_name, salaries.salary "
                            + "FROM employees, salaries "
                            + "WHERE employees.emp_no = salaries.emp_no AND salaries.to_date = '9999-01-01' "
                            + "ORDER BY employees.emp_no ASC";
            // Execute SQL statement
            ResultSet resultSet = statement.executeQuery(select);
            // Extract employee information
            ArrayList<Employee> employees = new ArrayList<Employee>();
            while (resultSet.next()) {
                Employee employee = new Employee();
                employee.employee_no = resultSet.getInt("employees.emp_no");
                employee.first_name = resultSet.getString("employees.first_name");
                employee.last_name = resultSet.getString("employees.last_name");
                employee.salary = resultSet.getInt("salaries.salary");
                employees.add(employee);
            }
            return employees;
        } catch (Exception e) {
            System.out.println(e.getMessage());
            System.out.println("Failed to get salary details");
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

    /**
     * Prints a list of employees.
     * @param employees The list of employees to print.
     */
    public void printSalaries(ArrayList<Employee> employees) {
        // Print header
        System.out.printf("%-10s %-15s %-20s %-8s%n", "Employee No", "First Name", "Last Name", "Salary");
        // Loop over all employees in the list
        for (Employee employee : employees) {
            String employee_data =
                    String.format("%-10s %-15s %-20s %-8s",
                            employee.employee_no, employee.first_name, employee.last_name, employee.salary);
            System.out.println(employee_data);
        }
    }
}
