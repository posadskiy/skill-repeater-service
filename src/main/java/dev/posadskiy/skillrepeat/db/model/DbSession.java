package dev.posadskiy.skillrepeat.db.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document
public class DbSession {
	@Id
	private String id;
	private long time;

	public DbSession(long time) {
		this.time = time;
	}
}