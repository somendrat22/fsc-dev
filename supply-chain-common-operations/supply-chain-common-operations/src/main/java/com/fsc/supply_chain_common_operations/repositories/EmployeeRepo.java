package com.fsc.supply_chain_common_operations.repositories;

import com.fsc.supply_chain_common_operations.models.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface EmployeeRepo extends JpaRepository<Employee, UUID> {

    public Employee findByWorkEmail(String email);

}
