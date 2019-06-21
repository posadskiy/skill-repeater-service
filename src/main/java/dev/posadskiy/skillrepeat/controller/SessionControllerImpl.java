package dev.posadskiy.skillrepeat.controller;

import dev.posadskiy.skillrepeat.db.SessionRepository;
import dev.posadskiy.skillrepeat.db.model.DbSession;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Optional;

public class SessionControllerImpl implements SessionController {

	@Autowired
	SessionRepository repository;

	private DbSession getSessionById(String sessionId) {
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
}