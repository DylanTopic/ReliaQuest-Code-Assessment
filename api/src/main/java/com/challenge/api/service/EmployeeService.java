package com.challenge.api.service;

import com.challenge.api.model.CreateEmployeeRequest;
import com.challenge.api.model.Employee;
import java.util.List;
import java.util.UUID;



public interface EmployeeService {

    /** Returns all employees. */
    List<Employee> getAllEmployees();

    /** Returns employee matching the given UUID, or 404 if not found. */
    Employee getEmployeeByUuid(UUID uuid);

    /** Creates and returns a new employee, or 409 if email already exists. */
    Employee createEmployee(CreateEmployeeRequest request);
}