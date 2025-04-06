package com.hask.hasktask.service;

import com.hask.hasktask.event.EventProducer;
import com.hask.hasktask.model.Event;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class EventDueReminder {

    private final EventService eventService;
    private final EventProducer eventProducer;

    @Autowired
    public EventDueReminder(EventService eventService, EventProducer eventProducer) {
        this.eventService = eventService;
        this.eventProducer = eventProducer;
    }

    // Background job runs every minute to check reminders
    @Scheduled(fixedRate = 60000)  // Runs every minute
    public void checkForDueEvents() {
        // Query events that are due now or within a specific time window
        List<Event> dueEvents = eventService.findDueEvents();

        // Use asynchronous processing to handle each event concurrently
        dueEvents.parallelStream().forEach(event -> {
            try {
                System.out.println("Success-Steve: " + event);
                // Trigger Kafka notification (asynchronously)
                if (!event.isReminderSent()) {
                    eventProducer.sendEventDueEvent(event.getEventId(), event.getEventName());
                    event.setReminderSent(true); // Mark reminder as sent
                }
            } catch (Exception e) {
                // Handle errors such as email failure or Kafka errors
                System.err.println("Failed to send notification for event ID " + event.getEventId() + ": " + e.getMessage());
            }
        });
    }
}
