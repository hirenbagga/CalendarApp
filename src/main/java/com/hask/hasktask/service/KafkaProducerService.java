/*
package com.hask.hasktask.service;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class KafkaProducerService {

    final private KafkaTemplate<String, String> kafkaTemplate;

    private final String taskTopic = "task-events";  // Kafka topic for task-related events
    private final String eventTopic = "event-events";  // Kafka topic for event-related events

    public KafkaProducerService(KafkaTemplate<String, String> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    // Method to send task creation event
    public void sendTaskCreatedEvent(Long taskId, String taskName) {
        String message = "TASK_CREATED: taskId=" + taskId + ", taskName=" + taskName;
        kafkaTemplate.send(taskTopic, message);
        System.out.println("Sent Kafka message: " + message);
    }

    // Method to send task completion event
    public void sendTaskCompletedEvent(Long taskId) {
        String message = "TASK_COMPLETED: taskId=" + taskId;
        kafkaTemplate.send(taskTopic, message);
        System.out.println("Sent Kafka message: " + message);
    }

    // Method to send task deletion event
    public void sendTaskDeletedEvent(Long taskId) {
        String message = "TASK_DELETED: taskId=" + taskId;
        kafkaTemplate.send(taskTopic, message);
        System.out.println("Sent Kafka message: " + message);
    }

    // Method to send event creation event
    public void sendEventCreatedEvent(Long eventId, String eventName) {
        String message = "EVENT_CREATED: eventId=" + eventId + ", eventName=" + eventName;
        kafkaTemplate.send(eventTopic, message);
        System.out.println("Sent Kafka message: " + message);
    }

    // Method to send event deletion event
    public void sendEventDeletedEvent(Long eventId) {
        String message = "EVENT_DELETED: eventId=" + eventId;
        kafkaTemplate.send(eventTopic, message);
        System.out.println("Sent Kafka message: " + message);
    }

    // update task event\
}
*/
