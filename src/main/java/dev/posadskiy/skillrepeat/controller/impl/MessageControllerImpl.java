package dev.posadskiy.skillrepeat.controller.impl;

import dev.posadskiy.skillrepeat.controller.MessageController;
import dev.posadskiy.skillrepeat.db.MessageRepository;
import dev.posadskiy.skillrepeat.db.model.DbMessage;
import dev.posadskiy.skillrepeat.dto.Message;
import org.springframework.beans.factory.annotation.Autowired;

public class MessageControllerImpl implements MessageController {

	@Autowired
	private MessageRepository repository;

	@Override
	public void create(Message message, String userId) {
		DbMessage dbMessage = new DbMessage();

		dbMessage.setUserId(userId);
		dbMessage.setMessage(message.getMessage());

		repository.save(dbMessage);
	}
}
