package com.corejsf.services;

import java.net.URI;
import java.sql.SQLException;
import java.time.LocalDate;
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
    /**
     * Provides access to the timesheet table
     */
    TimesheetManager timesheetManager;

    @Inject
    @AuthenticatedEmployee
    /**
     * The authenticated employee
     */
    private Employee authEmployee;

    @Secured({ Role.ADMIN, Role.EMPLOYEE })
    @GET
    @Produces("application/json")
    /**
     * Gets a list of all timesheets
     *
     * @return arraylist containing timesheets
     */
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
            throw new WebApplicationException(e, Response.Status.INTERNAL_SERVER_ERROR);
        }
        if (timesheets == null) {
            throw new WebApplicationException("Could not find timesheets", Response.Status.NOT_FOUND);
        }
        return timesheets;
    }

    @Secured({ Role.ADMIN, Role.EMPLOYEE })
    @Path("/{id}")
    @GET
    @Produces("application/json")
    /**
     * Gets a timesheet with the provided id
     *
     * @param id, the identifier of the timesheet
     * @return
     */
    public Timesheet find(@PathParam("id") Integer id) {
        Timesheet timesheet = null;
        try {
            timesheet = timesheetManager.find(id);
            if (authEmployee.getRole() == Role.EMPLOYEE
                    && timesheet.getEmployee().getEmpNumber() != authEmployee.getEmpNumber()) {
                throw new WebApplicationException("Cannot access this timesheet", Response.Status.UNAUTHORIZED);
            }
        } catch (final SQLException e) {
            e.printStackTrace();
            throw new WebApplicationException(e, Response.Status.INTERNAL_SERVER_ERROR);
        }
        if (timesheet == null) {
            throw new WebApplicationException("Could not find timesheet", Response.Status.NOT_FOUND);
        }
        return timesheet;
    }

    @Secured({ Role.ADMIN, Role.EMPLOYEE })
    @POST
    @Consumes("application/json")
    /**
     * Creates a new timesheet
     *
     * @param timesheet, the timesheet to be created
     * @return a Response
     */
    public Response insert(Timesheet timesheet) {
        final Response errorRes = checkErrors(timesheet);
        if (errorRes != null) {
            return errorRes;
        }
        try {
            timesheetManager.insert(timesheet);
        } catch (final SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return Response.serverError().entity(e).build();
        }
        return Response.created(URI.create("/timesheets/" + timesheet.getId())).build();
    }

    @Secured({ Role.ADMIN, Role.EMPLOYEE })
    @Path("/{id}")
    @PATCH
    @Consumes("application/json")
    /**
     * Updates an existing timesheet
     *
     * @param timesheet,   the updated timesheet
     * @param timesheetId, the timesheet to be updated
     * @return a Response
     */
    public Response merge(Timesheet timesheet, @PathParam("id") Integer timesheetId) {
        final Response errorRes = checkErrors(timesheet);
        if (errorRes != null) {
            return errorRes;
        }
        try {
            timesheetManager.merge(timesheet, timesheetId);
        } catch (final SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return Response.serverError().entity(e).build();
        }
        return Response.noContent().build();
    }

    /**
     * Checks the errors in a timesheet
     *
     * @param timesheet, the timesheet to be checked
     * @return a response
     */
    private Response checkErrors(Timesheet timesheet) {
        if (authEmployee.getRole() == Role.EMPLOYEE
                && timesheet.getEmployee().getEmpNumber() != authEmployee.getEmpNumber()) {
            return Response.status(Response.Status.UNAUTHORIZED).entity("Cannot access this timesheet").build();
        }
        if (timesheet.getEndWeek().isBefore(LocalDate.now())) {
            return Response.status(Response.Status.UNSUPPORTED_MEDIA_TYPE)
                    .entity("Cannot create a timesheet in the past").build();
        }
        return null;
    }

}
