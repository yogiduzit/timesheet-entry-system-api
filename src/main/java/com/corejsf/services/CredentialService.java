package com.corejsf.services;

import java.net.URI;
import java.sql.SQLException;

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

import com.corejsf.access.CredentialsManager;
import com.corejsf.model.employee.Credentials;
import com.corejsf.model.employee.Employee;
import com.corejsf.services.security.Role;
import com.corejsf.services.security.annotations.AuthenticatedEmployee;
import com.corejsf.services.security.annotations.Secured;

@Path("/credentials")
public class CredentialService {

    @Inject
    CredentialsManager credentialsManager;

    @Inject
    @AuthenticatedEmployee
    private Employee authEmployee;

    @Secured({ Role.ADMIN, Role.EMPLOYEE })
    @Path("/{username}")
    @GET
    @Produces("application/json")
    // Gets the credentials for a user
    public Credentials find(@PathParam("username") String userName) {
        final Response errorRes = checkErrors(authEmployee, userName);
        if (errorRes != null) {
            throw new WebApplicationException(errorRes.getStatus());
        }
        Credentials cred;
        try {
            cred = credentialsManager.find(userName);
        } catch (final SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            throw new WebApplicationException(Response.Status.INTERNAL_SERVER_ERROR);
        }
        if (cred == null) {
            throw new WebApplicationException(Response.Status.NOT_FOUND);
        }
        return cred;
    }

    @Secured({ Role.ADMIN })
    @POST
    @Consumes("application/json")
    // Inserts a credentials for a user in the db
    public Response insert(Credentials credentials) {
        try {
            credentialsManager.insert(credentials);
        } catch (final SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return Response.serverError().build();
        }
        return Response.created(URI.create("/credentials/" + credentials.getEmpNumber())).build();
    }

    @Secured({ Role.ADMIN, Role.EMPLOYEE })
    @Path("/{id}")
    @PATCH
    @Consumes("application/json")
    // Updates an existing credential
    public Response merge(Credentials credentials, @PathParam("id") Integer empId) {
        final Response errorRes = checkErrors(authEmployee, empId);
        if (errorRes != null) {
            return errorRes;
        }
        try {
            credentialsManager.merge(credentials, empId);
        } catch (final SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return Response.serverError().build();
        }
        return Response.noContent().build();
    }

    // Checks if credentials belong to current employee
    private static Response checkErrors(Employee authEmployee, int empId) {
        if (authEmployee.getRole() == Role.EMPLOYEE && !(authEmployee.getEmpNumber() == empId)) {
            return Response.status(Response.Status.UNAUTHORIZED).build();
        }
        return null;
    }

    // Checks if credentials belong to current employee
    private static Response checkErrors(Employee authEmployee, String username) {
        if (authEmployee.getRole() == Role.EMPLOYEE && !(authEmployee.getUsername().equals(username))) {
            return Response.status(Response.Status.UNAUTHORIZED).build();
        }
        return null;
    }

}
