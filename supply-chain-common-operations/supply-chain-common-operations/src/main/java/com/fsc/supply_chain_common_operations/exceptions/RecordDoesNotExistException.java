package com.fsc.supply_chain_common_operations.exceptions;

public class RecordDoesNotExistException extends RuntimeException {
    public RecordDoesNotExistException(String message){
        super(message);
    }
}
