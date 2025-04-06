package com.hask.hasktask.repository;

import com.hask.hasktask.model.Event;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface EventRepository extends JpaRepository<Event, Long> {
    List<Event> findByUserId(Long userId);

    // Custom query to find events based on the range of endDateTime
     List<Event> findByEndDateTimeBetween(LocalDateTime start, LocalDateTime end);

    // Custom query to find events that are due and haven't had a reminder sent
    // List<Event> findByEndDateTimeBetweenAndIsReminderSentFalse(LocalDateTime startDateTime, LocalDateTime endDateTime);
}