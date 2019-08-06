package dev.posadskiy.skillrepeat.mapper;

import dev.posadskiy.skillrepeat.db.model.DbUser;
import dev.posadskiy.skillrepeat.dto.Auth;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

@Mapper
public interface AuthMapper {

    @Mappings({
        @Mapping(source = "login", target = "name"),
        @Mapping(source = "password", target = "password"),
        @Mapping(source = "email", target = "email"),
    })
    DbUser mapFromDto(Auth auth);
}