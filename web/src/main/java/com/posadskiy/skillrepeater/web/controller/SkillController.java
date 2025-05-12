package com.posadskiy.skillrepeater.web.controller;

import com.posadskiy.skillrepeater.api.dto.SkillDto;
import com.posadskiy.skillrepeater.core.mapper.dto.SkillDtoMapper;
import com.posadskiy.skillrepeater.core.model.Skill;
import com.posadskiy.skillrepeater.core.service.SkillService;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.MutableHttpResponse;
import io.micronaut.http.annotation.*;
import io.micronaut.security.annotation.Secured;
import io.micronaut.security.rules.SecurityRule;
import io.micronaut.tracing.annotation.NewSpan;
import lombok.NoArgsConstructor;

import java.util.List;

@Secured(SecurityRule.IS_AUTHENTICATED)
@Controller("v0/skill")
@NoArgsConstructor
public class SkillController {

    private SkillService skillService;
    private SkillDtoMapper skillDtoMapper;

    public SkillController(SkillService skillService, SkillDtoMapper skillDtoMapper) {
        this.skillService = skillService;
        this.skillDtoMapper = skillDtoMapper;
    }

    @Get("get-all/{userId}")
    @NewSpan
    public List<SkillDto> getAll(@PathVariable final String userId) {
        return skillService.getAllByUser(userId)
            .stream()
            .map(skillDtoMapper::mapToDto)
            .toList();
    }

    @Post("add")
    @NewSpan
    public SkillDto add(@Body final SkillDto userDto) {
        final Skill user = skillDtoMapper.mapFromDto(userDto);

        return skillDtoMapper.mapToDto(
            skillService.addSkill(user)
        );
    }

    @Post("add-all")
    @NewSpan
    public MutableHttpResponse<Object> add(@Body final List<SkillDto> userDtos) {
        userDtos.forEach(this::add);

        return HttpResponse.ok();
    }

    @Post("edit")
    @NewSpan
    public SkillDto edit(@Body final SkillDto userDto) {
        final Skill user = skillDtoMapper.mapFromDto(userDto);

        return skillDtoMapper.mapToDto(
            skillService.editSkill(user)
        );
    }

    @Post("delete")
    @NewSpan
    public void delete(@Body final SkillDto userDto) {
        final Skill user = skillDtoMapper.mapFromDto(userDto);

        skillService.deleteSkill(user);
    }

}
