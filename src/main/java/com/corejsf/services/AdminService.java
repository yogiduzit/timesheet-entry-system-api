package com.corejsf.services;

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
import com.corejsf.services.security.Role;
import com.corejsf.services.security.annotations.Secured;

@Secured({ Role.ADMIN })
@Path("/admins")
@Stateless
@Local
public class AdminService {

    @Inject
    AdminManager adminManager;

    @Secured({ Role.ADMIN })
    @GET
    @Produces("application/json")
    // Finds the admin user
    public Employee find() {
        Employee admin;
        try {
            admin = adminManager.find();
        } catch (final SQLException e) {
            e.printStackTrace();
            throw new WebApplicationException(e, Response.Status.INTERNAL_SERVER_ERROR);
        }
        if (admin == null) {
            throw new WebApplicationException("An admin user does not exist", Response.Status.NOT_FOUND);
        }
        return admin;
    }

}
