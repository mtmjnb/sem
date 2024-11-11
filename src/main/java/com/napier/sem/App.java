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

        if (args.length < 1) {
            app.connect("localhost:33060", 10000);
        } else {
            app.connect(args[0], Integer.parseInt(args[1]));
        }

        /*ArrayList<Employee> employees = app.getSalariesByDepartment(app.getDepartment("Sales"));
        app.printSalaries(employees);*/
        Employee employee = app.getBasicEmployee(500000);
        app.displayEmployee(employee);

        // Disconnect from database
        app.disconnect();
    }

    /**
     * Connect to the MySQL database.
     */
    public void connect(String location, int delay) {
        try {
            // Load Database driver
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            System.out.println("Could not load SQL driver");
            System.exit(-1);
        }

        int retries = 10;
        boolean shouldWait = false;
        for (int i = 0; i < retries; ++i) {
            System.out.println("Connecting to database...");
            try {
                if (shouldWait) {
                    // Wait a bit for db to start
                    Thread.sleep(delay);
                }

                // Connect to database
                connection = DriverManager.getConnection("jdbc:mysql://" + location +
                        "/employees?allowPublicKeyRetrieval=true&useSSL=false", "root", "example");
                System.out.println("Successfully connected");
                break;
            } catch (SQLException sqle) {
                System.out.println("Failed to connect to database attempt " + Integer.toString(i));
                System.out.println(sqle.getMessage());

                // Let's wait before attempting to reconnect
                shouldWait = true;
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
                employee.department_name = getDepartment(resultSet.getString("dept_name"));
                employee.manager = employee.department_name.manager;
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

    /**
     * Gets the current employees and salaries with a given role.
     * @return A list of employees and salaries, or null if there is an error.
     */
    public ArrayList<Employee> getSalariesByRole(String title) {
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
                                + "AND titles.title = '" + title + "' "
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

    public Department getDepartment(String department_name) {
        try {
            // Create an SQL statement
            Statement statement = connection.createStatement();
            // Create string for SQL statement
            String select = "SELECT dept_no, dept_name "
                                + "FROM departments "
                                + "WHERE dept_name = '" + department_name + "'";
            // Execute SQL statement
            ResultSet resultSet = statement.executeQuery(select);
            // Return new employee if valid.
            // Check one is returned
            if (resultSet.next()) {
                Department department = new Department();
                department.department_no = resultSet.getString("dept_no");
                department.department_name = resultSet.getString("dept_name");
                return department;
            } else {
                return null;
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
            System.out.println("Failed to get department details");
            return null;
        }
    }

    public ArrayList<Employee> getSalariesByDepartment(Department department) {
        try {
            // Create an SQL statement
            Statement statement = connection.createStatement();
            // Create string for SQL statement
            String select = "SELECT employees.emp_no, employees.first_name, employees.last_name, salaries.salary "
                    + "FROM employees, salaries, dept_emp, departments "
                    + "WHERE employees.emp_no = salaries.emp_no "
                    + "AND employees.emp_no = dept_emp.emp_no "
                    + "AND dept_emp.dept_no = departments.dept_no "
                    + "AND salaries.to_date = '9999-01-01' "
                    + "AND departments.dept_no = '" + department.department_no + "' "
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
        // Check employees is not null
        if (employees == null) {
            System.out.println("No employees");
            return;
        }
        // Print header
        System.out.printf("%-10s %-15s %-20s %-8s%n", "Employee No", "First Name", "Last Name", "Salary");
        // Loop over all employees in the list
        for (Employee employee : employees) {
            if (employee == null) {
                continue;
            }
            String employee_data =
                    String.format("%-10s %-15s %-20s %-8s",
                            employee.employee_no, employee.first_name, employee.last_name, employee.salary);
            System.out.println(employee_data);
        }
    }

    public void addEmployee(Employee employee) {
        try {
            Statement statement = connection.createStatement();
            String update =
                              "INSERT INTO employees (emp_no, first_name, last_name, birth_date, gender, hire_date) "
                            + "VALUES (" + employee.employee_no + ", '" + employee.first_name + "', '"
                            + employee.last_name + "', '9999-01-01', 'M', '9999-01-01')";
            statement.execute(update);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            System.out.println("Failed to add employee");
        }
    }

    public Employee getBasicEmployee(int ID) {
        try {
            // Create an SQL statement
            Statement statement = connection.createStatement();
            // Create string for SQL statement
            String select = "SELECT emp_no, first_name, last_name "
                    + "FROM employees "
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
}
