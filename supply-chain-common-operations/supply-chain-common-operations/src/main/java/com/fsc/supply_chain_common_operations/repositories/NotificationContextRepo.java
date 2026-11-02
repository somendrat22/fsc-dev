package com.fsc.supply_chain_common_operations.repositories;

import com.fsc.supply_chain_common_operations.models.NotificationContext;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface NotificationContextRepo extends JpaRepository<NotificationContext, UUID> {
}
