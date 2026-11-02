package com.fsc.supply_chain_common_operations.models;

import com.fsc.supply_chain_common_operations.enums.EmploymentStatus;
import com.fsc.supply_chain_common_operations.enums.EmploymentType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.time.LocalDate;

@SuperBuilder
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "employees")
public class Employee extends User {
    @Column(nullable = false, unique = true)
    private String employeeCode;

    @ManyToOne
    private Company company;

    @ManyToOne
    private Employee manager;

    private String department;

    private String designation;

    private LocalDate joiningDate;

    private LocalDate probationEndDate;

    private LocalDate dateOfLeaving;

    @Enumerated(EnumType.STRING)
    @Column(length = 30)
    private EmploymentType employmentType;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private EmploymentStatus employmentStatus = EmploymentStatus.ACTIVE;

    private String workEmail;

    private String workPhone;

    private String officeLocation;
}
