package com.challenge.api.service;

import com.challenge.api.model.CreateEmployeeRequest;
import com.challenge.api.model.Employee;
import com.challenge.api.model.EmployeeImpl;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;


/** In-memory mock implementation of {@link EmployeeService}. */
@Service
public class EmployeeServiceImpl implements EmployeeService {

    private final Map<UUID, Employee> employeeStore = new ConcurrentHashMap<>();

    public EmployeeServiceImpl() {
        seedMockData();
    }

    @Override
    public List<Employee> getAllEmployees() {
        return new ArrayList<>(employeeStore.values());
    }

    @Override
    public Employee getEmployeeByUuid(UUID uuid) {
        Employee employee = employeeStore.get(uuid);
        if (employee == null) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "Employee not found for UUID: " + uuid);
        }
        return employee;
    }

    @Override
    public Employee createEmployee(CreateEmployeeRequest request) {
        boolean emailTaken = employeeStore.values().stream()
                .anyMatch(e -> e.getEmail().equalsIgnoreCase(request.getEmail()));
        if (emailTaken) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT, "An employee with that email already exists.");
        }

        EmployeeImpl employee = new EmployeeImpl(
                UUID.randomUUID(),
                request.getFirstName(),
                request.getLastName(),
                request.getSalary(),
                request.getAge(),
                request.getJobTitle(),
                request.getEmail(),
                request.getContractHireDate());

        employeeStore.put(employee.getUuid(), employee);
        return employee;
    }

    // Helpers
    private void seedMockData() {
        List<EmployeeImpl> seeds = List.of(
                new EmployeeImpl(
                        UUID.fromString("11111111-1111-1111-1111-111111111111"),
                        "Alice",
                        "Nguyen",
                        95000,
                        30,
                        "Software Engineer",
                        "alice.nguyen@company.com",
                        Instant.parse("2021-03-15T09:00:00Z")),
                new EmployeeImpl(
                        UUID.fromString("22222222-2222-2222-2222-222222222222"),
                        "Bob",
                        "Patel",
                        110000,
                        35,
                        "Product Manager",
                        "bob.patel@company.com",
                        Instant.parse("2019-07-01T09:00:00Z")),
                new EmployeeImpl(
                        UUID.fromString("33333333-3333-3333-3333-333333333333"),
                        "Carol",
                        "Smith",
                        75000,
                        28,
                        "HR Specialist",
                        "carol.smith@company.com",
                        Instant.parse("2022-11-28T09:00:00Z")));

        seeds.forEach(e -> employeeStore.put(e.getUuid(), e));
    }
}