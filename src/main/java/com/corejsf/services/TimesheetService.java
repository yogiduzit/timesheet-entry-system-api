package com.corejsf.services;

import java.sql.SQLException;
import java.util.List;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.PATCH;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;

import com.corejsf.access.TimesheetManager;
import com.corejsf.model.timesheet.Timesheet;

@Path("/timesheets")
public class TimesheetService {
    
    @Inject TimesheetManager timesheetManager;
    
    @GET
    @Produces("application/json")
    public List<Timesheet> getTimesheets() throws SQLException {
        return timesheetManager.getTimesheets();
    }
    
    @Path("/{id}")
    @GET
    @Produces("application/json")
    public List<Timesheet> getTimesheets(@PathParam("id") Integer empNo) throws SQLException {
        return timesheetManager.getTimesheets(empNo);
    }
    
    @POST
    public int insert(Timesheet timesheet) throws SQLException {
        return timesheetManager.insert(timesheet);
    }
    
    @Path("/{id}")
    @PATCH
    public void merge(Timesheet timesheet, @PathParam("id") Integer timesheetId) throws SQLException {
        timesheetManager.merge(timesheet, timesheetId);
    }
    
    

}
