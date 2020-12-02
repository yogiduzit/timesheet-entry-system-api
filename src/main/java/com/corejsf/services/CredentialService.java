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

    @Inject
    CredentialsManager credentialsManager;

    @Path("/{username}")
    @GET
    @Produces("application/json")
    public Credentials find(@PathParam("username") String userName) {

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

    @POST
    @Consumes("application/json")
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

    @Path("/{id}")
    @PATCH
    @Consumes("application/json")
    public Response merge(Credentials credentials, @PathParam("id") Integer empId) {
        try {
            credentialsManager.merge(credentials, empId);
        } catch (final SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return Response.serverError().build();
        }
        return Response.noContent().build();
    }

}
