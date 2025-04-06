package com.hask.hasktask.event;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class TaskProducer {

    final private KafkaTemplate<String, String> kafkaTemplate;

    @Value("${haskTask.app.topics.taskTopic}")
    private String taskTopic;  // Kafka topic for task-related events

    public TaskProducer(KafkaTemplate<String, String> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    // GENERIC METHOD TO SEND EVENTS TO KAFKA TOPICS
    private void sendEvent(String topic, String eventType, Long id, String name) {
        String message = buildTaskMessage(eventType, id, name);
        kafkaTemplate.send(topic, message);
        System.out.println("Sent Kafka message: " + message);
    }

    private static String buildTaskMessage(String eventType, Long id, String name) {
        return eventType + ": " + (name != null ? "id=" + id + ", name=" + name : "id=" + id);
    }

    // SEND TASK-RELATED EVENTS
    public void sendTaskCreatedEvent(Long taskId, String taskName) {
        sendEvent(taskTopic, "TASK_CREATED", taskId, taskName);
    }

    public void sendTaskCompletedEvent(Long taskId) {
        sendEvent(taskTopic, "TASK_COMPLETED", taskId, null);
    }

    public void sendTaskDueEvent(Long taskId, String taskName) {
        System.out.println("Steve-Task-success Producer ID " + taskId);
        sendEvent(taskTopic, "TASK_DUE", taskId, taskName);
    }

    public void sendTaskDeletedEvent(Long taskId) {
        sendEvent(taskTopic, "TASK_DELETED", taskId, null);
    }
}

