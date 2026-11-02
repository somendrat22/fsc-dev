package com.fsc.supply_chain_common_operations.services;

import com.fsc.supply_chain_common_operations.models.Notification;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class SMSHandlerImpl implements NotificationHandler{
    @Override
    public Notification send(Notification notification) {
        return null;
    }
}
