package dev.posadskiy.skillrepeat.db.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document
public class DbResetPassword {
	@Id
	private String id;
	private String userId;
	private String hash;
	private Long time;
}
