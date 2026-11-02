package com.fsc.supply_chain_common_operations.services;

import com.fsc.supply_chain_common_operations.exceptions.RetryAttemptsFailedException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class RetryService {

    /**
     * This function is going to expect for the impelmentation
     * In this function's arguments i am going to pass another function defination as the argument
     */
    public static void retry(Runnable operation) {

        log.info("Retry operation started");

        for (int i = 1; i <= 3; i++) {
            try {
                log.info("Executing operation. Attempt={}", i);

                operation.run();

                log.info("Operation completed successfully on attempt={}", i);
                return;


            } catch (Exception e) {
                log.error("Operation failed on attempt={}. Error={}", i, e.getMessage(), e);
            }
        }

        log.error("Operation failed after all retry attempts");

        throw new RetryAttemptsFailedException("All attempts exhausted");
    }

}
