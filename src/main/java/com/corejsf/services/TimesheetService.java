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
import com.corejsf.model.timesheet.Timesheet;

@Path("/timesheets")
public class TimesheetService {
    
    @Inject TimesheetManager timesheetManager;
    
    @GET
    @Produces("application/json")
    public List<Timesheet> getTimesheets() throws SQLException {
        final List<Timesheet> timesheets = timesheetManager.getTimesheets();
        if (timesheets == null) {
            throw new WebApplicationException(Response.Status.NOT_FOUND);
        }
        return timesheets;
    }
    
    @Path("/{id}")
    @GET
    @Produces("application/json")
    public List<Timesheet> getTimesheets(@PathParam("id") Integer empNo) throws SQLException {
        final List<Timesheet> timesheets = timesheetManager.getTimesheets(empNo);
        if (timesheets == null) {
            throw new WebApplicationException(Response.Status.NOT_FOUND);
        }
        return timesheets;
    }
    
    @POST
    @Consumes("application/json")
    public Response insert(Timesheet timesheet) throws SQLException {
        timesheetManager.insert(timesheet);
        return Response.created(URI.create("/timesheets/" + timesheet.getId())).build();
    }
    
    @Path("/{id}")
    @PATCH
    @Consumes("application/json")
    public Response merge(Timesheet timesheet, @PathParam("id") Integer timesheetId) throws SQLException {        
        timesheetManager.merge(timesheet, timesheetId);
        return Response.noContent().build();
    }
    
    

}
