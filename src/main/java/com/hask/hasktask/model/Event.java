package com.hask.hasktask.model;

import io.swagger.v3.oas.annotations.Hidden;
import jakarta.persistence.*;

import java.time.LocalDateTime;

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

    public Long getEventId() {
        return eventId;
    }

    public void setEventId(Long eventId) {
        this.eventId = eventId;
    }

    public String getEventName() {
        return eventName;
    }

    public void setEventName(String eventName) {
        this.eventName = eventName;
    }

    public LocalDateTime getStartDateTime() {
        return startDateTime;
    }

    public void setStartDateTime(LocalDateTime startDateTime) {
        this.startDateTime = startDateTime;
    }

    public LocalDateTime getEndDateTime() {
        return endDateTime;
    }

    public void setEndDateTime(LocalDateTime endDateTime) {
        this.endDateTime = endDateTime;
    }

    public String getMeetingLink() {
        return meetingLink;
    }

    public void setMeetingLink(String meetingLink) {
        this.meetingLink = meetingLink;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getBlockColor() {
        return blockColor;
    }

    public void setBlockColor(String blockColor) {
        this.blockColor = blockColor;
    }

    public boolean getIsRecurring() {
        return isRecurring;
    }

    public void setIsRecurring(boolean isRecurring) {
        this.isRecurring = isRecurring;
    }

    public String getRecurringType() {
        return recurringType;
    }

    public void setRecurringType(String recurringType) {
        this.recurringType = recurringType;
    }

    public String getRecurringDays() {
        return recurringDays;
    }

    public void setRecurringDays(String recurringDays) {
        this.recurringDays = recurringDays;
    }

    public String getRecurringEndDate() {
        return recurringEndDate;
    }

    public void setRecurringEndDate(String recurringEndDate) {
        this.recurringEndDate = recurringEndDate;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }


}
