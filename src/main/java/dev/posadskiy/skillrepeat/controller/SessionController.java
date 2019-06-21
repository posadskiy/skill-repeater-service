package dev.posadskiy.skillrepeat.controller;

public interface SessionController {
	boolean isSessionExist(String sessionId);
	boolean isSessionExpired(String sessionId);
}