package com.hask.hasktask.listener;

import com.hask.hasktask.model.Task;
import com.hask.hasktask.service.NotificationService;
import com.hask.hasktask.service.TaskService;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@EnableKafka  // Enable Kafka listener in Spring Boot
@Component    // Register KafkaConsumer as a Spring bean
public class TaskConsumer {

    private final TaskService taskService;
    private final NotificationService notificationService;

    public TaskConsumer(TaskService taskService, NotificationService notificationService) {
        this.taskService = taskService;
        this.notificationService = notificationService;
    }

    // Kafka listener for task events (e.g., TASK_CREATED, TASK_UPDATED, TASK_DELETED)
    @KafkaListener(topics = "${haskTask.app.topics.taskTopic}", groupId = "task-consumer-group")
    public void listenTaskEvents(ConsumerRecord<String, String> record) {
        String message = record.value();
        processTaskMessage(message);
    }

    // Generalized method to process task messages
    private void processTaskMessage(String msg) {
        TaskDetails taskDetails = extractTaskDetails(msg);

        if (taskDetails != null) {
            switch (taskDetails.taskType()) {
                case "TASK_CREATED":
                    handleTaskCreated(taskDetails);
                    break;
                case "TASK_DUE":
                    handleTaskDue(taskDetails);
                    break;
                case "TASK_DELETED":
                    handleTaskDeleted(taskDetails);
                    break;
                default:
                    System.out.println("Unknown task type");
            }
        }
    }

    private String getPart(String[] parts, int index) {
        return parts[index].split("=")[1];
    }

    private TaskDetails extractTaskDetails(String msg) {
        String[] parts = msg.split(",");
        if (parts.length < 2) return null; // Invalid message format

        long id = Long.parseLong(getPart(parts, 0));
        String name = getPart(parts, 1);

        String taskType = extractTaskType(msg);
        return new TaskDetails(id, name, taskType);
    }

    private String extractTaskType(String msg) {
        if (msg.contains("TASK_CREATED")) return "TASK_CREATED";
        if (msg.contains("TASK_UPDATED")) return "TASK_UPDATED";
        if (msg.contains("TASK_DELETED")) return "TASK_DELETED";
        return "UNKNOWN";
    }

    private void handleTaskCreated(TaskDetails details) {
        System.out.println("TASK created: ID=" + details.id() + ", Name=" + details.name());
    }

    private void handleTaskDue(TaskDetails details) {
        System.out.println("TASK due: ID=" + details.id() + ", Name=" + details.name());
        processTaskReminders(details);
    }

    private void handleTaskDeleted(TaskDetails details) {
        System.out.println("TASK deleted: ID=" + details.id());
        taskService.deleteTask(details.id());
    }

    private void processTaskReminders(TaskDetails details) {
        Task task = taskService.getTaskById(details.id());

        if (task != null) {
            if (details.taskType().equals("TASK_DUE") || details.taskType().equals("TASK_CREATED")) {
                // Set reminder and send notifications as required
                task.setReminderSent(true);
                taskService.setReminderSent(task);
            }

            notify(task);
        }
    }

    private void notify(Task task) {
        notificationService.sendTaskNotification(
                task.getUser().getEmail(),
                "Task " + task.getTaskName() + " " + "due",
                task.getDueDate()
        );
    }

    // Converted to Java Record
    public record TaskDetails(long id, String name, String taskType) {
    }
}

