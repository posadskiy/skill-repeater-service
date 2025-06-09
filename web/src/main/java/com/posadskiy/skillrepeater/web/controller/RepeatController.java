package com.posadskiy.skillrepeater.web.controller;


import com.posadskiy.skillrepeater.core.service.RepeatHistoryService;
import com.posadskiy.skillrepeater.core.service.SkillService;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Post;
import io.micronaut.security.annotation.Secured;
import io.micronaut.security.rules.SecurityRule;
import io.micronaut.tracing.annotation.NewSpan;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.NoArgsConstructor;

@Secured(SecurityRule.IS_AUTHENTICATED)
@Controller("v0/repeat")
@NoArgsConstructor
@Tag(name = "Skill Repetition", description = "APIs for managing skill repetition")
public class RepeatController {
    private SkillService skillService;
    private RepeatHistoryService repeatHistoryService;
    
    public RepeatController(SkillService skillService, RepeatHistoryService repeatHistoryService) {
        this.skillService = skillService;
        this.repeatHistoryService = repeatHistoryService;
    }

    @Post("{id}")
    @NewSpan
    @Operation(
        summary = "Mark a skill as repeated",
        description = "Records a repetition of a skill and updates its repetition history"
    )
    @ApiResponse(
        responseCode = "200",
        description = "Successfully recorded skill repetition"
    )
    public void repeat(
        @Parameter(description = "ID of the skill to mark as repeated", required = true)
        String id
    ) {
        skillService.repeatSkill(id);
        repeatHistoryService.repeatSkill(id);
    }
}
