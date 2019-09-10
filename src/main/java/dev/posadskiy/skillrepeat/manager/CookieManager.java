package dev.posadskiy.skillrepeat.manager;

import org.springframework.stereotype.Component;

import javax.servlet.http.Cookie;

import static dev.posadskiy.skillrepeat.controller.impl.SessionControllerImpl.SESSION_LIFE_TIME_S;

@Component
public class CookieManager {

	public static final String SESSION_COOKIE_NAME = "SESSION_ID";
	private static final String COOKIE_DOMAIN = "localhost";
	private static final String COOKIE_PATH = "/";


	public Cookie createCookie(String sessionId) {
		Cookie cookie = new Cookie(SESSION_COOKIE_NAME, sessionId);

		cookie.setDomain(COOKIE_DOMAIN);
		cookie.setPath(COOKIE_PATH);
		cookie.setMaxAge(SESSION_LIFE_TIME_S);

		return cookie;
	}
}
