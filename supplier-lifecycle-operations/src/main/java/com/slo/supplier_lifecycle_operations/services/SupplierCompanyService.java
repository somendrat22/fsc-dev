package com.slo.supplier_lifecycle_operations.services;

import com.fsc.supply_chain_common_operations.constants.NotificationTemplateConstant;
import com.fsc.supply_chain_common_operations.enums.CompanyType;
import com.fsc.supply_chain_common_operations.enums.NotificationPriority;
import com.fsc.supply_chain_common_operations.enums.NotificationStatus;
import com.fsc.supply_chain_common_operations.models.*;
import com.fsc.supply_chain_common_operations.repositories.NotificationContextRepo;
import com.fsc.supply_chain_common_operations.repositories.NotificationTemplateRepo;
import com.fsc.supply_chain_common_operations.repositories.SupplierCompanyRepo;
import com.fsc.supply_chain_common_operations.services.EmployeeService;
import com.fsc.supply_chain_common_operations.services.NotificationService;
import com.fsc.supply_chain_common_operations.utilities.SystemUtility;
import com.slo.supplier_lifecycle_operations.dtos.requests.SupplierCompanyRegistrationRequest;
import com.slo.supplier_lifecycle_operations.transformers.RequestTransformer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ExecutorService;

@Slf4j
@Service
public class SupplierCompanyService {

    private SupplierCompanyRepo supplierCompanyRepo;
    private EmployeeService employeeService;
    private NotificationTemplateRepo notificationTemplateRepo;
    private NotificationContextRepo notificationContextRepo;
    private ExecutorService executorService;
    private NotificationService notificationService;

    @Autowired
    public SupplierCompanyService(SupplierCompanyRepo supplierCompanyRepo,
                                  EmployeeService employeeService,
                                  NotificationTemplateRepo notificationTemplateRepo,
                                  NotificationService notificationService,
                                  NotificationContextRepo notificationContextRepo,
                                  ExecutorService executorService){
        this.supplierCompanyRepo = supplierCompanyRepo;
        this.employeeService = employeeService;
        this.notificationTemplateRepo = notificationTemplateRepo;
        this.notificationService = notificationService;
        this.executorService = executorService;
        this.notificationContextRepo = notificationContextRepo;
    }

    public SupplierCompany registerSupplierCompany(
            SupplierCompanyRegistrationRequest supplierCompanyRegistrationRequest) {

        log.info("Starting supplier company registration. companyName={}",
                supplierCompanyRegistrationRequest.getCompanyName());

        // Map registration request to the Supplier Company model
        SupplierCompany supplierCompany =
                RequestTransformer.mapRegistrationRequestToSupplierCompany(
                        supplierCompanyRegistrationRequest);

        log.debug("Successfully mapped registration request to SupplierCompany. companyName={}",
                supplierCompany.getCompanyName());

        // Save supplier company
        supplierCompany = this.saveOrUpdate(supplierCompany);

        log.info("Supplier company saved successfully. companyName={}, companyId={}",
                supplierCompany.getCompanyName(),
                supplierCompany.getSysId());

        // Create main/admin user for the supplier company
        log.info("Creating main employee for supplier company. companyId={}",
                supplierCompany.getSysId());

        Employee maintEmployee = employeeService.createMaintUserForCompany(
                supplierCompany,
                CompanyType.SUPPLIER_COMPANY);

        log.info("Main employee created successfully. companyId={}, employeeId={}",
                supplierCompany.getSysId(),
                maintEmployee.getSysId());

        log.info("Supplier company registration completed successfully. companyId={}",
                supplierCompany.getSysId());

        // 1. Decide which notification you want to send.
        NotificationTemplate notificationTemplate = notificationTemplateRepo.findById(NotificationTemplateConstant.COMPANY_REGISTRATION_TEMPLATE_ID).orElse(null);
        // 2. As we got the template we need to create the context for the template
        HashMap<String, String> emailMap = new HashMap<>();

        emailMap.put("maintUserName", maintEmployee.getFirstName());
        emailMap.put("companyName", supplierCompany.getCompanyName());
        emailMap.put("maintUserEmail", maintEmployee.getEmailAddress());
        emailMap.put("temporaryPassword", maintEmployee.getPasswordHash());
        emailMap.put("applicationUrl", "https://google.com");
        emailMap.put("currentYear", String.valueOf(LocalDate.now().getYear()));

        HashMap<String, String> subjectLineMap = new HashMap<>();

        subjectLineMap.put("companyName", supplierCompany.getCompanyName());

        NotificationContext notificationContext = new NotificationContext();
        notificationContext.setSubjectLineMap(subjectLineMap);
        notificationContext.setEmailMap(emailMap);
        notificationContext.setSmsMap(new HashMap<>());
        notificationContext.setWhatsAppMap(new HashMap<>());
        notificationContext.setContextId(SystemUtility.generateId("NOTIFICATION-CONTEXT"));
        notificationContext.setCreatedBy("system");
        notificationContext.setUpdatedBy("system");
        notificationContext.setCreatedAt(LocalDateTime.now());
        notificationContext.setUpdatedAt(LocalDateTime.now());

        notificationContextRepo.save(notificationContext);

        // 3. Create Notification object

        Notification notification = Notification.builder()
                .notificationContext(notificationContext)
                .notificationTemplate(notificationTemplate)
                .notificationId(SystemUtility.generateId("NOTIFICATION"))
                .notificationPriority(NotificationPriority.URGENT)
                .notificationStatus(NotificationStatus.DRAFT)
                .bccUsers(new ArrayList<>())
                .ccUsers(new ArrayList<>())
                .isEmail(true)
                .isSMSDelivered(true)
                .isWhatsAppDelivered(true)
                .isSMS(false)
                .isWhatsApp(false)
                .toUsers(List.of(maintEmployee))
                .createdAt(LocalDateTime.now())
                .createdBy("system")
                .updatedAt(LocalDateTime.now())
                .updatedBy("system")
                .build();

        // Call NotificationService to send the notification

        // This send notification method will be slow - sendNotification is internally dependent on external calls
        // Externally Calling javaMailSender which will call gmail server to send email which itself take some time
        // smsService - Twillio will send smsNotification which will take time
        // Whatsappservice will call whatsappserver which will take its time

        executorService.submit(() ->{
            notificationService.sendNotification(notification);
        });


        return supplierCompany;
    }



    public SupplierCompany saveOrUpdate(SupplierCompany supplierCompany){
        return this.supplierCompanyRepo.save(supplierCompany);
    }

}
