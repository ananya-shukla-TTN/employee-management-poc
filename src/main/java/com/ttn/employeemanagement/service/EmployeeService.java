package com.ttn.employeemanagement.service;

import com.ttn.employeemanagement.controller.dto.EmployeeOnboardRequest;
import com.ttn.employeemanagement.entity.Department;
import com.ttn.employeemanagement.entity.Employee;
import com.ttn.employeemanagement.exception.DuplicateEmailException;
import com.ttn.employeemanagement.exception.EmployeeNotFoundException;
import com.ttn.employeemanagement.repository.EmployeeRepository;

import java.math.BigDecimal;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class EmployeeService {

    private final EmployeeRepository employeeRepository;
    private final DepartmentService departmentService;

    @Transactional
    public Employee onboard(EmployeeOnboardRequest request) {
        String normalizedEmail = request.getEmail().trim().toLowerCase();
        employeeRepository
                .findByEmail(normalizedEmail)
                .ifPresent(e -> {
                    throw new DuplicateEmailException(normalizedEmail);
                });
        Department department = departmentService.requireActiveDepartmentForOnboarding(request.getDepartmentId());
        Employee employee = Employee.builder()
                .name(request.getName().trim())
                .email(normalizedEmail)
                .salary(request.getSalary())
                .department(department)
                .jobTitle(request.getJobTitle() != null ? request.getJobTitle().trim() : null)
                .hireDate(request.getHireDate())
                .active(true)
                .build();
        return employeeRepository.save(employee);
    }

    @Transactional(readOnly = true)
    public Page<Employee> findAll(Pageable pageable) {
        return employeeRepository.findAll(pageable);
    }

    @Transactional(readOnly = true)
    public Employee findById(Long id) {
        return employeeRepository.findById(id)
                .orElseThrow(() -> new EmployeeNotFoundException("Employee not found with id: " + id));
    }

    @Transactional
    public void deleteById(Long id) {
        if (!employeeRepository.existsById(id)) {
            throw new EmployeeNotFoundException("Employee not found with id: " + id);
        }
        employeeRepository.deleteById(id);
    }

    public BigDecimal parseSalary(String rawSalary) {
        try {
            if (rawSalary == null) {
                throw new NullPointerException("Salary text cannot be null");
            }
            BigDecimal value;
            try {
                value = new BigDecimal(rawSalary.trim());
            } catch (NumberFormatException ex) {
                throw new IllegalArgumentException("Invalid salary number format", ex);
            }
            if (value.compareTo(BigDecimal.ZERO) < 0) {
                throw new IllegalArgumentException("Salary cannot be negative");
            }
            return value;
        } catch (IllegalArgumentException ex) {
            log.warn("IllegalArgument while parsing salary: {}", ex.getMessage());
            throw ex;
        } catch (NullPointerException ex) {
            log.warn("NullPointer while parsing salary: {}", ex.getMessage());
            throw ex;
        } finally {
            log.info("Salary parsing operation completed");
        }
    }

    @Transactional
    public void transferEmployeeToDepartment(Long employeeId, Long newDepartmentId) {
        Employee employee = employeeRepository.findById(employeeId)
                .orElseThrow(() -> new EmployeeNotFoundException("Employee not found with id: " + employeeId));
        Department newDept = departmentService.findEntityById(newDepartmentId);
        employee.setDepartment(newDept);
        employeeRepository.save(employee);
    }

    @Transactional(readOnly = true)
    public List<Employee> findHighSalary(BigDecimal threshold) {
        return employeeRepository.findHighSalaryEmployees(threshold);
    }

    @Transactional(readOnly = true)
    public List<Employee> findByDepartmentName(String departmentName) {
        return employeeRepository.findEmployeesByDepartmentName(departmentName);
    }

    @Transactional(readOnly = true)
    public List<Employee> findEmployeesByDepartmentCode(String code) {
        return employeeRepository.findEmployeesByDepartmentCode(code);
    }
}
