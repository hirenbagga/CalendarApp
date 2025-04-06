package com.hask.hasktask.service;

import com.hask.hasktask.event.TaskProducer;
import com.hask.hasktask.model.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class TaskDueReminder {

    private final TaskService taskService;
    private final TaskProducer taskProducer;

    @Autowired
    public TaskDueReminder(TaskService taskService, TaskProducer taskProducer) {
        this.taskService = taskService;
        this.taskProducer = taskProducer;
    }

    // Background job runs every minute to check reminders
    @Scheduled(fixedRate = 60000)  // Runs every minute
    public void checkForDueTasks() {
        // Query Tasks that are due now or within a specific time window
        List<Task> dueTasks = taskService.findDueTasks();

        // Use asynchronous processing to handle each task concurrently
        dueTasks.parallelStream().forEach(task -> {
            try {
                System.out.println("Success-Tasks-Steve: " + task);
                // Trigger Kafka notification (asynchronously)
                if (!task.isReminderSent()) {
                    taskProducer.sendTaskDueEvent(task.getTaskId(), task.getTaskName());
                    task.setReminderSent(true); // Mark reminder as sent
                }
            } catch (Exception e) {
                // Handle errors such as email failure or Kafka errors
                System.err.println("Failed to send notification for task ID " + task.getTaskId() + ": " + e.getMessage());
            }
        });
    }
}
