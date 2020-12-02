/**
 *
 */
package com.corejsf.access;

import java.io.Serializable;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;

import javax.annotation.Resource;
import javax.enterprise.context.ConversationScoped;
import javax.inject.Named;
import javax.sql.DataSource;

import org.apache.commons.codec.binary.Hex;

import com.corejsf.helpers.PasswordHelper;
import com.corejsf.model.employee.Credentials;

/**
 * This is the class called CredentialsManager
 *
 * @author Yogesh Verma and Sung Na
 * @version 1.0
 *
 */
@Named("credentialsManager")
@ConversationScoped
public class CredentialsManager implements Serializable {

    private static String TAG = "Credential";
    private PasswordHelper passwordHelper;

    /**
     * Variable for implementing Serialiable
     */
    private static final long serialVersionUID = -6478292740340769939L;

    /**
     * Datasource for a project
     */
    @Resource(mappedName = "java:jboss/datasources/MySQLDS")
    private DataSource dataSource;

    public CredentialsManager() {
        try {
            passwordHelper = new PasswordHelper();
        } catch (final NoSuchAlgorithmException e) {
            passwordHelper = null;
            e.printStackTrace();
        }
    }

    /**
     * Method to get the credentials by employee number
     *
     * @param empNumber, number of the employee whose credentials need to be found
     * @return credentials, username and password of the employee
     * @return null, if the emp does not exist
     * @throws SQLException
     */
    public Credentials findByToken(String token) throws SQLException {
        Connection connection = null;
        PreparedStatement stmt = null;
        try {
            try {
                connection = dataSource.getConnection();
                try {
                    stmt = connection.prepareStatement("SELECT * FROM Credentials WHERE EmpToken = ?");
                    stmt.setBytes(1, token.getBytes());
                    final ResultSet result = stmt.executeQuery();
                    if (result.next()) {
                        final String password = Hex.encodeHexString(result.getBytes("EmpToken"));
                        final Credentials credentials = new Credentials(result.getString("EmpUserName"), password);
                        credentials.setEmpNumber(result.getInt("EmpNo"));
                        return credentials;
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
        return null;
    }

    public Credentials find(String empUserName) throws SQLException {
        Connection connection = null;
        PreparedStatement stmt = null;
        try {
            try {
                connection = dataSource.getConnection();
                try {
                    stmt = connection.prepareStatement("SELECT * FROM Credentials WHERE EmpUserName = ?");
                    stmt.setString(1, empUserName);
                    final ResultSet result = stmt.executeQuery();
                    if (result.next()) {
                        final String password = Hex.encodeHexString(result.getBytes("EmpToken"));
                        final Credentials credentials = new Credentials(result.getString("EmpUserName"), password);
                        credentials.setEmpNumber(result.getInt("EmpNo"));
                        return credentials;
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
        return null;
    }

    /**
     * Adds a Credentials record to the Credentials table in the datasource
     *
     * @param credentials, credentials object containing username and password
     * @throws SQLException
     */
    public void insert(Credentials credentials) throws SQLException {
        final int EmpNo = 1;
        final int EmpUserName = 2;
        final int EmpToken = 3;
        Connection connection = null;
        PreparedStatement stmt = null;
        try {
            try {
                connection = dataSource.getConnection();
                try {
                    stmt = connection.prepareStatement("INSERT INTO Credentials VALUES(?, ?, ?)");
                    stmt.setInt(EmpNo, credentials.getEmpNumber());
                    stmt.setString(EmpUserName, credentials.getUsername());
                    final byte[] token = passwordHelper.encrypt(credentials.getUsername() + credentials.getPassword());
                    stmt.setBytes(EmpToken, token);
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
     * Updates an existing Credentials record in the datasource
     *
     * @param credentials, credentials object containing username and password
     * @throws SQLException
     */
    public void merge(Credentials credentials, int id) throws SQLException {
        final int EmpUserName = 1;
        final int EmpToken = 2;
        final int EmpNo = 3;

        Connection connection = null;
        PreparedStatement stmt = null;
        try {
            try {
                connection = dataSource.getConnection();
                try {
                    stmt = connection.prepareStatement(
                            "UPDATE Credentials " + "SET EmpUserName=?, EmpPassword=? " + "WHERE EmpNo = ?");
                    stmt.setInt(EmpNo, id);
                    stmt.setString(EmpUserName, credentials.getUsername());
                    final byte[] token = passwordHelper.encrypt(credentials.getUsername() + credentials.getPassword());
                    stmt.setBytes(EmpToken, token);
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

}
