package com.slo.supplier_lifecycle_operations.dtos.requests;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SupplierCompanyRegistrationRequest {

    // Supplier details

    private String supplierCategory;


    private Integer leadTimeDays;


    private BigDecimal minimumOrderValue;

    // Company details

    private String companyName;


    private String legalName;


    private String gstNumber;


    private String panNumber;


    private String emailAddress;

    private String website;


    private String contactNumber;
}
