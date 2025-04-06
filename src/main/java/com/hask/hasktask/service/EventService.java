package com.hask.hasktask.service;

import com.hask.hasktask.customException.GeneralException;
import com.hask.hasktask.model.Event;
import com.hask.hasktask.repository.EventRepository;
import com.hask.hasktask.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class EventService {

    final private EventRepository eventRepository;
    private final UserRepository userRepository;

    public EventService(EventRepository eventRepository, UserRepository userRepository) {
        this.eventRepository = eventRepository;
        this.userRepository = userRepository;
    }

    public List<Event> findDueEvents() {
        LocalDateTime now = LocalDateTime.now(); // Get the current time
        LocalDateTime windowEnd = now.plusMinutes(5); // Current time + 5 minutes window

        System.out.println("Test due date should be: " + windowEnd);
        // Check for events that are due and haven't had a reminder sent
        return eventRepository.findByEndDateTimeBetween(now, windowEnd);
    }

    public Event createEvent(Event event) {
        var user = userRepository.findById(event.getUser().getId())
                .orElseThrow(() -> new GeneralException("User", "User not found"));

        // Set the fetched User in the Event object
        event.setUser(user);
        return eventRepository.save(event);
    }

    public List<Event> getEventsByUserId(Long userId) {
        return eventRepository.findByUserId(userId);
    }

    public Event getEventById(Long eventId) {
        Optional<Event> event = eventRepository.findById(eventId);
        return event.orElse(null);
    }

    public Event updateEvent(Long eventId, Event event) {
        eventRepository.findById(eventId)
                .orElseThrow(() -> new GeneralException("Event", "Event not found"));

        event.setEventId(eventId);
        return eventRepository.save(event);
    }

    public void setReminderSent(Event event) {
        event.setEventId(event.getEventId());
        eventRepository.save(event);
    }

    public void deleteEvent(Long eventId) {
        eventRepository.deleteById(eventId);
    }
}
