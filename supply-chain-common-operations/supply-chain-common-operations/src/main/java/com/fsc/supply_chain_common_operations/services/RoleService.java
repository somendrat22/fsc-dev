package com.fsc.supply_chain_common_operations.services;

import com.fsc.supply_chain_common_operations.dtos.requests.CreateRoleRequest;
import com.fsc.supply_chain_common_operations.enums.CompanyType;
import com.fsc.supply_chain_common_operations.models.Company;
import com.fsc.supply_chain_common_operations.models.Employee;
import com.fsc.supply_chain_common_operations.models.Operation;
import com.fsc.supply_chain_common_operations.models.Role;
import com.fsc.supply_chain_common_operations.repositories.RoleRepo;
import com.fsc.supply_chain_common_operations.utilities.SystemUtility;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Slf4j
@Service
public class RoleService implements EntityService<Role> {

    private OperationService operationService;
    private RoleRepo roleRepo;

    @Autowired
    public RoleService(OperationService operationService,
                       RoleRepo roleRepo){
        this.operationService = operationService;
        this.roleRepo = roleRepo;
    }

    public Role fetchRoleByName(String roleName){
        return roleRepo.findByRoleName(roleName);
    }

    public Role createRole(CreateRoleRequest createRoleRequest,
                           Employee creator){
        List<Operation> operations = operationService.fetchOperationsByName(createRoleRequest.getOperationName());
        Role role = Role.builder()
                .roleId(SystemUtility.generateId("ROLE"))
                .roleName(creator.getCompany().getCompanyName() + "_" + createRoleRequest.getRoleName())
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .createdBy(creator.getEmailAddress())
                .updatedBy(creator.getEmailAddress())
                .operations(operations)
                .build();
        return this.saveOrUpdate(role);
    }

    public List<Role> fetchRolesBySysId(List<UUID> roleSysIds){
        List<Role> roles = new ArrayList<>();
        for(UUID roleSysId : roleSysIds){
            Role role = roleRepo.findById(roleSysId).orElse(null);
            if(role == null){
                log.warn("Role with sysId={} does not exist", roleSysId.toString());
                continue;
            }
            roles.add(role);
        }
        return roles;
    }

    public Role saveOrUpdate(Role role){
        return this.roleRepo.save(role);
    }

    public Role createMaintRole(Company company, CompanyType companyType) {

        log.info("Starting main role creation. companyId={}, companyType={}",
                company.getSysId(), companyType);

        // Fetch main role operations based on company type
        log.info("Fetching main role operations. companyId={}, companyType={}",
                company.getSysId(), companyType);

        List<Operation> operations =
                operationService.fetchMaintOperationsByCompanyType(companyType);

        log.info("Main role operations fetched successfully. companyId={}, operationCount={}",
                company.getSysId(), operations.size());

        // Create main role
        Role role = Role.builder()
                .roleId(SystemUtility.generateId("ROLE"))
                .roleName(company.getCompanyName() + "_MAINT")
                .operations(operations)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .createdBy("system")
                .updatedBy("system")
                .build();

        log.debug("Main role created successfully. companyId={}, roleId={}, roleName={}",
                company.getSysId(),
                role.getRoleId(),
                role.getRoleName());

        // Save role
        role = this.saveOrUpdate(role);

        log.info("Main role saved successfully. companyId={}, roleId={}",
                company.getSysId(),
                role.getRoleId());

        return role;
    }

}
