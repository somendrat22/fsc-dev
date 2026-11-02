package com.fsc.supply_chain_common_operations.models;

import com.fsc.supply_chain_common_operations.enums.BidStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "bids")
@Entity
public class Bid extends GlobalRecord {

    @Column(name = "bid_number", nullable = false, length = 50)
    private String bidNumber;

    @ManyToOne
    private SourcingRequest sourcingRequest;

    @ManyToOne
    private SupplierCompany supplierCompany;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private BidStatus bidStatus = BidStatus.DRAFT;

    private Instant submissionDate;

    private LocalDate validUntil;

    @Column(length = 3, nullable = false)
    private String currencyCode;

    @Column(precision = 19, scale = 4)
    private BigDecimal totalBidAmount;

    private Integer deliveryLeadTimeDays;

    @Column(length = 1000)
    private String paymentTerms;

    @Column(length = 1000)
    private String deliveryTerms;

    @Column(columnDefinition = "TEXT")
    private String technicalProposal;

    @Column(columnDefinition = "TEXT")
    private String commercialProposal;

    @Column(length = 2000)
    private String remarks;

    @Column(precision = 7, scale = 4)
    private BigDecimal evaluationScore;

    private Instant evaluatedAt;
}
