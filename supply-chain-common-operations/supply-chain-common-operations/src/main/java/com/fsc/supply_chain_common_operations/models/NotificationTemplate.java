package com.fsc.supply_chain_common_operations.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "notifications_templates")
public class NotificationTemplate {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID sysId;

    @Column(nullable = false, unique = true)
    private String templateName;

    @Column(nullable = false)
    private String subjectLine;

    @Column(columnDefinition = "TEXT")
    private String emailTemplate;

    @Column(columnDefinition = "TEXT")
    private String smsTemplate;

    @Column(columnDefinition = "TEXT")
    private String whatsAppMessageTemplate;
}
