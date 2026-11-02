package com.fsc.supply_chain_common_operations.services;

import com.fsc.supply_chain_common_operations.enums.CompanyType;
import com.fsc.supply_chain_common_operations.models.Operation;
import com.fsc.supply_chain_common_operations.repositories.OperationRepo;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Service
public class OperationService {

    private OperationRepo operationRepository;

    @Autowired
    public OperationService(OperationRepo operationRepo){
        this.operationRepository = operationRepo;
    }


    public List<Operation> fetchOperationsByName(List<String> operationNames){
        List<Operation> operations = new ArrayList<>();
        for(String operationName : operationNames){
            Operation operation = operationRepository.findByOperationName(operationName);
            if(operation == null){
                continue;
            }
            operations.add(operation);
        }
        return operations;
    }

    /**
     * First we need to load operations in the table.
     * @param companyType
     * @return
     */
    public List<Operation> fetchMaintOperationsByCompanyType(CompanyType companyType) {

        log.info("Fetching MAINT operations for companyType={}", companyType);

        List<Operation> operations;

        switch (companyType) {

            case SUPPLIER_COMPANY:
                log.debug("Fetching SUPPLIER_COMPANY MAINT operations");
                operations = operationRepository.fetchOperationsForSupplierMaint();
                break;

            case PROCUREMENT_COMPANY:
                log.debug("Fetching PROCUREMENT_COMPANY MAINT operations");
                operations = operationRepository.fetchOperationsForProcurementCompanyMaint();
                break;

            case INTERNAL:
                log.debug("Fetching INTERNAL MAINT operations");
                operations = operationRepository.fetchOperationsForInternalMaint();
                break;

            default:
                log.error("Invalid company type provided for fetching MAINT operations. companyType={}",
                        companyType);
                throw new IllegalArgumentException(
                        "Invalid company type passed: " + companyType);
        }

        log.info("Successfully fetched MAINT operations. companyType={}, operationCount={}",
                companyType, operations.size());

        return operations;
    }

