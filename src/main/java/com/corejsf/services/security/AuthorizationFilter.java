package com.corejsf.services.security;

import java.io.IOException;
import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.annotation.Priority;
import javax.inject.Inject;
import javax.security.auth.message.AuthException;
import javax.ws.rs.Priorities;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.container.ResourceInfo;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.Provider;

import com.corejsf.access.AdminManager;
import com.corejsf.model.employee.Employee;
import com.corejsf.services.security.annotations.AuthenticatedEmployee;
import com.corejsf.services.security.annotations.Secured;

@Secured
@Provider
@Priority(Priorities.AUTHORIZATION)
public class AuthorizationFilter implements ContainerRequestFilter {

    @Context
    private ResourceInfo resourceInfo;

    @Inject
    @AuthenticatedEmployee
    private Employee authEmployee;

    @Inject
    private AdminManager adminManager;

    @Override
    public void filter(ContainerRequestContext requestContext) throws IOException {

        // Get the resource class which matches with the requested URL
        // Extract the roles declared by it
        final Class<?> resourceClass = resourceInfo.getResourceClass();
        final List<Role> classRoles = extractRoles(resourceClass);

        // Get the resource method which matches with the requested URL
        // Extract the roles declared by it
        final Method resourceMethod = resourceInfo.getResourceMethod();
        final List<Role> methodRoles = extractRoles(resourceMethod);

        try {

            // Check if the user is allowed to execute the method
            // The method annotations override the class annotations
            if (methodRoles.isEmpty()) {
                checkPermissions(classRoles);
            } else {
                checkPermissions(methodRoles);
            }

        } catch (final Exception e) {
            requestContext.abortWith(Response.status(Response.Status.FORBIDDEN).build());
        }
    }

    // Extract the roles from the annotated element
    private List<Role> extractRoles(AnnotatedElement annotatedElement) {
        if (annotatedElement == null) {
            return new ArrayList<Role>();
        } else {
            final Secured secured = annotatedElement.getAnnotation(Secured.class);
            if (secured == null) {
                return new ArrayList<Role>();
            } else {
                final Role[] allowedRoles = secured.value();
                return Arrays.asList(allowedRoles);
            }
        }
    }

    private void checkPermissions(List<Role> allowedRoles) throws Exception {
        Role currentRole = authEmployee.getRole();
        if (authEmployee.getRole() == Role.EMPLOYEE) {
            final Employee admin = adminManager.find();
            if (admin != null && admin.getUsername() != null
                    && admin.getUsername().equals(authEmployee.getUsername())) {
                currentRole = Role.ADMIN;
                authEmployee.setRole(currentRole);
            }
        }
        if (allowedRoles.size() == 1 && allowedRoles.get(0) == Role.ADMIN) {
            if (authEmployee.getRole() != Role.ADMIN) {
                throw new AuthException("Cannot access this resource");
            }
        }
    }
}
