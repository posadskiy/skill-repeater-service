package com.posadskiy.skillrepeater.core.notification.service;

import com.posadskiy.email.template.api.*;
import com.posadskiy.skillrepeater.core.notification.client.LoginClient;
import com.posadskiy.skillrepeater.core.notification.client.SendEmailOperations;
import io.micronaut.security.authentication.UsernamePasswordCredentials;
import jakarta.inject.Singleton;

@Singleton
public class NotificationService {
    private final SendEmailOperations sendEmailOperations;
    private final LoginClient loginClient;

    public NotificationService(SendEmailOperations sendEmailOperations, LoginClient loginClient) {
        this.sendEmailOperations = sendEmailOperations;
        this.loginClient = loginClient;
    }

    public void sendNotification(String userId, String names) {
        var authenticationResponse = loginClient.login(new UsernamePasswordCredentials("system", "any"));
        sendEmailOperations.sendBaseTemplatedEmail("Bearer " + authenticationResponse.getAccessToken(), new EmailFormDto(new Email("Time to Repeat"), new Recipient(userId), new Content("skill-repeater service", names + " is waiting to be repeated", "Find a time to train your skill", new Button("Repeated!", "link"))));
    }
}
