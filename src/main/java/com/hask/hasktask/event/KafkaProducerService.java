/* *
package com.hask.hasktask.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class KafkaProducerService {

    final private KafkaTemplate<String, String> kafkaTemplate;

    @Value("${haskTask.app.topics.eventTopic}")
    private String eventTopic;  // Kafka topic for event-related events

    @Value("${haskTask.app.topics.taskTopic}")
    private String taskTopic;  // Kafka topic for task-related events

    public KafkaProducerService(KafkaTemplate<String, String> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    // GENERIC METHOD TO SEND EVENTS TO KAFKA TOPICS (TASKS AND EVENTS)
    private void sendEvent(String topic, String eventType, Long id, String name) {
        String message = eventType + ": " + (name != null ? "id=" + id + ", name=" + name : "id=" + id);
        kafkaTemplate.send(topic, message);
        System.out.println("Sent Kafka message: " + message);
    }

    // SEND TASK-RELATED EVENTS
    public void sendTaskCreatedEvent(Long taskId, String taskName) {
        sendEvent(taskTopic, "TASK_CREATED", taskId, taskName);
    }

    public void sendTaskCompletedEvent(Long taskId) {
        sendEvent(taskTopic, "TASK_COMPLETED", taskId, null);
    }

    public void sendTaskDeletedEvent(Long taskId) {
        sendEvent(taskTopic, "TASK_DELETED", taskId, null);
    }

    // SEND EVENT-RELATED EVENTS
    public void sendEventCreatedEvent(Long eventId, String eventName) {
        sendEvent(eventTopic, "EVENT_CREATED", eventId, eventName);
    }

    public void sendEventDueEvent(Long eventId, String eventName) {
        System.out.println("Steve-success Producer ID "+eventId);
        sendEvent(eventTopic, "EVENT_DUE", eventId, eventName);
    }

    public void sendEventDeletedEvent(Long eventId) {
        sendEvent(eventTopic, "EVENT_DELETED", eventId, null);
    }
}
*/
