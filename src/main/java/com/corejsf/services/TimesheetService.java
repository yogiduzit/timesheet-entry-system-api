package com.corejsf.services;

import java.net.URI;
import java.sql.SQLException;
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
import com.corejsf.model.employee.Employee;
import com.corejsf.model.timesheet.Timesheet;
import com.corejsf.services.security.Role;
import com.corejsf.services.security.annotations.AuthenticatedEmployee;
import com.corejsf.services.security.annotations.Secured;

@Path("/timesheets")
public class TimesheetService {

    @Inject
    TimesheetManager timesheetManager;

    @Inject
    @AuthenticatedEmployee
    private Employee authEmployee;

    @Secured({ Role.ADMIN, Role.EMPLOYEE })
    @GET
    @Produces("application/json")
    public List<Timesheet> getTimesheets() {
        List<Timesheet> timesheets = null;
        try {
            if (authEmployee.getRole() == Role.ADMIN) {
                timesheets = timesheetManager.getTimesheets();
            } else if (authEmployee.getRole() == Role.EMPLOYEE) {
                timesheets = timesheetManager.getTimesheets(authEmployee.getEmpNumber());
            }
        } catch (final SQLException e) {
            e.printStackTrace();
            throw new WebApplicationException(Response.Status.INTERNAL_SERVER_ERROR);
        }
        if (timesheets == null) {
            throw new WebApplicationException(Response.Status.NOT_FOUND);
        }
        return timesheets;
    }

    @Secured({ Role.ADMIN, Role.EMPLOYEE })
    @Path("/{id}")
    @GET
    @Produces("application/json")
    public Timesheet find(@PathParam("id") Integer id) {
        if (authEmployee.getEmpNumber() != id) {
            throw new WebApplicationException(Response.Status.FORBIDDEN);
        }
        Timesheet timesheet = null;
        try {
            timesheet = timesheetManager.find(id);
        } catch (final SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            throw new WebApplicationException(Response.Status.INTERNAL_SERVER_ERROR);
        }
        if (timesheet == null) {
            throw new WebApplicationException(Response.Status.NOT_FOUND);
        }
        return timesheet;
    }

    @Secured({ Role.ADMIN, Role.EMPLOYEE })
    @POST
    @Consumes("application/json")
    public Response insert(Timesheet timesheet) {
        try {
            timesheetManager.insert(timesheet);
        } catch (final SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return Response.serverError().build();
        }
        return Response.created(URI.create("/timesheets/" + timesheet.getId())).build();
    }

    @Secured({ Role.ADMIN, Role.EMPLOYEE })
    @Path("/{id}")
    @PATCH
    @Consumes("application/json")
    public Response merge(Timesheet timesheet, @PathParam("id") Integer timesheetId) {
        if (authEmployee.getRole() == Role.EMPLOYEE
                && timesheet.getEmployee().getEmpNumber() != authEmployee.getEmpNumber()) {
            return Response.status(Response.Status.UNAUTHORIZED).build();
        }
        try {
            timesheetManager.merge(timesheet, timesheetId);
        } catch (final SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return Response.serverError().build();
        }
        return Response.noContent().build();
    }

}
