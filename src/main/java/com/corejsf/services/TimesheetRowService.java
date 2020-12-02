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

import com.corejsf.access.TimesheetRowManager;
import com.corejsf.model.timesheet.TimesheetRow;

@Path("/rows")
public class TimesheetRowService {
    
    @Inject TimesheetRowManager timesheetRowManager;
    
    @Path("/{id}")
    @GET
    @Produces("application/json")
    public ArrayList<TimesheetRow> getTimesheetRows(@PathParam("id") Integer timesheetId) throws SQLException {
        final ArrayList<TimesheetRow> timesheetRow = timesheetRowManager.getTimesheetRows(timesheetId);
        if (timesheetRow == null) {
            throw new WebApplicationException(Response.Status.NOT_FOUND);
        }
        return timesheetRow;
    }
    
    @Path("/{id}")
    @POST
    @Consumes("application/json")
    public Response insert(@PathParam("id") Integer timesheetId, List<TimesheetRow> timesheetRows) throws SQLException {
        timesheetRowManager.create(timesheetId, timesheetRows);
        return Response.created(URI.create("/rows/" + timesheetId)).build();
    }
    
    @Path("/{id}")
    @PATCH
    @Consumes("application/json")
    public Response update(@PathParam("id") Integer timesheetId, List<TimesheetRow> timesheetRows) throws SQLException {
        timesheetRowManager.update(timesheetId, timesheetRows);
        return Response.noContent().build();
    }
    

}
