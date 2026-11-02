package com.fsc.supply_chain_common_operations.exceptions;

public class RetryAttemptsFailedException extends RuntimeException{
    public RetryAttemptsFailedException(String message){
        super(message);
    }
}
