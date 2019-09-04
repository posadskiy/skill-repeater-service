package dev.posadskiy.skillrepeat.mapper;

import dev.posadskiy.skillrepeat.db.model.DbUser;
import dev.posadskiy.skillrepeat.dto.Auth;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.Named;

@Mapper
public interface AuthMapper {

    @Mappings({
        @Mapping(source = "login", target = "name"),
        @Mapping(source = "password", target = "password"),
        @Mapping(source = "email", target = "email", qualifiedByName = "emailQualifier"),
    })
    DbUser mapFromDto(Auth auth);

    @Named("emailQualifier")
    default String emailQualifier(String email) {
        return email.toLowerCase();
    }
}