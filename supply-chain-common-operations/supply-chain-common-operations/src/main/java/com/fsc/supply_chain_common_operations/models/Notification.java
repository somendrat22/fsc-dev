package com.fsc.supply_chain_common_operations.models;

import com.fsc.supply_chain_common_operations.enums.NotificationPriority;
import com.fsc.supply_chain_common_operations.enums.NotificationStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.HashMap;
import java.util.List;

@SuperBuilder
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "notifications")
public class Notification extends GlobalRecord{
    @Column(unique = true, nullable = false)
    private String notificationId;
    @ManyToMany
    private List<User> toUsers;
    @ManyToMany
    private List<User> ccUsers;
    @ManyToMany
    private List<User> bccUsers;
    @ManyToOne
    private NotificationTemplate notificationTemplate;
    private boolean isSMS;
    private boolean isWhatsApp;
    private boolean isEmail;
    private boolean isEmailFailed;
    private boolean isSMSFailed;
    private boolean isWhatsAppFailed;
    private boolean isSMSDelivered;
    private boolean isWhatsAppDelivered;
    private boolean isEmailDelivered;
    private String failureMessage;
    @OneToOne
    private NotificationContext notificationContext;
    @Enumerated(EnumType.STRING)
    private NotificationPriority notificationPriority;
    @Enumerated(EnumType.STRING)
    private NotificationStatus notificationStatus;
}
