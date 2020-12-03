package com.corejsf.services;

import java.net.URI;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.PATCH;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
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
    @Path("/{id}")
    @GET
    @Produces("application/json")
    public ArrayList<TimesheetRow> getTimesheetRows(@PathParam("id") Integer timesheetId) {
        ArrayList<TimesheetRow> timesheetRow;
        try {
            final Response errorRes = checkErrors(timesheetId);
            if (errorRes != null) {
                throw new WebApplicationException(errorRes.getStatus());
            }
            timesheetRow = timesheetRowManager.getTimesheetRows(timesheetId);
        } catch (final SQLException e) {
            e.printStackTrace();
            throw new WebApplicationException(Response.Status.INTERNAL_SERVER_ERROR);
        }
        if (timesheetRow == null) {
            throw new WebApplicationException(Response.Status.NOT_FOUND);
        }
        return timesheetRow;
    }

    @Secured({ Role.ADMIN, Role.EMPLOYEE })
    @Path("/{id}")
    @POST
    @Consumes("application/json")
    public Response insert(@PathParam("id") Integer timesheetId, List<TimesheetRow> timesheetRows) {
        try {
            final Response errorRes = checkErrors(timesheetId, timesheetRows);
            if (errorRes != null) {
                return errorRes;
            }
            timesheetRowManager.create(timesheetId, timesheetRows);
        } catch (final Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return Response.serverError().build();
        }
        return Response.created(URI.create("/rows/" + timesheetId)).build();
    }

    @Secured({ Role.ADMIN, Role.EMPLOYEE })
    @Path("/{id}")
    @PATCH
    @Consumes("application/json")
    public Response update(@PathParam("id") Integer timesheetId, List<TimesheetRow> timesheetRows) {
        try {
            final Response errorRes = checkErrors(timesheetId, timesheetRows);
            if (errorRes != null) {
                return errorRes;
            }
            timesheetRowManager.update(timesheetId, timesheetRows);
        } catch (final SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return Response.serverError().build();
        }
        return Response.noContent().build();
    }

    private Response checkErrors(int timesheetId, List<TimesheetRow> timesheetRows) throws SQLException {
        final Timesheet timesheet = timesheetManager.find(timesheetId);
        if (timesheet == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        } else if (timesheet.getEmployee().getEmpNumber() != authEmployee.getEmpNumber()) {
            return Response.status(Response.Status.UNAUTHORIZED).build();
        } else if (timesheet.getDetails().size() == Timesheet.DAYS_IN_WEEK
                || timesheetRows.size() >= Timesheet.DAYS_IN_WEEK) {
            throw new IllegalAccessError("Cannot add more rows to a timesheet with 7 rows");
        }
        return null;
    }

    private Response checkErrors(int timesheetId) throws SQLException {
        final Timesheet timesheet = timesheetManager.find(timesheetId);
        if (timesheet == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        } else if (timesheet.getEmployee().getEmpNumber() != authEmployee.getEmpNumber()) {
            return Response.status(Response.Status.UNAUTHORIZED).build();
        }
        return null;
    }

}
