package com.ttn.employeemanagement.config;

import com.ttn.employeemanagement.entity.Department;
import com.ttn.employeemanagement.entity.Employee;
import com.ttn.employeemanagement.repository.DepartmentRepository;
import com.ttn.employeemanagement.repository.EmployeeRepository;
import java.math.BigDecimal;
import java.time.LocalDate;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class DataInitializer implements CommandLineRunner {

    private final DepartmentRepository departmentRepository;
    private final EmployeeRepository employeeRepository;

    @Override
    public void run(String... args) {
        if (departmentRepository.count() > 0 && employeeRepository.count() > 0) {
            log.info("Database already seeded; skipping CommandLineRunner.");
            return;
        }

        Department engineering = departmentRepository.save(Department.builder()
                .name("Engineering")
                .code("ENG")
                .location("Bengaluru — Tower A")
                .active(true)
                .build());
        Department hr = departmentRepository.save(Department.builder()
                .name("Human Resources")
                .code("HR")
                .location("Hyderabad — Campus 2")
                .active(true)
                .build());
        Department legacy = departmentRepository.save(Department.builder()
                .name("Legacy Systems")
                .code("LEG")
                .location("Remote")
                .active(false)
                .build());

        employeeRepository.save(Employee.builder()
                .name("Ananya Shukla")
                .email("ananya.shukla@example.com")
                .salary(new BigDecimal("95000.00"))
                .department(engineering)
                .jobTitle("Principal Engineer")
                .hireDate(LocalDate.of(2021, 4, 12))
                .active(true)
                .build());
        employeeRepository.save(Employee.builder()
                .name("Rahul Verma")
                .email("rahul.verma@example.com")
                .salary(new BigDecimal("88000.00"))
                .department(engineering)
                .jobTitle("Senior Engineer")
                .hireDate(LocalDate.of(2022, 1, 5))
                .active(true)
                .build());
        employeeRepository.save(Employee.builder()
                .name("Priya Nair")
                .email("priya.nair@example.com")
                .salary(new BigDecimal("102000.00"))
                .department(engineering)
                .jobTitle("Engineering Manager")
                .hireDate(LocalDate.of(2019, 8, 19))
                .active(true)
                .build());
        employeeRepository.save(Employee.builder()
                .name("Sneha Patel")
                .email("sneha.patel@example.com")
                .salary(new BigDecimal("62000.00"))
                .department(hr)
                .jobTitle("HR Business Partner")
                .hireDate(LocalDate.of(2023, 3, 1))
                .active(true)
                .build());
        employeeRepository.save(Employee.builder()
                .name("Vikram Singh")
                .email("vikram.singh@example.com")
                .salary(new BigDecimal("71000.00"))
                .department(hr)
                .jobTitle("Talent Acquisition")
                .hireDate(LocalDate.of(2020, 11, 23))
                .active(true)
                .build());
        employeeRepository.save(Employee.builder()
                .name("Kiran Rao")
                .email("kiran.rao@example.com")
                .salary(new BigDecimal("78000.00"))
                .department(legacy)
                .jobTitle("Legacy Maintainer")
                .hireDate(LocalDate.of(2016, 2, 29))
                .active(true)
                .build());

        log.info("Seeded 3 departments and 6 employees");
    }
}
