package com.slo.supplier_lifecycle_operations.controllers;

import com.fsc.supply_chain_common_operations.models.SupplierCompany;
import com.slo.supplier_lifecycle_operations.dtos.requests.SupplierCompanyRegistrationRequest;
import com.slo.supplier_lifecycle_operations.services.SupplierCompanyService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/api/v1/slo/company")
public class SupplierCompanyController {

    private SupplierCompanyService supplierCompanyService;

    @Autowired
    public SupplierCompanyController(SupplierCompanyService supplierCompanyService){
        this.supplierCompanyService = supplierCompanyService;
    }

   @PostMapping("/register")
   public ResponseEntity registerSupplierCompany(
           @RequestBody SupplierCompanyRegistrationRequest supplierCompanyRegistrationRequest
           ){
       log.info("Received request to register supplier company: companyName={}",
               supplierCompanyRegistrationRequest.getCompanyName());
        SupplierCompany supplierCompany = supplierCompanyService.registerSupplierCompany(supplierCompanyRegistrationRequest);
       log.info("Supplier company registered successfully: companyName={}",
               supplierCompany.getCompanyName());
        return new ResponseEntity(supplierCompany, HttpStatus.CREATED);
   }
}
