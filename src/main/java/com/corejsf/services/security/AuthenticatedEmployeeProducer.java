package com.corejsf.services.security;

import java.sql.SQLException;

import javax.enterprise.context.RequestScoped;
import javax.enterprise.event.Observes;
import javax.enterprise.inject.Produces;
import javax.inject.Inject;

import com.corejsf.access.EmployeeManager;
import com.corejsf.model.employee.Employee;
import com.corejsf.services.security.annotations.AuthenticatedEmployee;

@RequestScoped
public class AuthenticatedEmployeeProducer {

    @Inject
    private EmployeeManager empManager;

    @Produces
    @RequestScoped
    @AuthenticatedEmployee
    private Employee authenticatedEmployee;

    public void handleAuthenticationEvent(@Observes @AuthenticatedEmployee String username) {
        try {
            authenticatedEmployee = empManager.find(username);
        } catch (final SQLException e) {
            e.printStackTrace();
        }
    }
}
