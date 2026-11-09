package com.fsc.supply_chain_common_operations.utilities;

import com.fsc.supply_chain_common_operations.dtos.requests.CommonValidationDto;
import com.fsc.supply_chain_common_operations.exceptions.RecordDoesNotExistException;
import com.fsc.supply_chain_common_operations.exceptions.UnAuthorizedException;
import com.fsc.supply_chain_common_operations.models.Company;
import com.fsc.supply_chain_common_operations.models.Employee;
import com.fsc.supply_chain_common_operations.models.Role;
import com.fsc.supply_chain_common_operations.repositories.CompanyRepo;
import com.fsc.supply_chain_common_operations.repositories.EmployeeRepo;
import com.fsc.supply_chain_common_operations.repositories.RoleRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.naming.directory.InvalidAttributesException;
import java.util.UUID;

@Service
public class CommonUtility {

    private CompanyRepo companyRepo;
    private EmployeeRepo employeeRepo;
    private RoleRepo roleRepo;

    @Autowired
    public CommonUtility(CompanyRepo companyRepo,
                         EmployeeRepo employeeRepo,
                         RoleRepo roleRepo){
        this.companyRepo = companyRepo;
        this.employeeRepo = employeeRepo;
        this.roleRepo = roleRepo;
    }


    /**
     * 1. Checking whatever sysId we are passing here is exist in DB or not.
     * 2. Then we need to validate all those sysId is owned by that company or not.
     * sysId passed is existing or not,
     * @return
     */
    public void validateBasicChecks(
            CommonValidationDto commonValidationDto
    ){
       // First Validation was to check that company exist or not.
        if(commonValidationDto.getCompanySysId() == null){
            throw new IllegalArgumentException("Company sysId not passed");
        }
        Company company = companyRepo.findById(commonValidationDto.getCompanySysId()).orElse(null);
        if(company == null){
            throw new RecordDoesNotExistException("Company sysId does not exist");
        }
        // Check employee exist or not. This is the employee who is performing task
        if(commonValidationDto.getEmpSysId() == null){
            throw new IllegalArgumentException("Employee sysId not passed");
        }

        Employee employee = employeeRepo.findById(commonValidationDto.getEmpSysId()).orElse(null);
        if(employee == null){
            throw new RecordDoesNotExistException("Company sysId does not exist");
        }

        // Validating roles are temp
        if(commonValidationDto.getRoleSysIds() != null){
            // We will validate roles belongs to this company or not.
            for(UUID roleSysId : commonValidationDto.getRoleSysIds()){
                Role role = this.roleRepo.findById(roleSysId).orElse(null);
                if(role == null ){
                    throw new RecordDoesNotExistException("Role sysId does not exist");
                }
                if(!role.getRoleName().contains(company.getCompanyName() + "_")){
                    throw new UnAuthorizedException("User is not allowed to use this role.");
                }
            }
        }
    }

}
