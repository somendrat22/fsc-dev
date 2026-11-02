package com.fsc.supply_chain_common_operations.models;

import com.fsc.supply_chain_common_operations.enums.CompanyStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@SuperBuilder
@Data
@NoArgsConstructor
@AllArgsConstructor
@Inheritance(strategy = InheritanceType.JOINED)
@Table(name = "companies")
@Entity
public class Company extends GlobalRecord{
    private String companyCode; // COMP-001
    private String companyName; // Microsoft
    private String legalName; // Microsoft India Pvt Ltd.
    private String gstNumber;
    private String panNumber;
    private String emailAddress;
    private String website;
    private String contactNumber;

    @Enumerated(EnumType.STRING)
    private CompanyStatus status; // PENDING, REJECTED, INACTIVE, ACTIVE
}
