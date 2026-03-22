package com.ttn.employeemanagement.controller;

import com.ttn.employeemanagement.controller.dto.EmployeeOnboardRequest;
import com.ttn.employeemanagement.entity.Employee;
import com.ttn.employeemanagement.service.EmployeeService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/employees")
@RequiredArgsConstructor
@Validated
public class EmployeeController {

    private final EmployeeService employeeService;

    @PostMapping("/add")
    public ResponseEntity<String> create(@Valid @RequestBody EmployeeOnboardRequest request) {
        employeeService.onboard(request);
        return new ResponseEntity<>("Employee added successfully", HttpStatus.CREATED);
    }

    @GetMapping
    public Page<Employee> list(@PageableDefault(size = 10, sort = "name", direction = Sort.Direction.ASC) Pageable pageable) {
        return employeeService.findAll(pageable);
    }

    @GetMapping("/{id}")
    public Employee getById(@PathVariable Long id) {
        return employeeService.findById(id);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(@PathVariable Long id) {
        employeeService.deleteById(id);
        return new ResponseEntity<>("Employee deleted successfully", HttpStatus.OK);
    }

    @PutMapping("/{employeeId}/department/{departmentId}")
    public ResponseEntity<String> transfer(@PathVariable Long employeeId, @PathVariable Long departmentId) {
        employeeService.transferEmployeeToDepartment(employeeId, departmentId);
        return new ResponseEntity<>("Employee transferred successfully", HttpStatus.OK);
    }

    @GetMapping("/parse-salary")
    public Map<String, Object> parseSalary(@RequestParam @NotBlank String raw) {
        BigDecimal value = employeeService.parseSalary(raw);
        return Map.of("parsedSalary", value);
    }

    @GetMapping("/high-salary")
    public List<Employee> highSalary(@RequestParam @DecimalMin("0.0") BigDecimal threshold) {
        return employeeService.findHighSalary(threshold);
    }

    @GetMapping("/department")
    public List<Employee> findByDepartment(@RequestParam @NotBlank String name) {
        return employeeService.findByDepartmentName(name);
    }

    @GetMapping("/department/code")
    public List<Employee> byDepartmentCode(@RequestParam @NotBlank String code) {
        return employeeService.findEmployeesByDepartmentCode(code);
    }
}
