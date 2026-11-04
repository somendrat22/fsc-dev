package com.fsc.supply_chain_common_operations.transformers;

import com.fsc.supply_chain_common_operations.dtos.requests.InviteEmployeeRequest;
import com.fsc.supply_chain_common_operations.enums.EmploymentStatus;
import com.fsc.supply_chain_common_operations.enums.UserStatus;
import com.fsc.supply_chain_common_operations.models.Company;
import com.fsc.supply_chain_common_operations.models.Employee;
import com.fsc.supply_chain_common_operations.models.Role;
import com.fsc.supply_chain_common_operations.utilities.SystemUtility;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public class EntityTransformer {
    public static Employee mapCompanyToEmployee(Company company, String passwordHash) {

        return Employee.builder()
                // User fields
                .firstName(company.getCompanyName())
                .lastName(company.getLegalName())
                .emailAddress(company.getEmailAddress())
                .phoneNumber(company.getContactNumber())
                .passwordHash(passwordHash)
                .emailVerified(false)
                .phoneVerified(false)
                .userStatus(UserStatus.PENDING_VERIFICATION)

                // Employee fields
                .employeeCode(SystemUtility.generateId("EMPLOYEE"))
                .company(company)
                .manager(null)
                .department(null)
                .designation("COMPANY_ADMIN")
                .joiningDate(LocalDate.now())
                .probationEndDate(null)
                .dateOfLeaving(null)
                .employmentType(null)
                .employmentStatus(EmploymentStatus.ACTIVE)
                .workEmail(company.getEmailAddress())
                .workPhone(company.getContactNumber())
                .officeLocation(null)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .createdBy("system")
                .updatedBy("system")

                .build();
    }


    public static Employee mapEmpRequestToEmpModel(
            InviteEmployeeRequest inviteEmployeeRequest,
            Company company,
            Employee creator,
            Employee manager,
            List<Role> roles
    ){
        return Employee.builder()
                .employeeCode(SystemUtility.generateId("EMPLOYEE"))
                .emailAddress(inviteEmployeeRequest.getEmailAddress())
                .company(company)
                .department(inviteEmployeeRequest.getDepartment())
                .designation(inviteEmployeeRequest.getDesignation())
                .joiningDate(inviteEmployeeRequest.getJoiningDate())
                .probationEndDate(inviteEmployeeRequest.getProbationEndDate())
                .employmentType(inviteEmployeeRequest.getEmploymentType())
                .workEmail(inviteEmployeeRequest.getWorkEmail())
                .workPhone(inviteEmployeeRequest.getWorkPhone())
                .firstName(inviteEmployeeRequest.getFirstName())
                .lastName(inviteEmployeeRequest.getLastName())
                .emailAddress(inviteEmployeeRequest.getEmailAddress())
                .phoneNumber(inviteEmployeeRequest.getPhoneNumber())
                .roles(roles)
                .passwordHash(SystemUtility.generatePassword(10))
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .createdBy(creator.getEmailAddress())
                .updatedBy(creator.getEmailAddress())
                .build();
    }
}
