package com.fsc.supply_chain_common_operations.models;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "procurement-companies")
@Entity
public class ProcurementCompany extends Company{
    private String procurementCode;
    private Integer fiscalYearStartMonth;
    private Boolean approvalRequired;
}
