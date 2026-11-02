package com.fsc.supply_chain_common_operations.services;

import com.fsc.supply_chain_common_operations.enums.NotificationPriority;
import com.fsc.supply_chain_common_operations.enums.NotificationStatus;
import com.fsc.supply_chain_common_operations.models.Notification;
import com.fsc.supply_chain_common_operations.models.User;
import com.fsc.supply_chain_common_operations.repositories.NotificationRepo;
import com.fsc.supply_chain_common_operations.utilities.SystemUtility;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
public class EmailHandlerImpl implements NotificationHandler {

    private JavaMailSender javaMailSender;
    private NotificationRepo notificationRepo;

    @Autowired
    public EmailHandlerImpl(JavaMailSender javaMailSender,
                            NotificationRepo notificationRepo){
        this.javaMailSender = javaMailSender;
        this.notificationRepo = notificationRepo;
    }

    /**
     * To Send emails we require library - JavaMailSender
     * @param notification
     * @return
     */
    @Override
    public Notification send(Notification notification) {

        log.info("Email notification processing started");

        try {
            if (notification.getNotificationPriority().equals(NotificationPriority.URGENT)
                    && notification.getNotificationStatus().equals(NotificationStatus.DRAFT)) {

                log.info("Processing URGENT email notification");

                this.sendEmail(notification);
                notification.setEmailFailed(false);
                notification.setEmailDelivered(true);
                log.info("URGENT email notification sent successfully");

            } else if (notification.getNotificationPriority().equals(NotificationPriority.NORMAL)
                    && notification.getNotificationStatus().equals(NotificationStatus.IN_QUEUE)) {

                log.info("Processing queued NORMAL email notification");

                this.sendEmail(notification);
                notification.setEmailFailed(false);
                notification.setEmailDelivered(true);
                log.info("Queued email notification sent successfully");

            } else if (notification.isEmailFailed()) {

                log.info("Retrying previously failed email notification");

                this.sendEmail(notification);
                notification.setEmailFailed(false);
                notification.setEmailDelivered(true);
                log.info("Email notification retry successful");

            } else {

                log.info("NORMAL notification received. Moving notification to IN_QUEUE");

                notification.setNotificationStatus(NotificationStatus.IN_QUEUE);
            }

        } catch (Exception e) {

            log.error("Unable to send email notification. Error: {}", e.getMessage(), e);

            notification.setFailureMessage(
                    notification.getFailureMessage() + " [EMAIL] - " + e.getMessage()
            );
            notification.setEmailFailed(true);
            notification.setNotificationStatus(NotificationStatus.FAILED);
        }

        log.info("Saving notification with status={}",
                notification.getNotificationStatus());

        notification.setUpdatedAt(LocalDateTime.now());
        notification.setUpdatedBy("system");

        this.notificationRepo.save(notification);

        log.info("Email notification processing completed");

        return notification;
    }


    private void sendEmail(Notification notification) throws MessagingException {

        log.info("Preparing email notification");

        // To Users
        List<String> toUsers = notification.getToUsers()
                .stream()
                .map(User::getEmailAddress)
                .toList();

        // CC Users
        List<String> ccUsers = notification.getCcUsers()
                .stream()
                .map(User::getEmailAddress)
                .toList();

        // BCC Users
        List<String> bccUsers = notification.getBccUsers()
                .stream()
                .map(User::getEmailAddress)
                .toList();

        log.info("Email recipients prepared. To={}, CC={}, BCC={}",
                toUsers.size(), ccUsers.size(), bccUsers.size());

        // Email Content
        String emailBody = notification.getNotificationTemplate().getEmailTemplate();
        String subjectLine = notification.getNotificationTemplate().getSubjectLine();

        emailBody = SystemUtility.replaceTemplateVariableWithValues(
                emailBody,
                notification.getNotificationContext().getEmailMap()
        );

        subjectLine = SystemUtility.replaceTemplateVariableWithValues(
                subjectLine,
                notification.getNotificationContext().getSubjectLineMap()
        );

        log.info("Email template variables replaced successfully");

        MimeMessage mimeMessage = javaMailSender.createMimeMessage();

        MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage);

        mimeMessageHelper.setTo(toUsers.toArray(new String[0]));
        mimeMessageHelper.setCc(ccUsers.toArray(new String[0]));
        mimeMessageHelper.setBcc(bccUsers.toArray(new String[0]));
        mimeMessageHelper.setSubject(subjectLine);
        mimeMessageHelper.setText(emailBody, true);

        log.info("Sending email notification");

        RetryService.retry(() -> {
            javaMailSender.send(mimeMessage);
        });

        log.info("Email notification sent successfully");
    }



}
