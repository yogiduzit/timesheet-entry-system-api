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
    public List<Timesheet> getTimesheets() {
        List<Timesheet> timesheets;
        try {
            timesheets = timesheetManager.getTimesheets();
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            throw new WebApplicationException(Response.Status.INTERNAL_SERVER_ERROR);
        }
        if (timesheets == null) {
            throw new WebApplicationException(Response.Status.NOT_FOUND);
        }
        return timesheets;
    }
    
    @Path("/{id}")
    @GET
    @Produces("application/json")
    public List<Timesheet> getTimesheets(@PathParam("id") Integer empNo) {
        List<Timesheet> timesheets;
        try {
            timesheets = timesheetManager.getTimesheets(empNo);
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            throw new WebApplicationException(Response.Status.INTERNAL_SERVER_ERROR);
        }
        if (timesheets == null) {
            throw new WebApplicationException(Response.Status.NOT_FOUND);
        }
        return timesheets;
    }
    
    @POST
    @Consumes("application/json")
    public Response insert(Timesheet timesheet) {
        try {
            timesheetManager.insert(timesheet);
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return Response.serverError().build();
        }
        return Response.created(URI.create("/timesheets/" + timesheet.getId())).build();
    }
    
    @Path("/{id}")
    @PATCH
    @Consumes("application/json")
    public Response merge(Timesheet timesheet, @PathParam("id") Integer timesheetId) {        
        try {
            timesheetManager.merge(timesheet, timesheetId);
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return Response.serverError().build();
        }
        return Response.noContent().build();
    }
    
    

}
