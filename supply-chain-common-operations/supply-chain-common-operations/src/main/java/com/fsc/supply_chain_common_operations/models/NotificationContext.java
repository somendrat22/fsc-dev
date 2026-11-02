package com.fsc.supply_chain_common_operations.models;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.HashMap;

@SuperBuilder
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "notification_contexts")
@Entity
public class NotificationContext extends GlobalRecord{
    private String contextId;
    private HashMap<String, String> subjectLineMap;
    private HashMap<String, String> emailMap;
    private HashMap<String, String> smsMap;
    private HashMap<String, String> whatsAppMap;
}
