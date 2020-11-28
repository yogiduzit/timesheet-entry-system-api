package com.corejsf.services;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.PATCH;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;

import com.corejsf.access.TimesheetRowManager;
import com.corejsf.model.timesheet.TimesheetRow;

@Path("/rows")
public class TimesheetRowService {
    
    @Inject TimesheetRowManager timesheetRowManager;
    
    @Path("/{id}")
    @GET
    @Produces("application/json")
    public ArrayList<TimesheetRow> getTimesheetRows(@PathParam("id") Integer timesheetId) throws SQLException {
        return timesheetRowManager.getTimesheetRows(timesheetId);
    }
    
    @Path("/{id}")
    @POST
    public void insert(@PathParam("id") Integer timesheetId, List<TimesheetRow> timesheetRows) throws SQLException {
        timesheetRowManager.create(timesheetId, timesheetRows);
    }
    
    @Path("/{id}")
    @PATCH
    public void update(@PathParam("id") Integer timesheetId, List<TimesheetRow> timesheetRows) throws SQLException {
        timesheetRowManager.update(timesheetId, timesheetRows);
    }
    

}
