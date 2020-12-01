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

@Path("/credentials")
public class CredentialService {

    @Inject CredentialsManager credentialsManager;
    
    @Path("/{id}")
    @GET
    @Produces("application/json")
    public Credentials find(@PathParam("id") Integer empId) throws SQLException {
        final Credentials cred = credentialsManager.find(empId);
        if (cred == null) {
            throw new WebApplicationException(Response.Status.NOT_FOUND);
        }
        return cred;
    }
    
    
    @POST
    @Consumes("application/json")
    public Response insert(Credentials credentials) throws SQLException {
        credentialsManager.insert(credentials);
        return Response.created(URI.create("/credentials/" + credentials.getEmpNumber())).build();
    }
    
    @Path("/{id}")
    @PATCH
    @Consumes("application/json")
    public Response merge(Credentials credentials, @PathParam("id") Integer empId) throws SQLException {
        credentialsManager.merge(credentials, empId);
        return Response.noContent().build();
    }
    
}
