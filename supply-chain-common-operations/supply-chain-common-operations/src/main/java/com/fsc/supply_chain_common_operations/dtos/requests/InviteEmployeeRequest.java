package com.fsc.supply_chain_common_operations.dtos.requests;

import com.fsc.supply_chain_common_operations.enums.EmploymentStatus;
import com.fsc.supply_chain_common_operations.enums.EmploymentType;
import com.fsc.supply_chain_common_operations.enums.UserStatus;
import com.fsc.supply_chain_common_operations.models.Company;
import com.fsc.supply_chain_common_operations.models.Employee;
import com.fsc.supply_chain_common_operations.models.Role;
import jakarta.persistence.*;

import java.time.Instant;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public class InviteEmployeeRequest {
    private UUID companySysId;

    private UUID managerSysId;

    private String department;

    private String designation;

    private LocalDate joiningDate;

    private LocalDate probationEndDate;

    private EmploymentType employmentType;

    private String workEmail;

    private String workPhone;

    private String firstName;

    private String lastName;

    private String emailAddress;

    private String phoneNumber;

    private List<UUID> roleSysIds;
}
