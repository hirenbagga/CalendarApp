package com.hask.hasktask.listener;

import com.hask.hasktask.model.Event;
import com.hask.hasktask.service.EventService;
import com.hask.hasktask.service.NotificationService;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@EnableKafka  // Enable Kafka listener in Spring Boot
@Component    // Register KafkaConsumer as a Spring bean
public class EventConsumer {

    private final EventService eventService;
    private final NotificationService notificationService;

    public EventConsumer(EventService eventService, NotificationService notificationService) {
        this.eventService = eventService;
        this.notificationService = notificationService;
    }

    // Kafka listener for user-event events (e.g., EVENT_CREATED, EVENT_DELETED)
    @KafkaListener(topics = "${haskTask.app.topics.eventTopic}", groupId = "event-consumer-group")
    public void listenEventEvents(ConsumerRecord<String, String> record) {
        String message = record.value();
        processEventMessage(message);
    }

    // Generalized method to process task and event messages
    private void processEventMessage(String msg) {
        EventDetails eventDetails = extractEventDetails(msg);

        if (eventDetails != null) {
            switch (eventDetails.eventType()) {
                case "EVENT_CREATED":
                    handleEventCreated(eventDetails);
                    break;
                case "EVENT_DUE":
                    handleEventDue(eventDetails);
                    break;
                case "EVENT_COMPLETED":
                case "EVENT_DELETED":
                    handleEventCompletedOrDeleted(eventDetails);
                    break;
                default:
                    System.out.println("Unknown event type");
            }
        }
    }

    private String getPart(String[] parts, int index) {
        return parts[index].split("=")[1];
    }

    private EventDetails extractEventDetails(String msg) {
        String[] parts = msg.split(",");
        if (parts.length < 2) return null; // Invalid message format

        long id = Long.parseLong(getPart(parts,0));
        String name = getPart(parts,1);

        String eventType = extractEventType(msg);
        return new EventDetails(id, name, eventType);
    }

    private String extractEventType(String msg) {
        if (msg.contains("EVENT_CREATED")) return "EVENT_CREATED";
        if (msg.contains("EVENT_DUE")) return "EVENT_DUE";
        if (msg.contains("EVENT_COMPLETED")) return "EVENT_COMPLETED";
        if (msg.contains("EVENT_DELETED")) return "EVENT_DELETED";
        return "UNKNOWN";
    }

    private void handleEventCreated(EventDetails details) {
        System.out.println("EVENT created: ID=" + details.id() + ", Name=" + details.name());
    }

    private void handleEventDue(EventDetails details) {
        String action = details.eventType().contains("TASK_COMPLETED") ? " marked as completed." : " is due.";
        processEventReminders(details, action);
        System.out.println("EVENT due: ID=" + details.id() + ", Name=" + details.name());
    }

    private void handleEventCompletedOrDeleted(EventDetails details) {
        String action = details.eventType().equals("EVENT_COMPLETED") ? "completed" : "deleted";
        processEventReminders(details, action);
        System.out.println("EVENT " + action + ": ID=" + details.id());
    }

    private void processEventReminders(EventDetails details, String status) {
        Event event = eventService.getEventById(details.id());

        if (event != null) {
            if (details.eventType().contains("EVENT_DUE")) {
                // Update event status and send notification
                event.setRecurring(false);
                event.setReminderSent(true);
                eventService.setReminderSent(event);
            }

            notify(status, event);
        }
    }

    private void notify(String status, Event event) {
        notificationService.sendEventNotification(
                event.getUser().getEmail(),
                "Event " + event.getEventName() + status,
                event.getStartDateTime()
        );
    }

    // Converted to Java Record
    public record EventDetails(long id, String name, String eventType) {
    }
}

