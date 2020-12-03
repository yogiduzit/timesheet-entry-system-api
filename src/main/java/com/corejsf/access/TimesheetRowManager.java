package com.corejsf.access;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;
import javax.enterprise.context.ConversationScoped;
import javax.inject.Named;
import javax.sql.DataSource;

import com.corejsf.model.timesheet.TimesheetRow;

@Named("rowManager")
@ConversationScoped
/**
 *
 * Provides access to the TimesheetRows table
 *
 * @author Sung Na and Yogesh Verma
 *
 */
public class TimesheetRowManager implements Serializable {

    /**
     * Variable for the serializable.
     */
    private static final long serialVersionUID = -1786252399378663291L;
    private static String TAG = TimesheetRowManager.class.getSimpleName();

    /**
     * Datasource for a project
     */
    @Resource(mappedName = "java:jboss/datasources/MySQLDS")
    private DataSource dataSource;

    /**
     * Gets all timesheet rows associated with a timesheet
     *
     * @param timesheetId, the timesheet to get rows from
     * @throws SQLException
     */
    public ArrayList<TimesheetRow> getTimesheetRows(Integer timesheetId) throws SQLException {
        final ArrayList<TimesheetRow> timesheetRows = new ArrayList<>();
        Connection connection = null;
        PreparedStatement stmt = null;
        try {
            try {
                connection = dataSource.getConnection();
                try {
                    stmt = connection
                            .prepareStatement("SELECT * FROM TimesheetRows WHERE TimesheetId = ? ORDER BY TimesheetID");
                    stmt.setInt(1, timesheetId);
                    final ResultSet result = stmt.executeQuery();
                    while (result.next()) {
                        final String hours = result.getString("HoursForWeek");

                        final BigDecimal[] weeklyHours = convertToWeeklyHours(hours);
                        final TimesheetRow row = new TimesheetRow(result.getInt("ProjectId"),
                                result.getString("WorkPackage"), weeklyHours, result.getString("Notes"));
                        row.setId(result.getInt(1));
                        timesheetRows.add(row);
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
        return timesheetRows;
    }

    /**
     * Adds timesheetRows to a timesheet
     *
     * @param timesheetId,   the timesheet to add rows to
     * @param timesheetRows, the rows to be added
     * @throws SQLException
     */
    public void create(Integer timesheetId, List<TimesheetRow> timesheetRows) throws SQLException {
        final int TimesheetID = 1;
        final int ProjectID = 2;
        final int WorkPackage = 3;
        final int Notes = 4;
        final int HoursForWeek = 5;

        Connection connection = null;
        PreparedStatement stmt = null;
        try {
            try {
                connection = dataSource.getConnection();
                try {
                    stmt = connection.prepareStatement(
                            "INSERT INTO TimesheetRows(TimesheetID, ProjectID, WorkPackage, Notes, HoursForWeek) VALUES(?, ?, ?, ?, ?)",
                            Statement.RETURN_GENERATED_KEYS);
                    for (final TimesheetRow timesheetRow : timesheetRows) {
                        if (timesheetRow.getWorkPackage() == null || timesheetRow.getWorkPackage().isEmpty()) {
                            continue;
                        }
                        stmt.setInt(TimesheetID, timesheetId);
                        stmt.setInt(ProjectID, timesheetRow.getProjectID());
                        stmt.setString(WorkPackage, timesheetRow.getWorkPackage());
                        stmt.setString(Notes, timesheetRow.getNotes());
                        stmt.setString(HoursForWeek, stringifyWeeklyHours(timesheetRow.getHoursForWeek()));
                        stmt.addBatch();
                        stmt.clearParameters();
                    }
                    stmt.executeBatch();

                    final int counter = 0;
                    final ResultSet rs = stmt.getGeneratedKeys();
                    while (rs.next()) {
                        timesheetRows.get(counter).setId(rs.getInt(1));
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
     * Adds timesheetRows to a timesheet
     *
     * @param timesheetId,   the timesheet to add rows to
     * @param timesheetRows, the rows to be added
     * @throws SQLException
     */
    public void create(Integer timesheetId, TimesheetRow timesheetRow) throws SQLException {
        final int TimesheetID = 1;
        final int ProjectID = 2;
        final int WorkPackage = 3;
        final int Notes = 4;
        final int HoursForWeek = 5;

        Connection connection = null;
        PreparedStatement stmt = null;
        try {
            try {
                connection = dataSource.getConnection();
                try {
                    stmt = connection.prepareStatement(
                            "INSERT INTO TimesheetRows(TimesheetID, ProjectID, WorkPackage, Notes, HoursForWeek) VALUES(?, ?, ?, ?, ?)",
                            Statement.RETURN_GENERATED_KEYS);
                    if (timesheetRow.getWorkPackage() == null || timesheetRow.getWorkPackage().isEmpty()) {
                        throw new IllegalArgumentException("Please enter all values");
                    }
                    stmt.setInt(TimesheetID, timesheetId);
                    stmt.setInt(ProjectID, timesheetRow.getProjectID());
                    stmt.setString(WorkPackage, timesheetRow.getWorkPackage());
                    stmt.setString(Notes, timesheetRow.getNotes());
                    stmt.setString(HoursForWeek, stringifyWeeklyHours(timesheetRow.getHoursForWeek()));
                    stmt.executeUpdate();

                    final ResultSet rs = stmt.getGeneratedKeys();
                    if (rs.next()) {
                        timesheetRow.setId(rs.getInt(1));
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
        } catch (

        final SQLException ex) {
            ex.printStackTrace();
            throw ex;
        }
    }

    /**
     * Updating a timesheetRow present in a timesheet
     *
     * @param timesheetId,   the timesheet whose rows need to be updated
     * @param timesheetRows, the list containing the timesheet's rows
     * @throws SQLException
     */
    public void update(Integer timesheetId, List<TimesheetRow> timesheetRows) throws SQLException {
        final int HoursForWeek = 1;
        final int Notes = 2;
        final int ProjectID = 3;
        final int WorkPackage = 4;
        final int TimesheetRowID = 5;

        Connection connection = null;
        PreparedStatement stmt = null;
        try {
            try {
                connection = dataSource.getConnection();
                try {
                    stmt = connection.prepareStatement("UPDATE TimesheetRows "
                            + "SET HoursForWeek=?, Notes=?, ProjectID=?, WorkPackage=? " + "WHERE TimesheetRowID=?");
                    for (final TimesheetRow timesheetRow : timesheetRows) {
                        if (timesheetRow.getWorkPackage() == null || timesheetRow.getWorkPackage().isEmpty()) {
                            continue;
                        }
                        stmt.setInt(TimesheetRowID, timesheetRow.getId());
                        stmt.setInt(ProjectID, timesheetRow.getProjectID());
                        stmt.setString(WorkPackage, timesheetRow.getWorkPackage());
                        stmt.setString(Notes, timesheetRow.getNotes());
                        stmt.setString(HoursForWeek, stringifyWeeklyHours(timesheetRow.getHoursForWeek()));
                        stmt.addBatch();
                        stmt.clearParameters();
                    }
                    stmt.executeBatch();
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
            System.out.println("Error in find" + TAG);
            ex.printStackTrace();
            throw ex;
        }
    }

    /**
     * Updating a timesheetRow present in a timesheet
     *
     * @param timesheetId,   the timesheet whose rows need to be updated
     * @param timesheetRows, the list containing the timesheet's rows
     * @throws SQLException
     */
    public void update(Integer timesheetId, TimesheetRow timesheetRow) throws SQLException {
        final int HoursForWeek = 1;
        final int Notes = 2;
        final int ProjectID = 3;
        final int WorkPackage = 4;
        final int TimesheetRowID = 5;

        Connection connection = null;
        PreparedStatement stmt = null;
        try {
            try {
                connection = dataSource.getConnection();
                try {
                    stmt = connection.prepareStatement("UPDATE TimesheetRows "
                            + "SET HoursForWeek=?, Notes=?, ProjectID=?, WorkPackage=? " + "WHERE TimesheetRowID=?");
                    if (timesheetRow.getWorkPackage() == null || timesheetRow.getWorkPackage().isEmpty()) {
                        throw new IllegalArgumentException("Please enter all values");
                    }
                    stmt.setInt(TimesheetRowID, timesheetRow.getId());
                    stmt.setInt(ProjectID, timesheetRow.getProjectID());
                    stmt.setString(WorkPackage, timesheetRow.getWorkPackage());
                    stmt.setString(Notes, timesheetRow.getNotes());
                    stmt.setString(HoursForWeek, stringifyWeeklyHours(timesheetRow.getHoursForWeek()));
                    stmt.executeBatch();
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
            System.out.println("Error in find" + TAG);
            ex.printStackTrace();
            throw ex;
        }
    }

    /**
     * Converts a hours string to array
     *
     * @param hours, the string to be converted
     * @return array containing BigDecimals
     */
    private static BigDecimal[] convertToWeeklyHours(String hours) {
        final String[] temp = hours.split(",");
        final BigDecimal[] weeklyHours = new BigDecimal[temp.length];

        for (int i = 0; i < weeklyHours.length; i++) {
            if (!temp[i].equals("null")) {
                weeklyHours[i] = BigDecimal.valueOf(Long.valueOf(temp[i]));
            } else {
                weeklyHours[i] = BigDecimal.ZERO;
            }

        }
        return weeklyHours;
    }

    /**
     * Converts a BigDecimal array to a storable string
     *
     * @param hours, the array to be converted @return, the string to be stored
     */
    private static String stringifyWeeklyHours(BigDecimal[] hours) {
        String weeklyHours = "";
        for (int i = 0; i < hours.length; i++) {
            weeklyHours += (hours[i] + ((i != hours.length - 1) ? "," : ""));
        }
        return weeklyHours;
    }

}