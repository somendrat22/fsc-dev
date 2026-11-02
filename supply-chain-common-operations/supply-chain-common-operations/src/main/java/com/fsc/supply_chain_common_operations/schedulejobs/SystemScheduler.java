package com.fsc.supply_chain_common_operations.schedulejobs;

import com.fsc.supply_chain_common_operations.enums.NotificationStatus;
import com.fsc.supply_chain_common_operations.models.Notification;
import com.fsc.supply_chain_common_operations.services.NotificationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class SystemScheduler {

    private NotificationService notificationService;

    @Autowired
    public SystemScheduler(NotificationService notificationService){
        this.notificationService = notificationService;
    }

    // ToDO - Need to fix this bug when this schedule job is running why it is throwing error
    @Scheduled(cron = "0 */5 * * * *")
    private void processFailedAndInQueueNotification() {

        log.info("Notification scheduler started");

        // Fetch FAILED notifications
        List<Notification> failedNotifications =
                notificationService.fetchNotificationByStatus(NotificationStatus.FAILED);

        log.info("Fetched {} failed notifications", failedNotifications.size());

        // Fetch IN_QUEUE notifications
        List<Notification> combinedNotifications =
                notificationService.fetchNotificationByStatus(NotificationStatus.IN_QUEUE);

        log.info("Fetched {} in-queue notifications", combinedNotifications.size());

        combinedNotifications.addAll(failedNotifications);

        log.info("Total notifications to process: {}", combinedNotifications.size());

        for (Notification notification : combinedNotifications) {
            try {
                log.info("Processing notification");

                this.notificationService.sendNotification(notification);

                log.info("Notification processed successfully");

            } catch (Exception e) {
                log.error("Failed to process notification. Error={}",
                        e.getMessage(), e);
            }
        }

        log.info("Notification scheduler completed");
    }
}
