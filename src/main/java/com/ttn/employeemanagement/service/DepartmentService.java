package com.ttn.employeemanagement.service;

import com.ttn.employeemanagement.controller.dto.DepartmentRegisterRequest;
import com.ttn.employeemanagement.entity.Department;
import com.ttn.employeemanagement.exception.DepartmentInactiveException;
import com.ttn.employeemanagement.exception.DepartmentNotFoundException;
import com.ttn.employeemanagement.exception.DuplicateDepartmentCodeException;
import com.ttn.employeemanagement.repository.DepartmentRepository;
import com.ttn.employeemanagement.repository.EmployeeRepository;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class DepartmentService {

    private final DepartmentRepository departmentRepository;
    private final EmployeeRepository employeeRepository;

    @Transactional
    public Department registerDepartment(DepartmentRegisterRequest request) {
        String normalizedCode = request.getCode().trim().toUpperCase();
        if (departmentRepository.findByCode(normalizedCode).isPresent()) {
            throw new DuplicateDepartmentCodeException(normalizedCode);
        }
        boolean active = request.getActive() != null ? request.getActive() : true;
        Department department = Department.builder()
                .code(normalizedCode)
                .name(request.getName().trim())
                .location(request.getLocation() != null ? request.getLocation().trim() : null)
                .active(active)
                .build();
        return departmentRepository.save(department);
    }

    @Transactional(readOnly = true)
    public List<Department> findAll() {
        return departmentRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Department findEntityById(Long id) {
        return getDepartmentByIdOrThrow(id);
    }

    @Transactional(readOnly = true)
    public Department getByCode(String code) {
        return departmentRepository
                .findByCode(code.trim().toUpperCase())
                .orElseThrow(() -> new DepartmentNotFoundException("Department not found with code: " + code));
    }

    @Transactional(readOnly = true)
    public Department requireActiveDepartmentForOnboarding(Long departmentId) {
        Department department = getDepartmentByIdOrThrow(departmentId);
        if (!department.isActive()) {
            throw new DepartmentInactiveException("Department " + department.getName() + " (" + department.getCode() + ") is inactive");
        }
        return department;
    }

    @Transactional(readOnly = true)
    public List<Department> findDepartmentsStaffedWithAtLeast(long minActiveEmployees) {
        return departmentRepository.findDepartmentsWithAtLeastActiveEmployees(minActiveEmployees);
    }

    @Transactional(readOnly = true)
    public List<Department> findInactiveDepartmentsStillHavingEmployees() {
        return departmentRepository.findInactiveDepartmentsWithEmployees();
    }

    @Transactional(readOnly = true)
    public BigDecimal averageSalaryForDepartment(Long departmentId) {
        getDepartmentByIdOrThrow(departmentId);
        return employeeRepository.averageActiveSalaryByDepartmentId(departmentId);
    }

    private Department getDepartmentByIdOrThrow(Long id) {
        return departmentRepository.findById(id)
                .orElseThrow(() -> new DepartmentNotFoundException("Department not found with id: " + id));
    }
}
