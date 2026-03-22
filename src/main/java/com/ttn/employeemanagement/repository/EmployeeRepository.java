package com.ttn.employeemanagement.repository;

import com.ttn.employeemanagement.entity.Employee;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface EmployeeRepository extends JpaRepository<Employee, Long> {

    List<Employee> findByName(String name);

    Optional<Employee> findByEmail(String email);

    @Query("SELECT e FROM Employee e JOIN e.department d WHERE d.name = :deptName")
    List<Employee> findEmployeesByDepartmentName(@Param("deptName") String deptName);

    @Query(value = "SELECT * FROM employees WHERE salary > :threshold", nativeQuery = true)
    List<Employee> findHighSalaryEmployees(@Param("threshold") BigDecimal threshold);

    @Query("SELECT e FROM Employee e JOIN e.department d WHERE UPPER(TRIM(d.code)) = UPPER(TRIM(:code))")
    List<Employee> findEmployeesByDepartmentCode(@Param("code") String code);

    @Query("SELECT COALESCE(AVG(e.salary), 0) FROM Employee e WHERE e.department.id = :deptId AND e.active = true")
    BigDecimal averageActiveSalaryByDepartmentId(@Param("deptId") Long deptId);
}
