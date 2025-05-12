package com.posadskiy.skillrepeater.web.controller;


import com.posadskiy.skillrepeater.core.service.RepeatHistoryService;
import com.posadskiy.skillrepeater.core.service.SkillService;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Post;
import io.micronaut.security.annotation.Secured;
import io.micronaut.security.rules.SecurityRule;
import io.micronaut.tracing.annotation.NewSpan;
import lombok.NoArgsConstructor;

@Secured(SecurityRule.IS_AUTHENTICATED)
@Controller("v0/repeat")
@NoArgsConstructor
public class RepeatController {
    private SkillService skillService;
    private RepeatHistoryService repeatService;
    
    public RepeatController(SkillService skillService, RepeatHistoryService repeatService) {
        this.skillService = skillService;
        this.repeatService = repeatService;
    }

    @Post("{id}")
    @NewSpan
    public void repeat(String id) {
        skillService.repeatSkill(id);
        repeatService.repeatSkill(id);
    }
}
