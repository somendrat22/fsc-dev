package com.fsc.supply_chain_common_operations.models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "purchase-request-lines")
@Entity
public class PurchaseRequestLine extends RequestLine {

    @ManyToOne
    private PurchaseRequest purchaseRequest;

    @Column(precision = 19, scale = 4)
    private BigDecimal estimatedUnitPrice;

    @Column(precision = 19, scale = 4)
    private BigDecimal estimatedLineAmount;

    @Column(length = 3)
    private String currencyCode;

    private LocalDate requiredDate;

    @Column(length = 500)
    private String deliveryLocation;

    @Column(precision = 7, scale = 4)
    private BigDecimal taxRate;
}
