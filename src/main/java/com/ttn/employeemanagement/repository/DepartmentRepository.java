package com.ttn.employeemanagement.repository;

import com.ttn.employeemanagement.entity.Department;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface DepartmentRepository extends JpaRepository<Department, Long> {

    Optional<Department> findById(Long id);

    Optional<Department> findByCode(String code);

    @Query("SELECT d FROM Department d WHERE "
                    + "(SELECT COUNT(e) FROM Employee e WHERE e.department = d AND e.active = true) >= :min")
    List<Department> findDepartmentsWithAtLeastActiveEmployees(@Param("min") long min);

    @Query("SELECT DISTINCT d FROM Department d JOIN d.employees e WHERE d.active = false")
    List<Department> findInactiveDepartmentsWithEmployees();
}
