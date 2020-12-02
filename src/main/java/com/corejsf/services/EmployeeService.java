package com.corejsf.services;

import java.net.URI;
import java.sql.SQLException;

import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.PATCH;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;

import com.corejsf.access.EmployeeManager;
import com.corejsf.model.employee.Employee;

@Path("/employees")
public class EmployeeService {

    @Inject
    private EmployeeManager employeeManager;

    @Path("/{id}")
    @GET
    @Produces("application/json")
    public Employee find(@PathParam("id") Integer empId) {
        Employee employee;
        try {
            employee = employeeManager.find(empId);
        } catch (SQLException e) {
            e.printStackTrace();
            throw new WebApplicationException(Response.Status.INTERNAL_SERVER_ERROR);

        }
        if (employee == null) {
            throw new WebApplicationException(Response.Status.NOT_FOUND);
        }
        return employee;
    }

    @GET
    @Produces("application/json")
    public Employee[] getAll()  {
        Employee[] employees;
        try {
            employees = employeeManager.getAll();
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            throw new WebApplicationException(Response.Status.INTERNAL_SERVER_ERROR);
        }
        if (employees == null) {
            throw new WebApplicationException(Response.Status.NOT_FOUND);
        }
        return employees;
    }

    @POST
    @Consumes("application/json")
    public Response persist(Employee employee) {
        try {
            employeeManager.persist(employee);
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return Response.serverError().build();
        }
        return Response.created(URI.create("/employees/" + employee.getEmpNumber())).build();
    }

    @Path("/{id}")
    @PATCH
    @Consumes("application/json")
    public Response merge(Employee employee, @PathParam("id") Integer empId) {
        try {
            employeeManager.merge(employee, empId);
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return Response.serverError().build();
        }
        return Response.noContent().build();
    }

    @Path("/{id}")
    @DELETE
    public Response remove(Employee employee, @PathParam("id") Integer empId) {
        try {
            employeeManager.remove(employee, empId);
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return Response.serverError().build();
        }
        return Response.ok().build();
    }

}
