package com.fsc.supply_chain_common_operations.controllers;


import com.fsc.supply_chain_common_operations.dtos.requests.CreateRoleRequest;
import com.fsc.supply_chain_common_operations.dtos.requests.EmployeeLoginRequest;
import com.fsc.supply_chain_common_operations.dtos.responses.EmployeeLoginResponse;
import com.fsc.supply_chain_common_operations.dtos.responses.ErrorResponse;
import com.fsc.supply_chain_common_operations.exceptions.InvalidCredentialsException;
import com.fsc.supply_chain_common_operations.exceptions.UnAuthorizedException;
import com.fsc.supply_chain_common_operations.models.Employee;
import com.fsc.supply_chain_common_operations.models.Role;
import com.fsc.supply_chain_common_operations.services.AuthService;
import com.fsc.supply_chain_common_operations.services.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/common")
public class CommonController {

    private AuthService authService;
    private RoleService roleService;

    @Autowired
    public CommonController(AuthService authService,
                            RoleService roleService){
        this.authService = authService;
        this.roleService = roleService;
    }

    @PostMapping("/emp/login")
    public ResponseEntity loginEmployee(@RequestBody EmployeeLoginRequest employeeLoginRequest){
        // EmployeeService -> to verify credentials if credentials are correct we will call JwtUtility to generate token for this user.
        try{
            // 2XX
            EmployeeLoginResponse employeeLoginResponse = authService.authenticateUser(employeeLoginRequest);
            return new ResponseEntity(employeeLoginResponse, HttpStatus.OK);
        }catch (InvalidCredentialsException e){
            // 4XX
            ErrorResponse errorResponse = new ErrorResponse(HttpStatus.FORBIDDEN, e.getMessage());
            return new ResponseEntity(errorResponse, HttpStatus.FORBIDDEN);
        }catch (Exception e){
            // 5XX
            ErrorResponse errorResponse = new ErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
            return new ResponseEntity(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    @PostMapping("/role/create")
    public ResponseEntity createRole(
            @RequestBody CreateRoleRequest createRoleRequest,
            @RequestHeader String token
            ){
        // We are coming at this controller when user is authenticated.
        // We need to verify that user is having acess to create role or not.
        try{
            Employee employee = authService.isUserHavingAccess(token, "CREATE_ROLE");
            Role role = roleService.createRole(createRoleRequest, employee);
            return new ResponseEntity<>(role, HttpStatus.CREATED);
            // We need to call roleService
        }catch (UnAuthorizedException e){
            // 401
            ErrorResponse errorResponse = new ErrorResponse(HttpStatus.UNAUTHORIZED, e.getMessage());
            return new ResponseEntity(errorResponse, HttpStatus.UNAUTHORIZED);
        }catch (Exception e){
            // 5XX
            ErrorResponse errorResponse = new ErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
            return new ResponseEntity(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
        }



    }
}
