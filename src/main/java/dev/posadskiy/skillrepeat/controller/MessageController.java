package dev.posadskiy.skillrepeat.controller;

import dev.posadskiy.skillrepeat.dto.Message;

public interface MessageController {

	void create(Message message, String userId);

}
