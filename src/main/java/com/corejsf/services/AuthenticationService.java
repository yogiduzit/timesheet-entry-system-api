package com.corejsf.services;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.corejsf.access.CredentialsManager;
import com.corejsf.model.employee.Credentials;
import com.sun.mail.iap.Response;

@Path("/authentication")
public class AuthenticationService {

    @Inject
    private CredentialsManager credManager;

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response authenticateUser(Credentials credentials) {

    }
}
