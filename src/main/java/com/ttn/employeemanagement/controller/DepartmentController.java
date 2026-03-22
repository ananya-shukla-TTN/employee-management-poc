package com.ttn.employeemanagement.controller;

import com.ttn.employeemanagement.controller.dto.DepartmentRegisterRequest;
import com.ttn.employeemanagement.entity.Department;
import com.ttn.employeemanagement.service.DepartmentService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/departments")
@RequiredArgsConstructor
@Validated
public class DepartmentController {

    private final DepartmentService departmentService;

    @PostMapping("/register")
    public ResponseEntity<String> create(@Valid @RequestBody DepartmentRegisterRequest request) {
        departmentService.registerDepartment(request);
        return new ResponseEntity<>("Department added successfully", HttpStatus.CREATED);
    }

    @GetMapping
    public List<Department> list() {
        return departmentService.findAll();
    }

    @GetMapping("/active-with-employees")
    public List<Department> activeWithEmployees(@RequestParam @Min(1) long minActiveEmployees) {
        return departmentService.findDepartmentsStaffedWithAtLeast(minActiveEmployees);
    }

    @GetMapping("/inactive-with-employees")
    public List<Department> inactiveWithEmployees() {
        return departmentService.findInactiveDepartmentsStillHavingEmployees();
    }

    @GetMapping("/code/{code}")
    public Department getByCode(@PathVariable String code) {
        return departmentService.getByCode(code);
    }

    @GetMapping("/{id}")
    public Department getDepartmentByID(@PathVariable Long id) {
        return departmentService.findEntityById(id);
    }

    @GetMapping("/{departmentId}/average-salary")
    public BigDecimal averageSalary(@PathVariable Long departmentId) {
        return departmentService.averageSalaryForDepartment(departmentId);
    }
}
