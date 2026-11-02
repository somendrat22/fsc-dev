package com.fsc.supply_chain_common_operations.exceptions;


public class InvalidCredentialsException extends RuntimeException{
    public InvalidCredentialsException(String message){
        super(message);
    }
}
