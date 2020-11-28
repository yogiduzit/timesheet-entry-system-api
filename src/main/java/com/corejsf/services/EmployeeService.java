package com.corejsf.services;

import java.sql.SQLException;

import javax.inject.Inject;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.PATCH;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;

import com.corejsf.access.EmployeeManager;
import com.corejsf.model.employee.Employee;

@Path("/employees")
public class EmployeeService {
    
    @Inject
    private EmployeeManager employeeManager;
    
    @Path("/{id}")
    @GET
    @Produces("application/json")
    public Employee find(@PathParam("id") Integer empId) throws SQLException {
        return employeeManager.find(empId);
    }
    
    @GET
    @Produces("application/json")
    public Employee[] getAll() throws SQLException {
        return employeeManager.getAll();
    }
    
    @POST
    public void persist(Employee employee) throws SQLException {
        employeeManager.persist(employee);
    }
    
    @Path("/{id}")
    @PATCH
    public void merge(Employee employee, @PathParam("id") Integer empId) throws SQLException {
        employeeManager.merge(employee, empId);
    }
    
    @Path("/{id}")
    @DELETE
    public void remove(Employee employee, @PathParam("id") Integer empId) throws SQLException {
        employeeManager.remove(employee, empId);
    }
    

}
