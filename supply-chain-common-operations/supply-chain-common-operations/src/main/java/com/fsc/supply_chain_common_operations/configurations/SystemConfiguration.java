package com.fsc.supply_chain_common_operations.configurations;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import java.util.Properties;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Slf4j
@Configuration
public class SystemConfiguration {

    /**
     * Our api will use this email address to sending out the notification.
     */
    //TODO - Get this credentials from .env file.
    public String apiEmailAddress = "6206472735rahul@gmail.com";


    public String apiEmailPassword = "ceffelhhjfderwei";


    /**
     * This function returns the object of JavaMailSender
     * @return
     */
    @Bean
    public JavaMailSender createJavaMailSender(){
        JavaMailSenderImpl javaMailSender = new JavaMailSenderImpl();
        Properties mailProperties = new Properties();
        // As our api is going to use email address for sending out the email
        // For example our api is using gmail email address
        // Now our api wants to send the registration email so, our api will tell gmail server hey send this email for me onbehalf of my email address
        mailProperties.put("mail.smtp.auth", true);
        mailProperties.put("mail.smtp.starttls.enable", true);
        javaMailSender.setJavaMailProperties(mailProperties);
        javaMailSender.setHost("smtp.gmail.com");
        javaMailSender.setPort(587);
        // Here whatever email address will be used by our api to sending out the emails. Here we are setting emailId and password of that particular emailId
        javaMailSender.setUsername(apiEmailAddress);
        javaMailSender.setPassword(apiEmailPassword);
        return javaMailSender;
    }

    @Bean
    public ExecutorService createExecutorService() {
        return Executors.newFixedThreadPool(10);
    }

}
