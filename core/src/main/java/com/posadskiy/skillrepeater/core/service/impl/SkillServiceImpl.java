package com.posadskiy.skillrepeater.core.service.impl;

import com.posadskiy.skillrepeater.core.mapper.entity.SkillEntityMapper;
import com.posadskiy.skillrepeater.core.model.Skill;
import com.posadskiy.skillrepeater.core.service.SkillService;
import com.posadskiy.skillrepeater.core.storage.db.SkillRepository;
import com.posadskiy.skillrepeater.core.storage.db.entity.SkillEntity;
import jakarta.inject.Singleton;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Singleton
@NoArgsConstructor
public class SkillServiceImpl implements SkillService {
    private SkillRepository skillRepository;
    private SkillEntityMapper skillEntityMapper;

    public SkillServiceImpl(SkillRepository skillRepository, SkillEntityMapper skillEntityMapper) {
        this.skillRepository = skillRepository;
        this.skillEntityMapper = skillEntityMapper;
    }

    @Override
    public Skill addSkill(Skill skill) {
        final SkillEntity skillEntity = skillEntityMapper.mapToEntity(skill);

        skillEntity.setLastRepeated(LocalDateTime.now());
        skillEntity.setNextRepeated(LocalDateTime.now());

        return skillEntityMapper.mapFromEntity(
            skillRepository.save(
                skillEntity
            ));
    }

    @Override
    public Skill editSkill(Skill skill) {
        return skillEntityMapper.mapFromEntity(
            skillRepository.update(
                skillEntityMapper.mapToEntity(skill)
            ));
    }

    @Override
    public void deleteSkill(Skill skill) {
        skillRepository.delete(
            skillEntityMapper.mapToEntity(skill)
        );
    }

    @Override
    public void repeatSkill(String id) {
        var loaded = get(id);

        loaded.setLastRepeated(LocalDateTime.now());
        loaded.setNextRepeated(loaded.getLastRepeated().plus(loaded.getNumber(), ChronoUnit.valueOf(loaded.getPeriod())));
        loaded.setLevel(loaded.getLevel() + 1);

        skillRepository.update(
            skillEntityMapper.mapToEntity(loaded)
        );
    }
    
    public List<Skill> getAllByUser(String userId) {
        return skillRepository
            .findByUserId(userId)
            .stream()
            .map(skillEntityMapper::mapFromEntity)
            .toList();
    }

    @Override
    public Skill get(String id) {
        return skillEntityMapper
            .mapFromEntity(
                skillRepository
                    .findById(id)
                    .orElseThrow(RuntimeException::new));
    }
}
