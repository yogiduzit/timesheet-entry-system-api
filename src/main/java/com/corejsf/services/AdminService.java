package com.corejsf.services;

import java.sql.SQLDataException;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

import com.corejsf.access.AdminManager;
import com.corejsf.model.employee.Employee;

@Path("/admins")
public class AdminService {
    
    @Inject AdminManager adminManager;
    
    @GET
    @Produces("application/json")
    public Employee find() throws SQLDataException {
        return adminManager.find();
    }

}
