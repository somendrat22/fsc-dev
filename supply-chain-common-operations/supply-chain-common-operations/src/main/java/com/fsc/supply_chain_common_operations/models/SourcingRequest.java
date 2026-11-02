package com.fsc.supply_chain_common_operations.models;

import com.fsc.supply_chain_common_operations.enums.SourcingMethod;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.time.Instant;

@SuperBuilder
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "sourcing_requests")
@Entity
public class SourcingRequest extends Request {
    @Enumerated(EnumType.STRING)
    @Column(length = 30)
    private SourcingMethod sourcingMethod;

    @ManyToOne
    private Employee submitterEmployee;

    @ManyToOne
    private Employee procurementApprovalManager;

    private Instant responseDeadline;

    private Instant evaluationDeadline;

    private Integer targetSupplierCount;

    @Column(length = 2000)
    private String evaluationCriteria;

    @Column(length = 2000)
    private String commercialTerms;

    @Column(length = 3)
    private String currencyCode;
}
