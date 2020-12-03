package com.corejsf.access;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.sql.Statement;
import java.util.ArrayList;

import javax.annotation.Resource;
import javax.enterprise.context.ConversationScoped;
import javax.inject.Named;
import javax.sql.DataSource;

import com.corejsf.model.employee.Employee;

@Named("employeeManager")
@ConversationScoped
public class EmployeeManager implements Serializable {

    private static final long serialVersionUID = 1L;
    private static String TAG = "Employee";

    /**
     * Datasource for the project
     */
    @Resource(mappedName = "java:jboss/datasources/MySQLDS")
    private DataSource dataSource;

    public EmployeeManager() {
    }

    /**
     * Finds an employee by their username
     *
     * @param username, unique identifier for the Employee
     * @return Employee POJO
     * @throws SQLException
     */
    public Employee find(String username) throws SQLException {
        Connection connection = null;
        PreparedStatement stmt = null;
        try {
            try {
                connection = dataSource.getConnection();
                try {
                    stmt = connection.prepareStatement("SELECT * FROM " + "Employees WHERE EmpUserName = ?");
                    stmt.setString(1, username);
                    final ResultSet result = stmt.executeQuery();
                    if (result.next()) {
                        return new Employee(result.getInt("EmpNo"), result.getString("EmpName"),
                                result.getString("EmpUserName"));
                    } else {
                        return null;
                    }
                } finally {
                    if (stmt != null) {
                        stmt.close();
                    }
                }
            } finally {
                if (connection != null) {
                    connection.close();
                }
            }
        } catch (final SQLException ex) {
            ex.printStackTrace();
            throw ex;
        }
    }

    /**
     * Finds an employee by their unique number
     *
     * @param empNo, the unique id of the employee
     * @return Employee POJO
     * @throws SQLException
     */
    public Employee find(int empNo) throws SQLException {
        Connection connection = null;
        PreparedStatement stmt = null;
        try {
            try {
                connection = dataSource.getConnection();
                try {
                    stmt = connection.prepareStatement("SELECT * FROM " + "Employees WHERE EmpNo = ?");
                    stmt.setInt(1, empNo);
                    final ResultSet result = stmt.executeQuery();
                    if (result.next()) {
                        return new Employee(result.getInt("EmpNo"), result.getString("EmpName"),
                                result.getString("EmpUserName"));
                    } else {
                        return null;
                    }
                } finally {
                    if (stmt != null) {
                        stmt.close();
                    }
                }
            } finally {
                if (connection != null) {
                    connection.close();
                }
            }
        } catch (final SQLException ex) {
            ex.printStackTrace();
            throw ex;
        }
    }

    /**
     * Inserts an Employee record into the employees table
     *
     * @param employee, POJO representing an employee record
     * @throws SQLException
     */
    public void persist(Employee employee) throws SQLException {
        final int empNo = 1;
        final int empName = 2;
        final int empUsername = 3;
        Connection connection = null;
        PreparedStatement stmt = null;
        try {
            try {
                connection = dataSource.getConnection();
                try {
                    stmt = connection.prepareStatement("INSERT INTO Employees " + "VALUES (?, ?, ?)");
                    stmt.setInt(empNo, employee.getEmpNumber());
                    stmt.setString(empName, employee.getFullName());
                    stmt.setString(empUsername, employee.getUsername());
                    stmt.executeUpdate();
                } finally {
                    if (stmt != null) {
                        stmt.close();
                    }
                }
            } finally {
                if (connection != null) {
                    connection.close();
                }
            }
        } catch (final SQLIntegrityConstraintViolationException ex) {
            ex.printStackTrace();
            throw ex;
        } catch (final SQLException ex) {
            ex.printStackTrace();
            throw ex;
        }
    }

    /**
     * Updates an existing employee record in the employees table
     *
     * @param employee, POJO representing the employee record
     * @throws SQLException
     */
    public void merge(Employee employee, Integer id) throws SQLException {
        final int empName = 1;
        final int empUsername = 2;
        final int empNo = 3;
        Connection connection = null;
        PreparedStatement stmt = null;
        try {
            try {
                connection = dataSource.getConnection();
                try {
                    stmt = connection.prepareStatement(
                            "UPDATE Employees " + "SET EmpName = ?, EmpUserName = ? " + "WHERE EmpNo = ?");
                    stmt.setString(empName, employee.getFullName());
                    stmt.setString(empUsername, employee.getUsername());
                    stmt.setInt(empNo, id);
                    stmt.executeUpdate();
                } finally {
                    if (stmt != null) {
                        stmt.close();
                    }
                }
            } finally {
                if (connection != null) {
                    connection.close();
                }
            }
        } catch (final SQLIntegrityConstraintViolationException ex) {
            ex.printStackTrace();
            throw ex;
        } catch (final SQLException ex) {
            ex.printStackTrace();
            throw ex;
        }
    }

    /**
     * Removes an employee record from the employees table in the datasource
     *
     * @param employee, Employee POJO
     * @throws SQLException
     */
    public void remove(Employee employee, Integer id) throws SQLException {
        Connection connection = null;
        PreparedStatement stmt = null;
        try {
            try {
                connection = dataSource.getConnection();
                try {
                    stmt = connection.prepareStatement("DELETE FROM Employees WHERE EmpNo = ?");
                    stmt.setInt(1, id);
                    stmt.executeUpdate();
                } finally {
                    if (stmt != null) {
                        stmt.close();
                    }
                }
            } finally {
                if (connection != null) {
                    connection.close();
                }
            }
        } catch (final SQLException ex) {
            ex.printStackTrace();
            throw ex;
        }
    }

    /**
     * Gets the list of all employee records in the table
     *
     * @return list of all employees
     * @throws SQLException
     */
    public Employee[] getAll() throws SQLException {
        final ArrayList<Employee> employees = new ArrayList<Employee>();
        Connection connection = null;
        Statement stmt = null;
        try {
            try {
                connection = dataSource.getConnection();
                try {
                    stmt = connection.createStatement();
                    final ResultSet result = stmt.executeQuery("SELECT * FROM Employees ORDER BY EmpNo");
                    while (result.next()) {
                        employees.add(new Employee(result.getInt("EmpNo"), result.getString("EmpName"),
                                result.getString("EmpUserName")));
                    }
                } finally {
                    if (stmt != null) {
                        stmt.close();
                    }
                }
            } finally {
                if (connection != null) {
                    connection.close();
                }
            }
        } catch (final SQLException ex) {
            ex.printStackTrace();
            throw ex;
        }

        final Employee[] subarray = new Employee[employees.size()];
        return employees.toArray(subarray);
    }
}
