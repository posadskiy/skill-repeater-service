package com.posadskiy.skillrepeater.core.notification.client;

import com.posadskiy.email.template.api.EmailFormDto;

public interface SendEmailOperations {
    void sendBaseTemplatedEmail(String authorization, EmailFormDto emailFormDto);
}
