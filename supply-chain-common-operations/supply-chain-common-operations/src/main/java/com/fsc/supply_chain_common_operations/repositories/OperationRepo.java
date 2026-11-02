package com.fsc.supply_chain_common_operations.repositories;

import com.fsc.supply_chain_common_operations.models.Operation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface OperationRepo extends JpaRepository<Operation, UUID> {

    public Operation findByOperationName(String operationName);

    @Query(value = """
            SELECT *
            FROM operations o
            WHERE o.operation_category IN (
                'COMPANY',
                'USER_MANAGEMENT',
                'SUPPLIER_MANAGEMENT',
                'CATALOG',
                'PURCHASE_REQUEST',
                'SOURCING',
                'BID_MANAGEMENT',
                'PURCHASE_ORDER',
                'CONTRACT',
                'INVOICE',
                'PAYMENT',
                'REPORTING'
            )
            """, nativeQuery = true)
    List<Operation> fetchOperationsForProcurementCompanyMaint();


    @Query(value = """
            SELECT *
            FROM operations o
            WHERE o.operation_category IN (
                'COMPANY',
                'USER_MANAGEMENT',
                'CATALOG',
                'SOURCING',
                'BID_MANAGEMENT',
                'PURCHASE_ORDER',
                'CONTRACT',
                'INVOICE',
                'PAYMENT'
            )
            """, nativeQuery = true)
    List<Operation> fetchOperationsForSupplierMaint();


    @Query(value = """
            SELECT *
            FROM operations 
            WHERE operation_category IN (
                'COMPANY',
                'USER_MANAGEMENT',
                'SUPPLIER_MANAGEMENT',
                'CATALOG',
                'PURCHASE_REQUEST',
                'SOURCING',
                'BID_MANAGEMENT',
                'PURCHASE_ORDER',
                'CONTRACT',
                'INVOICE',
                'PAYMENT',
                'REPORTING',
                'AUDIT'
            )
            """, nativeQuery = true)
    List<Operation> fetchOperationsForInternalMaint();
}
