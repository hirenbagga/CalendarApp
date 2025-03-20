package com.hask.hasktask.model;


import io.swagger.v3.oas.annotations.Hidden;
import jakarta.persistence.*;

@Entity
@Table(name = "timers")
public class Timer {

    @Hidden // Hide the taskId from the Swagger documentation
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long timerId;

    private Long duration;
    private String status;
    private Long userId;

    public Timer() {
    }

    public Long getTimerId() {
        return timerId;
    }

    public void setTimerId(Long timerId) {
        this.timerId = timerId;
    }

    public Long getDuration() {
        return duration;
    }

    public void setDuration(Long duration) {
        this.duration = duration;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

}
