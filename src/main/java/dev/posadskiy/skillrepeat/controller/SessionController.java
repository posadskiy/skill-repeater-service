package dev.posadskiy.skillrepeat.controller;

import dev.posadskiy.skillrepeat.db.model.DbSession;

public interface SessionController {
	DbSession create(String sessionId, String userId);
}
