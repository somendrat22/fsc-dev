package com.fsc.supply_chain_common_operations.services;

import com.fsc.supply_chain_common_operations.models.Notification;
import org.aspectj.weaver.ast.Not;

public interface NotificationHandler {
    public Notification send(Notification notification);
}
