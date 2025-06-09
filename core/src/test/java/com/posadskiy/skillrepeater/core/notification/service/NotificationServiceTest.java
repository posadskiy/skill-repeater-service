package com.posadskiy.skillrepeater.core.notification.service;

import com.posadskiy.email.template.api.EmailFormDto;
import com.posadskiy.skillrepeater.core.notification.client.LoginClient;
import com.posadskiy.skillrepeater.core.notification.client.SendEmailOperations;
import io.micronaut.security.authentication.UsernamePasswordCredentials;
import io.micronaut.security.token.render.BearerAccessRefreshToken;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class NotificationServiceTest {

    @Mock
    private SendEmailOperations sendEmailOperations;

    @Mock
    private LoginClient loginClient;

    private NotificationService notificationService;

    private String testUserId;
    private String testSkillNames;
    private String testToken;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        notificationService = new NotificationService(sendEmailOperations, loginClient);

        // Setup test data
        testUserId = "test-user";
        testSkillNames = "Java, Spring";
        testToken = "test-token";

        // Setup mock behavior
        BearerAccessRefreshToken token = mock(BearerAccessRefreshToken.class);
        when(token.getAccessToken()).thenReturn(testToken);
        when(loginClient.login(any(UsernamePasswordCredentials.class))).thenReturn(token);
    }

    @Test
    void sendNotification_ShouldSendEmail() {
        // Act
        notificationService.sendNotification(testUserId, testSkillNames);

        // Assert
        verify(loginClient).login(any(UsernamePasswordCredentials.class));
        verify(sendEmailOperations).sendBaseTemplatedEmail(
            eq("Bearer " + testToken),
            any(EmailFormDto.class)
        );
    }
} 