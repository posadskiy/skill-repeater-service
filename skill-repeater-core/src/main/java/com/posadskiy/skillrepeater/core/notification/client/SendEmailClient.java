package com.posadskiy.skillrepeater.core.notification.client;

import com.posadskiy.email.template.api.EmailFormDto;
import io.micronaut.http.annotation.Body;
import io.micronaut.http.annotation.Header;
import io.micronaut.http.annotation.Post;
import io.micronaut.http.client.annotation.Client;

import static io.micronaut.http.HttpHeaders.AUTHORIZATION;

@Client("http://email-template-service.local/email/template/send")
public interface SendEmailClient extends SendEmailOperations {

    @Post("base")
    void sendBaseTemplatedEmail(@Header(AUTHORIZATION) String authorization, @Body EmailFormDto emailFormDto);

}
