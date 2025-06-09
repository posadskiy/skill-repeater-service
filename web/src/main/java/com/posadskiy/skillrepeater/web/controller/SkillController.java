package com.posadskiy.skillrepeater.web.controller;

import com.posadskiy.skillrepeater.api.dto.SkillDto;
import com.posadskiy.skillrepeater.core.mapper.dto.SkillDtoMapper;
import com.posadskiy.skillrepeater.core.model.Skill;
import com.posadskiy.skillrepeater.core.service.SkillService;
import io.micronaut.http.annotation.*;
import io.micronaut.security.annotation.Secured;
import io.micronaut.security.rules.SecurityRule;
import io.micronaut.tracing.annotation.NewSpan;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.NoArgsConstructor;

import java.util.List;

@Secured(SecurityRule.IS_AUTHENTICATED)
@Controller("v0/skill")
@NoArgsConstructor
@Tag(name = "Skill Management", description = "APIs for managing user skills")
public class SkillController {

    private SkillService skillService;
    private SkillDtoMapper skillDtoMapper;

    public SkillController(SkillService skillService, SkillDtoMapper skillDtoMapper) {
        this.skillService = skillService;
        this.skillDtoMapper = skillDtoMapper;
    }

    @Get("get/{id}")
    @NewSpan
    @Operation(
        summary = "Get skill by ID",
        description = "Retrieves a specific skill by its unique identifier"
    )
    @ApiResponse(
        responseCode = "200",
        description = "Successfully retrieved skill",
        content = @Content(mediaType = "application/json", schema = @Schema(implementation = SkillDto.class))
    )
    @ApiResponse(
        responseCode = "404",
        description = "Skill not found"
    )
    public SkillDto getById(
        @Parameter(description = "ID of the skill to retrieve", required = true, example = "123e4567-e89b-12d3-a456-426614174000")
        @PathVariable final String id
    ) {
        return skillDtoMapper.mapToDto(
            skillService.get(id)
        );
    }

    @Get("get-all/{userId}")
    @NewSpan
    @Operation(
        summary = "Get all skills for a user",
        description = "Retrieves a list of all skills associated with the specified user ID"
    )
    @ApiResponse(
        responseCode = "200",
        description = "Successfully retrieved skills",
        content = @Content(mediaType = "application/json", schema = @Schema(implementation = SkillDto.class))
    )
    public List<SkillDto> getAll(
        @Parameter(description = "ID of the user to get skills for", required = true)
        @PathVariable final String userId
    ) {
        return skillService.getAllByUser(userId)
            .stream()
            .map(skillDtoMapper::mapToDto)
            .toList();
    }

    @Post("add")
    @NewSpan
    @Operation(
        summary = "Add a new skill",
        description = "Creates a new skill for a user"
    )
    @ApiResponse(
        responseCode = "200",
        description = "Successfully added skill",
        content = @Content(mediaType = "application/json", schema = @Schema(implementation = SkillDto.class))
    )
    public SkillDto add(
        @Parameter(description = "Skill data to add", required = true)
        @Body final SkillDto userDto
    ) {
        final Skill user = skillDtoMapper.mapFromDto(userDto);

        return skillDtoMapper.mapToDto(
            skillService.addSkill(user)
        );
    }

    @Post("add-all")
    @NewSpan
    @Operation(
        summary = "Add multiple skills",
        description = "Creates multiple new skills for a user"
    )
    @ApiResponse(
        responseCode = "200",
        description = "Successfully added skills",
        content = @Content(mediaType = "application/json", schema = @Schema(implementation = SkillDto.class))
    )
    public List<SkillDto> add(
        @Parameter(description = "List of skills to add", required = true)
        @Body final List<SkillDto> userDtos
    ) {
        return userDtos.stream().map(this::add).toList();
    }

    @Post("edit")
    @NewSpan
    @Operation(
        summary = "Edit an existing skill",
        description = "Updates an existing skill's information"
    )
    @ApiResponse(
        responseCode = "200",
        description = "Successfully updated skill",
        content = @Content(mediaType = "application/json", schema = @Schema(implementation = SkillDto.class))
    )
    public SkillDto edit(
        @Parameter(description = "Updated skill data", required = true)
        @Body final SkillDto userDto
    ) {
        final Skill user = skillDtoMapper.mapFromDto(userDto);

        return skillDtoMapper.mapToDto(
            skillService.editSkill(user)
        );
    }

    @Post("delete")
    @NewSpan
    @Operation(
        summary = "Delete a skill",
        description = "Removes a skill from the system"
    )
    @ApiResponse(
        responseCode = "200",
        description = "Successfully deleted skill"
    )
    public void delete(
        @Parameter(description = "Skill to delete", required = true)
        @Body final SkillDto userDto
    ) {
        final Skill user = skillDtoMapper.mapFromDto(userDto);

        skillService.deleteSkill(user);
    }
}
