package dev.posadskiy.skillrepeat.rest;

import lombok.Getter;

@Getter
public class RequestWrapper {
	private Object data;
	private String userId;
	private String sessionId;

	public RequestWrapper data(Object data) {
		this.data = data;
		return this;
	}

	public RequestWrapper userId(String userId) {
		this.userId = userId;
		return this;
	}

	public RequestWrapper sessionId(String sessionId) {
		this.sessionId = sessionId;
		return this;
	}
}