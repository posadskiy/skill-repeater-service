package com.posadskiy.skillrepeater.core.notification.client;

import io.micronaut.http.annotation.Body;
import io.micronaut.http.annotation.Post;
import io.micronaut.http.client.annotation.Client;
import io.micronaut.security.authentication.AuthenticationRequest;
import io.micronaut.security.token.render.BearerAccessRefreshToken;

@Client("http://auth-service.local/")
public interface LoginClient {

    @Post("login")
    BearerAccessRefreshToken login(@Body AuthenticationRequest<String, String> authenticationRequest);

}
