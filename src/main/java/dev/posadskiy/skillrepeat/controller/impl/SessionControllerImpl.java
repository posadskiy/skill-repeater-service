package dev.posadskiy.skillrepeat.controller.impl;

import dev.posadskiy.skillrepeat.controller.SessionController;
import dev.posadskiy.skillrepeat.db.SessionRepository;
import dev.posadskiy.skillrepeat.db.model.DbSession;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Optional;

public class SessionControllerImpl implements SessionController {

	public static final long SESSION_LIFE_TIME_MS = 2592000000L;
	public static final int SESSION_LIFE_TIME_S = 2592000;

	@Autowired
	SessionRepository repository;

	public DbSession getSessionById(String sessionId) {
		Optional<DbSession> byId = repository.findById(sessionId);
		return byId.orElse(null);
	}

	public boolean isSessionExist(String sessionId) {
		DbSession sessionById = getSessionById(sessionId);
		return sessionById != null;

	}

	public boolean isSessionExpired(String sessionId) {
		DbSession sessionById = getSessionById(sessionId);
		return sessionById.getTime() < System.currentTimeMillis();
	}

	public DbSession create(String sessionId, String userId) {
		return repository.save(new DbSession(sessionId, userId, System.currentTimeMillis() + SESSION_LIFE_TIME_MS));
	}
}