package com.hask.hasktask.controller;

import com.hask.hasktask.model.Event;
import com.hask.hasktask.service.EventService;
import com.hask.hasktask.service.KafkaProducerService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/events")
@Tag(name = "Event Management")
public class EventController {

    final private EventService eventService;
    final private KafkaProducerService kafkaProducerService;

    public EventController(EventService eventService, KafkaProducerService kafkaProducerService) {
        this.eventService = eventService;
        this.kafkaProducerService = kafkaProducerService;
    }

    @PostMapping
    public ResponseEntity<?> createEvent(@RequestBody Event body) {
        var event = eventService.createEvent(body); // Save event to the database
        kafkaProducerService.sendEventCreatedEvent(event.getEventId(), event.getEventName());  // Trigger Kafka event for event creation
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    // Endpoint to delete an event
    @DeleteMapping("/{eventId}")
    public ResponseEntity<Void> deleteEvent(@PathVariable Long eventId) {
        eventService.deleteEvent(eventId); // Delete event from the database
        kafkaProducerService.sendEventDeletedEvent(eventId);  // Trigger Kafka event for event deletion
        return ResponseEntity.noContent().build();
    }


    @GetMapping("/{userId}/user")
    public ResponseEntity<List<Event>> getEvents(@PathVariable Long userId) {
        return ResponseEntity.ok(eventService.getEventsByUserId(userId));
    }

    @GetMapping("/{eventId}")
    public ResponseEntity<Event> getEvent(@PathVariable Long eventId) {
        return ResponseEntity.ok(eventService.getEventById(eventId));
    }

    @PutMapping("/{eventId}")
    public ResponseEntity<Event> updateEvent(@PathVariable Long eventId, @RequestBody Event event) {
        return ResponseEntity.ok(eventService.updateEvent(eventId, event));
    }

}

