package com.fsc.supply_chain_common_operations.models;

import com.fsc.supply_chain_common_operations.enums.ProcurementType;
import com.fsc.supply_chain_common_operations.enums.PurchaseRequestType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * For example if I am a procurement company called TCS. Somendra is employee working for TCS
 * Somendra wants to place purchase request on behalf of TCS.
 * Somendra PR --- Somendra's Managers approval ---- Supplier Company
 *
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "purchase_requests")
@Entity
public class PurchaseRequest extends Request {

    @Enumerated(EnumType.STRING)
    @Column(length = 30)
    private PurchaseRequestType purchaseRequestType;

    @ManyToOne
    private Employee submitterEmployee;

    @ManyToOne
    private Employee procurementApprovalManager;

    @ManyToOne
    private Employee supplierReviewerApprovalEmployee;

    @ManyToOne
    private Employee supplierManagerApproval;


    private String deliveryLocation;

    @Column(precision = 19, scale = 4)
    private BigDecimal estimatedTotalAmount;

    @Column(length = 3)
    private String currencyCode;

    private String budgetCode;

    @Enumerated(EnumType.STRING)
    @Column(length = 30)
    private ProcurementType procurementType;
}
