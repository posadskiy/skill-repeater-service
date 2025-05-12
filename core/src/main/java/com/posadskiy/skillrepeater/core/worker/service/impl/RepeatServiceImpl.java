package com.posadskiy.skillrepeater.core.worker.service.impl;

import com.posadskiy.skillrepeater.core.mapper.entity.SkillEntityMapper;
import com.posadskiy.skillrepeater.core.model.Skill;
import com.posadskiy.skillrepeater.core.notification.service.NotificationService;
import com.posadskiy.skillrepeater.core.service.RepeatHistoryService;
import com.posadskiy.skillrepeater.core.service.SkillService;
import com.posadskiy.skillrepeater.core.storage.db.SkillRepository;
import com.posadskiy.skillrepeater.core.worker.ScheduledEvent;
import com.posadskiy.skillrepeater.core.worker.service.RepeatService;
import io.micronaut.scheduling.annotation.Scheduled;
import jakarta.inject.Singleton;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.concurrent.*;
import java.util.stream.Collectors;

@Slf4j
@Singleton
public class RepeatServiceImpl implements RepeatService {
    private final SkillRepository skillRepository;
    private final SkillEntityMapper skillEntityMapper;
    private final BlockingQueue<ScheduledEvent> queue;
    private final NotificationService notificationService;
    private final RepeatHistoryService repeatHistoryService;
    private final SkillService skillService;

    public RepeatServiceImpl(SkillRepository skillRepository, SkillEntityMapper skillEntityMapper, NotificationService notificationService, RepeatHistoryService repeatHistoryService, SkillService skillService) {
        this.skillRepository = skillRepository;
        this.skillEntityMapper = skillEntityMapper;
        this.notificationService = notificationService;
        this.queue = new LinkedBlockingQueue<>();
        this.repeatHistoryService = repeatHistoryService;
        this.skillService = skillService;

        startScheduler();
        repeatWorker();
    }

    @Override
    public void repeatWorker() {

        try (ScheduledExecutorService executor = Executors.newScheduledThreadPool(1)) {
            executor.scheduleAtFixedRate(this::loadUpcomingEvents, 0, 5, TimeUnit.MINUTES);
        }

    }

    //@Scheduled(cron = "0 0 * * *")
    @Scheduled(cron = "* * * * *")
    public void loadUpcomingEvents() {
        var users = skillRepository
            .findByNextRepeatedBetween(LocalDate.now().minusDays(1).atStartOfDay(), LocalDate.now().plusDays(1).atStartOfDay())
            .stream()
            .map(skillEntityMapper::mapFromEntity)
            .collect(Collectors.groupingBy(Skill::getUserId));

        users.forEach((userId, skills) -> {
            var names = skills
                .stream()
                .map(Skill::getName)
                .collect(Collectors.joining(", "));

            queue.add(
                new ScheduledEvent(LocalDateTime.now().toEpochSecond(ZoneOffset.UTC), userId, names)
            );
        });
    }

    public void startScheduler() {
        Thread eventProcessor = new Thread(() -> {
            while (true) {
                try {
                    ScheduledEvent event = queue.take();  // Blocks until an event is ready
                    log.info(event.toString());
                    notificationService.sendNotification(event.userId(), event.names());
                    //skillService.repeatSkill(skill.getId());
                    //repeatHistoryService.repeatSkill(skill.getId());
                } catch (Exception e) {
                    log.error(e.getMessage(), e);
                }
            }
        });
        eventProcessor.start();
    }
}
