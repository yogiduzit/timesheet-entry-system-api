package com.corejsf.services;

import java.sql.SQLException;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.PATCH;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;

import com.corejsf.access.CredentialsManager;
import com.corejsf.model.employee.Credentials;

@Path("/credentials")
public class CredentialService {

    @Inject CredentialsManager credentialsManager;
    
    @Path("/{id}")
    @GET
    @Produces("application/json")
    public Credentials find(@PathParam("id") Integer empId) throws SQLException {
        return credentialsManager.find(empId);
    }
    
    
    @POST
    public void insert(Credentials credentials) throws SQLException {
        credentialsManager.insert(credentials);
    }
    
    @Path("/{id}")
    @PATCH
    public void merge(Credentials credentials, @PathParam("id") Integer empId) throws SQLException {
        credentialsManager.merge(credentials, empId);
    }
    
}
