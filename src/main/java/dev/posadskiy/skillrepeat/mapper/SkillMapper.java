package dev.posadskiy.skillrepeat.mapper;

import dev.posadskiy.skillrepeat.db.model.DbSkill;
import dev.posadskiy.skillrepeat.dto.Skill;
import org.apache.commons.lang3.RandomStringUtils;
import org.mapstruct.*;

import java.util.Calendar;
import java.util.Date;

@Mapper
public interface SkillMapper {

	@Mappings({
		@Mapping(source = "id", target = "id"),
		@Mapping(source = "name", target = "name"),
		@Mapping(source = "level", target = "level"),
		@Mapping(source = "lastRepeat", target = "lastRepeat"),
		@Mapping(source = "lastRepeat", target = "isNeedRepeat", qualifiedByName = "lastRepeatToIsNeedRepeat"),
		@Mapping(target = "termRepeat", ignore = true)
	})
	Skill map(DbSkill skill);

	@Mappings({
		@Mapping(source = "id", target = "id", qualifiedByName = "idToDbId"),
		@Mapping(source = "name", target = "name"),
		@Mapping(source = "period", target = "period"),
		@Mapping(source = "time", target = "time"),
		@Mapping(source = "level", target = "level", qualifiedByName = "level"),
		@Mapping(source = "termRepeat", target = "lastRepeat", qualifiedByName = "termRepeatStringToLastRepeatDate"),
		@Mapping(target = "lastNotification", ignore = true),
	})
	DbSkill map(Skill skill);

	@Named("lastRepeatToIsNeedRepeat")
	default Boolean mapLastRepeatDateToIsNeedRepeatBoolean(Date date) {
		if (date == null) return false;

		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.DATE, -14);
		return date.before(calendar.getTime());
	}

	@Named("idToDbId")
	default String mapIdToDbId(String id) {
		return id != null ? id : RandomStringUtils.randomAlphabetic(10);
	}

	@Named("level")
	default Integer mapLevel(Integer level) {
		return level != null ? level : 1;
	}

	@Named("termRepeatStringToLastRepeatDate")
	default Date mapTermRepeatStringToLastRepeatDate(String term) {

		if (term == null) return subDateToCalendar(365);

		switch (term) {
			case "0":
				return subDateToCalendar(0);
			case "1":
				return subDateToCalendar(1);
			case "2":
				return subDateToCalendar(7);
			case "3":
				return subDateToCalendar(14);
			case "4":
				return subDateToCalendar(30);
			default:
				break;
		}

		return subDateToCalendar(365);
	}

	default Date subDateToCalendar(int days) {
		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.DATE, -days);
		return calendar.getTime();
	}
}
