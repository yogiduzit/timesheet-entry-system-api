package com.corejsf.services;

import java.net.URI;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.PATCH;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;

import com.corejsf.access.TimesheetManager;
import com.corejsf.access.TimesheetRowManager;
import com.corejsf.model.employee.Employee;
import com.corejsf.model.timesheet.Timesheet;
import com.corejsf.model.timesheet.TimesheetRow;
import com.corejsf.services.security.Role;
import com.corejsf.services.security.annotations.AuthenticatedEmployee;
import com.corejsf.services.security.annotations.Secured;

@Path("/rows")
public class TimesheetRowService {

    @Inject
    TimesheetRowManager timesheetRowManager;

    @Inject
    TimesheetManager timesheetManager;

    @Inject
    @AuthenticatedEmployee
    private Employee authEmployee;

    @Secured({ Role.ADMIN, Role.EMPLOYEE })
    @GET
    @Produces("application/json")
    /**
     * Gets all the rows of a timesheet
     *
     * @param timesheetId, the timesheetId to get rows from
     * @return list of timesheet rows
     */
    public ArrayList<TimesheetRow> getTimesheetRows(@QueryParam("timesheetId") Integer timesheetId) {
        ArrayList<TimesheetRow> timesheetRow;
        try {
            final Response errorRes = checkErrors(timesheetId);
            if (errorRes != null) {
                throw new WebApplicationException((String) errorRes.getEntity(), errorRes.getStatus());
            }
            timesheetRow = timesheetRowManager.getTimesheetRows(timesheetId);
        } catch (final SQLException e) {
            e.printStackTrace();
            throw new WebApplicationException(e, Response.Status.INTERNAL_SERVER_ERROR);
        }
        if (timesheetRow == null) {
            throw new WebApplicationException("Couldn't find timesheet row", Response.Status.NOT_FOUND);
        }
        return timesheetRow;
    }

    @Secured({ Role.ADMIN, Role.EMPLOYEE })
    @POST
    @Consumes("application/json")
    // Creates timesheet rows
    public Response insert(@QueryParam("timesheetId") Integer timesheetId, TimesheetRow timesheetRow) {
        try {
            final Timesheet timesheet = timesheetManager.find(timesheetId);
            if (timesheet == null) {
                return Response.status(Response.Status.NOT_FOUND).entity("Unable to find a timesheet to insert to").build();
            } else if (timesheet.getEmployee().getEmpNumber() != authEmployee.getEmpNumber()) {
                return Response.status(Response.Status.UNAUTHORIZED).entity("Cannot modify another's user's timesheet").build();
            } else if (timesheet.getDetails().size() == Timesheet.DAYS_IN_WEEK) {
                return Response.status(Response.Status.UNSUPPORTED_MEDIA_TYPE)
                        .entity("Cannot add more rows to a timesheet with 7 rows").build();
            }
            timesheetRowManager.create(timesheetId, timesheetRow);
        } catch (final Exception e) {
            e.printStackTrace();
            return Response.serverError().entity(e).build();
        }
        return Response.created(URI.create("/rows/" + timesheetId)).build();
    }

    @Secured({ Role.ADMIN, Role.EMPLOYEE })
    @PATCH
    @Consumes("application/json")
    // Updates a timesheet row
    public Response update(@QueryParam("timesheetId") Integer timesheetId, TimesheetRow timesheetRow) {
        try {
            final Response errorRes = checkErrors(timesheetId);
            if (errorRes != null) {
                return errorRes;
            }
            timesheetRowManager.update(timesheetId, timesheetRow);
        } catch (final SQLException e) {
            e.printStackTrace();
            return Response.serverError().entity(e).build();
        }
        return Response.noContent().build();
    }

    // Checks for errors in the timesheet row
    private Response checkErrors(int timesheetId) throws SQLException {
        final Timesheet timesheet = timesheetManager.find(timesheetId);
        if (timesheet == null) {
            return Response.status(Response.Status.NOT_FOUND).entity("Cannot find timesheet").build();
        } else if (timesheet.getEmployee().getEmpNumber() != authEmployee.getEmpNumber()) {
            return Response.status(Response.Status.UNAUTHORIZED).entity("Cannot access another user's timesheet")
                    .build();
        }
        return null;
    }

}
