package com.hask.hasktask.event;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class EventProducer {

    @Value("${haskTask.app.topics.eventTopic}")
    private String eventTopic;  // Kafka topic for event-related events

    final private KafkaTemplate<String, String> kafkaTemplate;

    public EventProducer(KafkaTemplate<String, String> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    // GENERIC METHOD TO SEND EVENTS TO KAFKA TOPICS
    private void sendEvent(String topic, String eventType, Long id, String name) {
        String message = buildEventMessage(eventType, id, name);
        kafkaTemplate.send(topic, message);
        System.out.println("Sent Kafka message: " + message);
    }

    private String buildEventMessage(String eventType, Long id, String name) {
        return eventType + ": id=" + id + ", name=" + (name != null ? name : "N/A");
    }

    // SEND EVENT-RELATED EVENTS
    public void sendEventCreatedEvent(Long eventId, String eventName) {
        sendEvent(eventTopic, "EVENT_CREATED", eventId, eventName);
    }

    public void sendEventDueEvent(Long eventId, String eventName) {
        System.out.println("Steve-Event-success Producer ID " + eventId);
        sendEvent(eventTopic, "EVENT_DUE", eventId, eventName);
    }

    public void sendEventDeletedEvent(Long eventId) {
        sendEvent(eventTopic, "EVENT_DELETED", eventId, null);
    }

    /* *public void sendEventCompletedEvent(Long eventId, String eventName) {
        sendEvent(eventTopic, "EVENT_COMPLETED", eventId, eventName);
    }*/
}


