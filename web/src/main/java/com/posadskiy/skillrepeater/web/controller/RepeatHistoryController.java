package com.posadskiy.skillrepeater.web.controller;

import com.posadskiy.skillrepeater.api.dto.RepeatHistoryDto;
import com.posadskiy.skillrepeater.core.mapper.dto.RepeatHistoryDtoMapper;
import com.posadskiy.skillrepeater.core.service.RepeatHistoryService;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.PathVariable;
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
@Controller("v0/repeat-history")
@NoArgsConstructor
@Tag(name = "Repeat History", description = "APIs for managing skill repetition history")
public class RepeatHistoryController {
    private RepeatHistoryService repeatHistoryService;
    private RepeatHistoryDtoMapper repeatHistoryDtoMapper;
    
    public RepeatHistoryController(RepeatHistoryService repeatHistoryService,
                                 RepeatHistoryDtoMapper repeatHistoryDtoMapper) {
        this.repeatHistoryService = repeatHistoryService;
        this.repeatHistoryDtoMapper = repeatHistoryDtoMapper;
    }

    @Get("skill/{skillId}")
    @NewSpan
    @Operation(
        summary = "Get repeat history for a skill",
        description = "Retrieves the repetition history for a specific skill"
    )
    @ApiResponse(
        responseCode = "200",
        description = "Successfully retrieved repeat history",
        content = @Content(mediaType = "application/json", schema = @Schema(implementation = RepeatHistoryDto.class))
    )
    public List<RepeatHistoryDto> getBySkill(
        @Parameter(description = "ID of the skill to get history for", required = true)
        @PathVariable String skillId
    ) {
        return repeatHistoryService.getBySkillId(skillId)
            .stream()
            .map(repeatHistoryDtoMapper::mapToDto)
            .toList();
    }

    @Get("user/{userId}")
    @NewSpan
    @Operation(
        summary = "Get repeat history for a user",
        description = "Retrieves the repetition history for all skills of a specific user"
    )
    @ApiResponse(
        responseCode = "200",
        description = "Successfully retrieved repeat history",
        content = @Content(mediaType = "application/json", schema = @Schema(implementation = RepeatHistoryDto.class))
    )
    public List<RepeatHistoryDto> getByUser(
        @Parameter(description = "ID of the user to get history for", required = true)
        @PathVariable String userId
    ) {
        return repeatHistoryService.getByUserId(userId)
            .stream()
            .map(repeatHistoryDtoMapper::mapToDto)
            .toList();
    }
} 