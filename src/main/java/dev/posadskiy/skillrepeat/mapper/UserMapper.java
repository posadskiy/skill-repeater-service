package dev.posadskiy.skillrepeat.mapper;

import dev.posadskiy.skillrepeat.db.model.DbSkill;
import dev.posadskiy.skillrepeat.db.model.DbUser;
import dev.posadskiy.skillrepeat.dto.Skill;
import dev.posadskiy.skillrepeat.dto.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;

@Mapper
public interface UserMapper {

	SkillMapper skillMapper = Mappers.getMapper(SkillMapper.class);

	@Mappings({
		@Mapping(source = "id", target = "id"),
		@Mapping(source = "name", target = "name"),
		@Mapping(source = "email", target = "email"),
		@Mapping(source = "period", target = "period"),
		@Mapping(source = "time", target = "time"),
		@Mapping(source = "skills", target = "skills"),
		@Mapping(source = "isAgreeGetEmails", target = "isAgreeGetEmails"),
		@Mapping(target = "password", ignore = true),
	})
	User mapToDto(DbUser dbUser);

	default Skill map(DbSkill skill) {
		return skillMapper.map(skill);
	}

	@Mappings({
		@Mapping(source = "id", target = "id"),
		@Mapping(source = "name", target = "name"),
		@Mapping(source = "email", target = "email", qualifiedByName = "emailQualifier"),
		@Mapping(source = "password", target = "password"),
		@Mapping(source = "skills", target = "skills"),
		@Mapping(source = "period", target = "period"),
		@Mapping(source = "time", target = "time"),
		@Mapping(source = "isAgreeGetEmails", target = "isAgreeGetEmails"),
		@Mapping(target = "isConfirmedEmail", ignore = true),
		@Mapping(target = "roles", ignore = true),
		@Mapping(target = "registrationDate", ignore = true)
	})
	DbUser mapFromDto(User user);

	default DbSkill map(Skill skill) {
		return skillMapper.map(skill);
	}

	@Named("emailQualifier")
	default String emailQualifier(String email) {
		return email.toLowerCase();
	}

}