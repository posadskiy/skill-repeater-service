package dev.posadskiy.skillrepeat.controller.security;

import com.posadskiy.restsecurity.controller.SessionSecurityController;
import com.posadskiy.restsecurity.exception.SessionDoesNotExistException;
import dev.posadskiy.skillrepeat.db.SessionRepository;
import dev.posadskiy.skillrepeat.db.model.DbSession;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Optional;

public class SessionSecurityControllerImpl implements SessionSecurityController {
	
	@Autowired
	private SessionRepository sessionRepository;

	public DbSession getSessionById(String sessionId) {
		Optional<DbSession> foundSession = sessionRepository.findById(sessionId);
		if (!foundSession.isPresent()) throw new SessionDoesNotExistException();
		
		return foundSession.get();
	}

	@Override
	public boolean isSessionExist(String sessionId) {
		DbSession sessionById = getSessionById(sessionId);
		return sessionById != null;
	}

	@Override
	public boolean isSessionExpired(String sessionId) {
		DbSession sessionById = getSessionById(sessionId);
		return sessionById.getTime() < System.currentTimeMillis();
	}

	@Override
	public String getUserIdBySessionId(String sessionId) {
		final DbSession foundSession = getSessionById(sessionId);
		return  foundSession.getUserId();
	}
}
