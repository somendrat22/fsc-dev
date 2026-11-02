package com.fsc.supply_chain_common_operations.enums;

public enum CompanyStatus {
    PENDING,       // Company registered, awaiting review
    UNDER_REVIEW,  // Company details are being verified
    ACTIVE,        // Company is approved and operational
    SUSPENDED,     // Temporarily disabled
    INACTIVE,      // Deactivated by an administrator
    REJECTED       // Company registration was rejected
}
