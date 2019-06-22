package dev.posadskiy.skillrepeat.db.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document
public class DbSession {
	@Id
	private String id;
	private String userId;
	private long time;

	public DbSession(String userId, long time) {
		this.userId = userId;
		this.time = time;
	}
}