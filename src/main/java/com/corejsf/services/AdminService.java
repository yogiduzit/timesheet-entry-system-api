package com.corejsf.services;

import java.sql.SQLDataException;
import java.sql.SQLException;

import javax.ejb.Local;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;

import com.corejsf.access.AdminManager;
import com.corejsf.model.employee.Employee;

@Path("/admins")
@Stateless
@Local
public class AdminService {
    
    @Inject AdminManager adminManager;
    
    @GET
    @Produces("application/json")
    public Employee find() {
        final Employee admin = adminManager.find();
        if (admin == null) {
            throw new WebApplicationException(Response.Status.NOT_FOUND);
        }
        return admin;
    }

}
