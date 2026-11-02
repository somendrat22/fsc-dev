package com.slo.supplier_lifecycle_operations;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableScheduling;

@ComponentScan({
		"com.slo.supplier_lifecycle_operations",
		"com.fsc.supply_chain_common_operations"
})
@EnableScheduling
@SpringBootApplication
public class SupplierLifecycleOperationsApplication {

	public static void main(String[] args) {
		SpringApplication.run(SupplierLifecycleOperationsApplication.class, args);
	}

}
