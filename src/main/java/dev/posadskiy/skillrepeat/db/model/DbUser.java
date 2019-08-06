package dev.posadskiy.skillrepeat.db.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;
import java.util.List;

@Data
@Document
public class DbUser implements Serializable {

	@Id
	private String id;
	private String name;
	private String email;
	private String password;
	private Integer period;
	private String time;
	private List<DbSkill> skills;
	private List<String> roles;
	private Long registrationDate;
	private Boolean isAgreeGetEmails;

}
