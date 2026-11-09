package com.fsc.supply_chain_common_operations.services;

import com.fsc.supply_chain_common_operations.constants.NotificationTemplateConstant;
import com.fsc.supply_chain_common_operations.dtos.requests.CommonValidationDto;
import com.fsc.supply_chain_common_operations.dtos.requests.InviteEmployeeRequest;
import com.fsc.supply_chain_common_operations.enums.CompanyType;
import com.fsc.supply_chain_common_operations.enums.NotificationPriority;
import com.fsc.supply_chain_common_operations.models.*;
import com.fsc.supply_chain_common_operations.repositories.EmployeeRepo;
import com.fsc.supply_chain_common_operations.transformers.EntityTransformer;
import com.fsc.supply_chain_common_operations.utilities.CommonUtility;
import com.fsc.supply_chain_common_operations.utilities.SystemUtility;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;


@Slf4j
@Service
public class EmployeeService implements EntityService<Employee> {

    private RoleService roleService;
    private EmployeeRepo employeeRepo;
    private CommonUtility commonUtility;
    private NotificationService notificationService;

    @Autowired
    public EmployeeService(RoleService roleService,
                           EmployeeRepo employeeRepo,
                           CommonUtility commonUtility,
                           NotificationService notificationService){
        this.roleService = roleService;
        this.employeeRepo = employeeRepo;
        this.commonUtility = commonUtility;
        this.notificationService = notificationService;
    }

    public Employee getEmployeeByEmail(String email){
        return this.employeeRepo.findByWorkEmail(email);
    }

    public Employee saveOrUpdate(Employee employee){
        return this.employeeRepo.save(employee);
    }

    public Employee createMaintUserForCompany(Company company, CompanyType companyType) {

        log.info("Starting main user creation. companyId={}, companyType={}",
                company.getSysId(), companyType);

        // Create main/admin role for the company
        log.info("Creating main role. companyId={}, companyType={}",
                company.getSysId(), companyType);

        Role maintRole = roleService.createMaintRole(company, companyType);

        log.info("Main role created successfully. companyId={}, roleId={}",
                company.getSysId(), maintRole.getSysId());

        // Generate password and map company details to first employee
        String generatedPassword = SystemUtility.generatePassword(10);

        Employee maintEmployee =
                EntityTransformer.mapCompanyToEmployee(company, generatedPassword);

        log.debug("Company mapped to main employee successfully. companyId={}",
                company.getSysId());

        // Assign main/admin role
        maintEmployee.setRoles(List.of(maintRole));

        log.debug("Main role assigned to employee. companyId={}, roleId={}",
                company.getSysId(), maintRole.getSysId());

        // Save employee
        maintEmployee = this.saveOrUpdate(maintEmployee);

        log.info("Main employee created successfully. companyId={}, employeeId={}",
                company.getSysId(), maintEmployee.getSysId());

        // TODO: Send account creation notification to employee.
        log.info("Main user creation completed successfully. companyId={}, employeeId={}",
                company.getSysId(), maintEmployee.getSysId());

        return maintEmployee;
    }

    public Employee getEmployeeBySysId(UUID sysId){
        return employeeRepo.findById(sysId).orElse(null);
    }

    public Employee inviteEmployee(
            InviteEmployeeRequest inviteEmployeeRequest,
            Employee invitorEmp
    ){
      commonUtility.validateBasicChecks(new CommonValidationDto(inviteEmployeeRequest.getRoleSysIds(), invitorEmp.getSysId(),inviteEmployeeRequest.getCompanySysId()));
      // Map the request to the model.
      Employee manager =  this.getEmployeeBySysId(inviteEmployeeRequest.getManagerSysId());
      if(manager == null){
          log.warn("Manager details not passed");
      }
      List<Role> roles = roleService.fetchRolesBySysId(inviteEmployeeRequest.getRoleSysIds());
      Employee employee = EntityTransformer.mapEmpRequestToEmpModel(inviteEmployeeRequest, invitorEmp.getCompany(), invitorEmp, manager,roles);
      employee = this.saveOrUpdate(employee);
        HashMap<String, String> emailMap = new HashMap<>();

// User Details
        emailMap.put("maintUserName", employee.getFirstName());
        emailMap.put("maintUserEmail", employee.getEmailAddress());

// Company Details
        emailMap.put("companyName", employee.getCompany().getCompanyName());

// Employee Details
        emailMap.put("employeeCode", employee.getEmployeeCode());
        emailMap.put("designation", employee.getDesignation());
        emailMap.put("department", employee.getDepartment());

        emailMap.put("managerName",
                employee.getManager() != null
                        ? employee.getManager().getFirstName()
                        : "Not Assigned");

        emailMap.put("employmentType",
                employee.getEmploymentType() != null
                        ? employee.getEmploymentType().name()
                        : "Not Specified");

        emailMap.put("joiningDate",
                employee.getJoiningDate() != null
                        ? employee.getJoiningDate().toString()
                        : "Not Specified");

        emailMap.put("workEmail", employee.getWorkEmail());
        emailMap.put("workPhone", employee.getWorkPhone());
        emailMap.put("officeLocation", employee.getOfficeLocation());

// Application Details
        emailMap.put("applicationUrl", "https://google.com");
        emailMap.put("currentYear", String.valueOf(LocalDate.now().getYear()));

        HashMap<String, String> subjectMap = new HashMap<>();

        subjectMap.put("companyName",
                employee.getCompany().getCompanyName());

        NotificationContext notificationContext = new NotificationContext(
                SystemUtility.generateId("NOTIFICATION-CONTEXT"),
                subjectMap,
                emailMap,
                new HashMap<>(),
                new HashMap<>()
        );

        NotificationTemplate notificationTemplate = notificationService.fetchTemplateById(NotificationTemplateConstant.COMPANY_REGISTRATION_TEMPLATE_ID);
        Notification inviteNotification = new Notification();
        inviteNotification.setBccUsers(List.of(invitorEmp));
        inviteNotification.setNotificationContext(notificationContext);
        inviteNotification.setEmail(true);
        inviteNotification.setNotificationPriority(NotificationPriority.URGENT);
        inviteNotification.setToUsers(List.of(employee));
        inviteNotification.setCreatedAt(LocalDateTime.now());
        inviteNotification.setUpdatedAt(LocalDateTime.now());
        inviteNotification.setCreatedBy(invitorEmp.getEmailAddress());
        inviteNotification.setUpdatedBy(invitorEmp.getEmailAddress());
        inviteNotification.setNotificationTemplate(notificationTemplate);

        notificationService.sendNotification(inviteNotification);
      return employee;
    }

}
