package com.fsc.supply_chain_common_operations.services;

import com.fsc.supply_chain_common_operations.enums.CompanyType;
import com.fsc.supply_chain_common_operations.models.Company;
import com.fsc.supply_chain_common_operations.models.Employee;
import com.fsc.supply_chain_common_operations.models.Role;
import com.fsc.supply_chain_common_operations.repositories.EmployeeRepo;
import com.fsc.supply_chain_common_operations.transformers.EntityTransformer;
import com.fsc.supply_chain_common_operations.utilities.SystemUtility;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class EmployeeService implements EntityService<Employee> {

    private RoleService roleService;
    private EmployeeRepo employeeRepo;

    @Autowired
    public EmployeeService(RoleService roleService,
                           EmployeeRepo employeeRepo){
        this.roleService = roleService;
        this.employeeRepo = employeeRepo;
    }

    public Employee getEmployeeByEmail(String email){
        return this.employeeRepo.findByWorkEmail(email);
    }

    public Employee saveOrUpdate(Employee employee){
        return this.employeeRepo.save(employee);
    }

    public Employee createMaintUserForCompany(Company company, CompanyType companyType) {

        log.info("Starting main user creation. companyId={}, companyType={}",
                company.getSysId(), companyType);

        // Create main/admin role for the company
        log.info("Creating main role. companyId={}, companyType={}",
                company.getSysId(), companyType);

        Role maintRole = roleService.createMaintRole(company, companyType);

        log.info("Main role created successfully. companyId={}, roleId={}",
                company.getSysId(), maintRole.getSysId());

        // Generate password and map company details to first employee
        String generatedPassword = SystemUtility.generatePassword(10);

        Employee maintEmployee =
                EntityTransformer.mapCompanyToEmployee(company, generatedPassword);

        log.debug("Company mapped to main employee successfully. companyId={}",
                company.getSysId());

        // Assign main/admin role
        maintEmployee.setRoles(List.of(maintRole));

        log.debug("Main role assigned to employee. companyId={}, roleId={}",
                company.getSysId(), maintRole.getSysId());

        // Save employee
        maintEmployee = this.saveOrUpdate(maintEmployee);

        log.info("Main employee created successfully. companyId={}, employeeId={}",
                company.getSysId(), maintEmployee.getSysId());

        // TODO: Send account creation notification to employee.
        log.info("Main user creation completed successfully. companyId={}, employeeId={}",
                company.getSysId(), maintEmployee.getSysId());

        return maintEmployee;
    }

}
