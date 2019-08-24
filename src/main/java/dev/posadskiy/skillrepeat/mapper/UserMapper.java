package dev.posadskiy.skillrepeat.mapper;

import dev.posadskiy.skillrepeat.db.model.DbSkill;
import dev.posadskiy.skillrepeat.db.model.DbUser;
import dev.posadskiy.skillrepeat.dto.Skill;
import dev.posadskiy.skillrepeat.dto.User;
import org.mapstruct.*;

import java.util.Calendar;
import java.util.Date;
import java.util.UUID;

@Mapper
public interface UserMapper {
    @Mappings({
        @Mapping(source = "id", target = "id"),
        @Mapping(source = "name", target = "name"),
        @Mapping(target = "password", ignore = true),
        @Mapping(source = "skills", target = "skills")
    })
    User mapToDto(DbUser dbUser);

    @Mappings({
        @Mapping(source = "id", target = "id"),
        @Mapping(source = "name", target = "name"),
        @Mapping(source = "level", target = "level"),
        @Mapping(source = "lastRepeat", target = "lastRepeat")
    })
    Skill map(DbSkill skill);

    @AfterMapping
    default void map(DbSkill dbSkill, @MappingTarget Skill skill) {
        skill.setIsNeedRepeat(mapLastRepeatDateToIsNeedRepeatBoolean(dbSkill.getLastRepeat()));
    }

    @Mappings({
        @Mapping(source = "id", target = "id"),
        @Mapping(source = "name", target = "name"),
        @Mapping(source = "email", target = "email"),
        @Mapping(source = "skills", target = "skills")
    })
    DbUser mapFromDto(User user);

    @Mappings({
        @Mapping(source = "id", target = "id"),
        @Mapping(source = "name", target = "name"),
        @Mapping(source = "lastRepeat", target = "lastRepeat"),
        @Mapping(target = "level", ignore = true),
    })
    DbSkill map(Skill skill);

    @AfterMapping
    default void map(Skill skill, @MappingTarget DbSkill dbSkill) {
        dbSkill.setId(skill.getId() != null ? skill.getId() : UUID.randomUUID().toString());
        dbSkill.setLevel(skill.getLevel() != null ? skill.getLevel() : 1);
        dbSkill.setLastRepeat(mapTermRepeatStringToLastRepeatDate(skill.getTermRepeat()));
    }

    default Boolean mapLastRepeatDateToIsNeedRepeatBoolean(Date date) {
        if (date == null) return false;

        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE, -14);
        return date.before(calendar.getTime());
    }

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