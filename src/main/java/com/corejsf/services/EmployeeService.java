package com.corejsf.services;

import java.net.URI;
import java.sql.SQLException;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.PATCH;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;

import com.corejsf.access.EmployeeManager;
import com.corejsf.model.employee.Employee;
import com.corejsf.services.security.Role;
import com.corejsf.services.security.annotations.AuthenticatedEmployee;
import com.corejsf.services.security.annotations.Secured;

@Path("/employees")
public class EmployeeService {

    @Inject
    // Provides access to the employee table
    private EmployeeManager employeeManager;

    @Inject
    @AuthenticatedEmployee
    // Gets the authenticated employee
    private Employee authEmployee;

    @Secured({ Role.ADMIN, Role.EMPLOYEE })
    @GET
    @Path("/{id}")
    @Produces("application/json")
    // Finds an employee
    public Employee find(@PathParam("id") Integer empId) {
        if (authEmployee.getRole() == Role.EMPLOYEE && authEmployee.getEmpNumber() != empId) {
            throw new WebApplicationException(Response.Status.FORBIDDEN);
        }
        Employee employee;
        try {
            employee = employeeManager.find(empId);
        } catch (final SQLException e) {
            e.printStackTrace();
            throw new WebApplicationException(Response.Status.INTERNAL_SERVER_ERROR);
        }
        if (employee == null) {
            throw new WebApplicationException(Response.Status.NOT_FOUND);
        }
        return employee;
    }

    @Secured({ Role.ADMIN })
    @GET
    @Produces("application/json")
    // Gets a list of all employees
    public Employee[] getAll() {
        Employee[] employees;
        try {
            employees = employeeManager.getAll();
        } catch (final SQLException e) {
            e.printStackTrace();
            throw new WebApplicationException(Response.Status.INTERNAL_SERVER_ERROR);
        }
        if (employees == null) {
            throw new WebApplicationException(Response.Status.NOT_FOUND);
        }
        return employees;
    }

    @Secured({ Role.ADMIN })
    @POST
    @Consumes("application/json")
    // Inserts an employee in the database
    public Response persist(Employee employee) {
        try {
            employeeManager.persist(employee);
        } catch (final SQLException e) {
            e.printStackTrace();
            return Response.serverError().build();
        }
        return Response.created(URI.create("/employees/" + employee.getEmpNumber())).build();
    }

    @Secured({ Role.ADMIN, Role.EMPLOYEE })
    @Path("/{id}")
    @PATCH
    @Consumes("application/json")
    // Updates an existing employee
    public Response merge(Employee employee, @PathParam("id") Integer empId) {
        if (authEmployee.getRole() == Role.EMPLOYEE && authEmployee.getEmpNumber() != empId) {
            throw new WebApplicationException(Response.Status.FORBIDDEN);
        }
        try {
            employeeManager.merge(employee, empId);
        } catch (final SQLException e) {
            e.printStackTrace();
            return Response.serverError().build();
        }
        return Response.noContent().build();
    }

    @Secured({ Role.ADMIN })
    @Path("/{id}")
    @DELETE
    // Deletes an existing employee
    public Response remove(Employee employee, @PathParam("id") Integer empId) {
        try {
            employeeManager.remove(employee, empId);
        } catch (final SQLException e) {
            e.printStackTrace();
            return Response.serverError().build();
        }
        return Response.ok().build();
    }

}
