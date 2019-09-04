package dev.posadskiy.skillrepeat.db.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;
import java.util.Date;

@Data
@Document
public class DbMessage implements Serializable {

	@Id
	private String id;
	private String userId;
	private String message;
	private Date date;

}