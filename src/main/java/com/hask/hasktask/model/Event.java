package com.hask.hasktask.model;

import io.swagger.v3.oas.annotations.Hidden;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Setter
@Getter
@Entity
@Table(name = "events")
public class Event {

    @Hidden // Hide the taskId from the Swagger documentation
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long eventId;

    private String eventName;
    private LocalDateTime startDateTime;
    private LocalDateTime endDateTime;
    private String meetingLink;
    private String location;
    private String blockColor;
    private boolean isRecurring;
    private String recurringType;
    private String recurringDays;
    private String recurringEndDate;
    private String description;
    private Long userId;

    public Event() {
    }

}
