package dev.posadskiy.skillrepeat.controller;

import dev.posadskiy.skillrepeat.db.model.DbSession;

public interface SessionController {
	boolean isSessionExist(String sessionId);
	boolean isSessionExpired(String sessionId);
	DbSession getSessionById(String sessionId);
	DbSession create(String sessionId, String userId);
}