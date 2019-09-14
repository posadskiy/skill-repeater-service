package dev.posadskiy.skillrepeat.db.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;

@Data
@Document
public class DbTelegramAuthCode implements Serializable {
	@Id
	private String id;
	private String userId;
	private String hash;
	private Long time;
}
