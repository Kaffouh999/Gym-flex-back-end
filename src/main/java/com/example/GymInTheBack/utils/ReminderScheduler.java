package com.example.GymInTheBack.utils;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class ReminderScheduler {

    private static final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

    public static void scheduleReminder(LocalDateTime reminderTime, Runnable task) {
        LocalDateTime now = LocalDateTime.now();
        long initialDelay = now.until(reminderTime, ChronoUnit.SECONDS);
        scheduler.schedule(task, initialDelay, TimeUnit.SECONDS);
    }
}

