package com.fsc.supply_chain_common_operations.services;

import com.fsc.supply_chain_common_operations.enums.NotificationStatus;
import com.fsc.supply_chain_common_operations.models.Notification;
import com.fsc.supply_chain_common_operations.repositories.NotificationRepo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class NotificationService {

    private EmailHandlerImpl emailHandler;
    private NotificationRepo notificationRepo;

    @Autowired
    public NotificationService(EmailHandlerImpl emailHandler,
                               NotificationRepo notificationRepo){
        this.emailHandler = emailHandler;
        this.notificationRepo = notificationRepo;
    }

    /**
     * Whenever any service wants to send the nottification they will call
     * this sendNotification function and this function needs just notification object.
     * In whatever mode you want to send notification you just need to toggle on the flag for that notification type.
     * @param notification
     */
    public Notification sendNotification(Notification notification) {

        log.info("Notification process started");

        log.info("Saving notification");
        notificationRepo.save(notification);
        log.info("Notification saved successfully");

        if (notification.isEmail()) {
            log.info("Sending email notification");
            emailHandler.send(notification);
            log.info("Email notification sent successfully");
        }

        if (notification.isSMS()) {
            log.info("Sending SMS notification");
            notification.setSMSDelivered(true);
            // SMSService
        }

        if (notification.isWhatsApp()) {
            log.info("Sending WhatsApp notification");
            notification.setWhatsAppDelivered(true);
            // WhatsAppService
        }

        if(notification.isEmailDelivered() && notification.isSMSDelivered() && notification.isWhatsAppDelivered()){
            notification.setNotificationStatus(NotificationStatus.DELIVERED);
        }

        log.info("Notification process completed successfully");

        return notification;
    }


    public List<Notification> fetchNotificationByStatus(NotificationStatus notificationStatus){
        // We need to call repository layer which will get us all the failed notifications.
        return notificationRepo.findByNotificationStatus(notificationStatus);
    }


}
