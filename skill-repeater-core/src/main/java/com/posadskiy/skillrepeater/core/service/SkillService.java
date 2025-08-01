package com.posadskiy.skillrepeater.core.service;

import com.posadskiy.skillrepeater.core.model.Skill;

import java.util.List;

public interface SkillService {
    Skill addSkill(Skill skill);

    Skill editSkill(Skill skill);

    void deleteSkill(Skill skill);

    void deleteById(String id);

    void deleteAllByUser(String userId);

    void repeatSkill(String id);

    List<Skill> getAllByUser(String userId);

    Skill get(String id);
}
