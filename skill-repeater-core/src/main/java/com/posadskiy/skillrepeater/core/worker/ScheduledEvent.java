package com.posadskiy.skillrepeater.core.worker;

import java.util.concurrent.Delayed;
import java.util.concurrent.TimeUnit;

public record ScheduledEvent(long triggerTime, String userId, String names) implements Delayed {

    @Override
    public long getDelay(TimeUnit unit) {
        return unit.convert(triggerTime - System.currentTimeMillis(), TimeUnit.MILLISECONDS);
    }

    @Override
    public int compareTo(Delayed o) {
        return Long.compare(this.triggerTime, ((ScheduledEvent) o).triggerTime());
    }
}