    @EventListener(ApplicationReadyEvent.class)
    @Transactional
    public void initializeOperations() {
        /**
         * We are cretaing list of operatoions
         */
        List<OperationSeed> operations = List.of(
                new OperationSeed("OPR-001", "VIEW_COMPANY_PROFILE", "COMPANY"),
                new OperationSeed("OPR-002", "UPDATE_COMPANY_PROFILE", "COMPANY"),

                new OperationSeed("OPR-003", "CREATE_COMPANY_EMPLOYEE", "USER_MANAGEMENT"),
                new OperationSeed("OPR-004", "VIEW_COMPANY_EMPLOYEE", "USER_MANAGEMENT"),
                new OperationSeed("OPR-005", "UPDATE_COMPANY_EMPLOYEE", "USER_MANAGEMENT"),
                new OperationSeed("OPR-006", "DEACTIVATE_COMPANY_EMPLOYEE", "USER_MANAGEMENT"),

                new OperationSeed("OPR-007", "CREATE_SUPPLIER", "SUPPLIER_MANAGEMENT"),
                new OperationSeed("OPR-008", "VIEW_SUPPLIER", "SUPPLIER_MANAGEMENT"),
                new OperationSeed("OPR-009", "UPDATE_SUPPLIER", "SUPPLIER_MANAGEMENT"),
                new OperationSeed("OPR-010", "APPROVE_SUPPLIER", "SUPPLIER_MANAGEMENT"),

                new OperationSeed("OPR-011", "CREATE_PRODUCT", "CATALOG"),
                new OperationSeed("OPR-012", "VIEW_PRODUCT", "CATALOG"),
                new OperationSeed("OPR-013", "UPDATE_PRODUCT", "CATALOG"),
                new OperationSeed("OPR-014", "DELETE_PRODUCT", "CATALOG"),

                new OperationSeed("OPR-015", "CREATE_PURCHASE_REQUEST", "PURCHASE_REQUEST"),
                new OperationSeed("OPR-016", "VIEW_PURCHASE_REQUEST", "PURCHASE_REQUEST"),
                new OperationSeed("OPR-017", "UPDATE_PURCHASE_REQUEST", "PURCHASE_REQUEST"),
                new OperationSeed("OPR-018", "SUBMIT_PURCHASE_REQUEST", "PURCHASE_REQUEST"),
                new OperationSeed("OPR-019", "APPROVE_PURCHASE_REQUEST", "PURCHASE_REQUEST"),
                new OperationSeed("OPR-020", "REJECT_PURCHASE_REQUEST", "PURCHASE_REQUEST"),

                new OperationSeed("OPR-021", "CREATE_SOURCING_REQUEST", "SOURCING"),
                new OperationSeed("OPR-022", "VIEW_SOURCING_REQUEST", "SOURCING"),
                new OperationSeed("OPR-023", "UPDATE_SOURCING_REQUEST", "SOURCING"),
                new OperationSeed("OPR-024", "PUBLISH_SOURCING_REQUEST", "SOURCING"),
                new OperationSeed("OPR-025", "CLOSE_SOURCING_REQUEST", "SOURCING"),

                new OperationSeed("OPR-026", "CREATE_BID", "BID_MANAGEMENT"),
                new OperationSeed("OPR-027", "VIEW_BID", "BID_MANAGEMENT"),
                new OperationSeed("OPR-028", "UPDATE_BID", "BID_MANAGEMENT"),
                new OperationSeed("OPR-029", "SUBMIT_BID", "BID_MANAGEMENT"),
                new OperationSeed("OPR-030", "COMPARE_BIDS", "BID_MANAGEMENT"),
                new OperationSeed("OPR-031", "AWARD_BID", "BID_MANAGEMENT"),

                new OperationSeed("OPR-032", "CREATE_PURCHASE_ORDER", "PURCHASE_ORDER"),
                new OperationSeed("OPR-033", "VIEW_PURCHASE_ORDER", "PURCHASE_ORDER"),
                new OperationSeed("OPR-034", "UPDATE_PURCHASE_ORDER", "PURCHASE_ORDER"),
                new OperationSeed("OPR-035", "APPROVE_PURCHASE_ORDER", "PURCHASE_ORDER"),
                new OperationSeed("OPR-036", "CANCEL_PURCHASE_ORDER", "PURCHASE_ORDER"),
                new OperationSeed("OPR-037", "ACKNOWLEDGE_PURCHASE_ORDER", "PURCHASE_ORDER"),

                new OperationSeed("OPR-038", "CREATE_CONTRACT", "CONTRACT"),
                new OperationSeed("OPR-039", "VIEW_CONTRACT", "CONTRACT"),
                new OperationSeed("OPR-040", "APPROVE_CONTRACT", "CONTRACT"),

                new OperationSeed("OPR-041", "CREATE_INVOICE", "INVOICE"),
                new OperationSeed("OPR-042", "VIEW_INVOICE", "INVOICE"),
                new OperationSeed("OPR-043", "APPROVE_INVOICE", "INVOICE"),
                new OperationSeed("OPR-044", "VIEW_PAYMENT_HISTORY", "PAYMENT"),

                new OperationSeed("OPR-045", "VIEW_PROCUREMENT_REPORT", "REPORTING"),
                new OperationSeed("OPR-046", "EXPORT_PROCUREMENT_REPORT", "REPORTING"),
                new OperationSeed("OPR-047", "VIEW_AUDIT_LOG", "AUDIT")
        );

        // Existing operations that are already present in the table
        // Fetch existing operation names to prevent duplicate inserts.
        Set<String> existingOperationNames = operationRepository.findAll()
                .stream()
                .map(Operation::getOperationName)
                .collect(Collectors.toSet());

        LocalDateTime now = LocalDateTime.now();

        List<Operation> newOperations = operations.stream()
                .filter(operationSeed ->
                        !existingOperationNames.contains(operationSeed.operationName()))
                .map(seed -> {
                    Operation operation = new Operation();

                    // Operation fields
                    operation.setOperationId(seed.operationId());
                    operation.setOperationName(seed.operationName());
                    operation.setOperationCategory(seed.operationCategory());

                    // GlobalRecord fields — adjust to your actual field names.
                    operation.setCreatedAt(LocalDateTime.now());
                    operation.setUpdatedAt(LocalDateTime.now());

                    operation.setCreatedBy("system");
                    operation.setUpdatedBy("system");

                    return operation;
                })
                .toList();

        if (!newOperations.isEmpty()) {
            operationRepository.saveAll(newOperations);
        }
    }

    private record OperationSeed(
            String operationId,
            String operationName,
            String operationCategory
    ) {}
}


