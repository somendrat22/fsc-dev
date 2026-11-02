package com.fsc.supply_chain_common_operations.transformers;

import com.fsc.supply_chain_common_operations.enums.EmploymentStatus;
import com.fsc.supply_chain_common_operations.enums.UserStatus;
import com.fsc.supply_chain_common_operations.models.Company;
import com.fsc.supply_chain_common_operations.models.Employee;
import com.fsc.supply_chain_common_operations.utilities.SystemUtility;

import java.time.LocalDate;
import java.time.LocalDateTime;

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
}
