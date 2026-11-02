package com.slo.supplier_lifecycle_operations.transformers;


import com.fsc.supply_chain_common_operations.enums.CompanyStatus;
import com.fsc.supply_chain_common_operations.models.SupplierCompany;
import com.fsc.supply_chain_common_operations.utilities.SystemUtility;
import com.slo.supplier_lifecycle_operations.dtos.requests.SupplierCompanyRegistrationRequest;

import java.time.LocalDateTime;

public class RequestTransformer {

    public static SupplierCompany mapRegistrationRequestToSupplierCompany(
            SupplierCompanyRegistrationRequest request
    ){
        // I will map request to model
        SupplierCompany supplierCompany = SupplierCompany.builder()
                .supplierCategory(request.getSupplierCategory())
                .leadTimeDays(request.getLeadTimeDays())
                .minimumOrderValue(request.getMinimumOrderValue())

                .companyName(request.getCompanyName())
                .legalName(request.getLegalName())
                .gstNumber(request.getGstNumber())
                .panNumber(request.getPanNumber())
                .emailAddress(request.getEmailAddress())
                .website(request.getWebsite())
                .contactNumber(request.getContactNumber())

                .companyCode(SystemUtility.generateId("COMP"))
                .supplierCode(SystemUtility.generateId("SUPPLIER"))
                .status(CompanyStatus.PENDING)

                .createdAt(LocalDateTime.now())
                .createdBy("system")
                .updatedAt(LocalDateTime.now())
                .updatedBy("system")

                .build();

        return supplierCompany;
    }


}
