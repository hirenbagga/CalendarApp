package com.hask.hasktask.service;

import com.hask.hasktask.model.Event;
import com.hask.hasktask.repository.EventRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class EventService {

    final private EventRepository eventRepository;

    public EventService(EventRepository eventRepository) {
        this.eventRepository = eventRepository;
    }

    public Event createEvent(Event event) {
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
        event.setEventId(eventId);
        return eventRepository.save(event);
    }

    public void deleteEvent(Long eventId) {
        eventRepository.deleteById(eventId);
    }
}
