package com.fsc.supply_chain_common_operations.services;

import com.fsc.supply_chain_common_operations.dtos.requests.EmployeeLoginRequest;
import com.fsc.supply_chain_common_operations.dtos.responses.EmployeeLoginResponse;
import com.fsc.supply_chain_common_operations.exceptions.InvalidCredentialsException;
import com.fsc.supply_chain_common_operations.exceptions.UnAuthorizedException;
import com.fsc.supply_chain_common_operations.models.Employee;
import com.fsc.supply_chain_common_operations.models.Operation;
import com.fsc.supply_chain_common_operations.models.Role;
import com.fsc.supply_chain_common_operations.utilities.JwtUtility;
import io.jsonwebtoken.Claims;
import org.springframework.beans.InvalidPropertyException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;

@Service
public class AuthService {

    private EmployeeService employeeService;
    private JwtUtility jwtUtility;
    private RoleService roleService;

    @Autowired
    public AuthService(EmployeeService employeeService,
                       JwtUtility jwtUtility,
                       RoleService roleService){
        this.employeeService = employeeService;
        this.jwtUtility = jwtUtility;
        this.roleService = roleService;
    }

    public EmployeeLoginResponse authenticateUser(EmployeeLoginRequest employeeLoginRequest){
        String email = employeeLoginRequest.getEmail();
        String password = employeeLoginRequest.getPassword();
        Employee employee = employeeService.getEmployeeByEmail(email);
        if(employee == null || !employee.getPasswordHash().equals(password)){
            throw new InvalidCredentialsException("Wrong email or password entered");
        }
        employee.setLastLoginAt(Instant.now());
        employeeService.saveOrUpdate(employee);
        String token = jwtUtility.generateJwtToken(employee);
        return new EmployeeLoginResponse(token);
    }

    public Employee isUserHavingAccess(
            String userToken,
            String operationName
    ){
        Claims claims = jwtUtility.extractAllClaims(userToken);
        String userEmail = claims.get("email", String.class);
        Employee employee = employeeService.getEmployeeByEmail(userEmail);
        List<String> roleNames = claims.get("roles", List.class);
        for(String roleName : roleNames){
            // We will call roleService for getting the role object from role table.
            Role role = roleService.fetchRoleByName(roleName);
            // H.W. - You should have check that roleName should exist if it is not existing throw exceptioon
            if(isRoleCanPerformOperation(role, operationName)){
                return employee;
            }
            // We need to check every role of the user and we will check is this role can perform the operation
        }
        throw new UnAuthorizedException("User roles can not perform this operation.");
    }

    private boolean isRoleCanPerformOperation(Role role, String operationName){
        List<Operation> operations = role.getOperations();
        for(Operation operation : operations){
            if(operation.getOperationName().equals(operationName)){
                return true;
            }
        }
        return false;
    }

}
