package com.fsc.supply_chain_common_operations.models;

import jakarta.persistence.Entity;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "supplier-companies")
@SuperBuilder
@Entity
public class SupplierCompany extends Company{
    private String supplierCode;
    private String supplierCategory; // MANUFACTURER, SERVICE
    private Integer leadTimeDays;
    private BigDecimal minimumOrderValue;
}
