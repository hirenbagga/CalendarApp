package com.hask.hasktask.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.v3.oas.annotations.Hidden;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Setter
@Getter
@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
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
    @Builder.Default
    private boolean isRecurring = false;
    private String recurringType;
    private String recurringDays;
    private String recurringEndDate;
    private String description;
    @Builder.Default
    private boolean isReminderSent = false;

    @ManyToOne // This is a Many-to-One relationship with the User entity
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    @JsonIgnoreProperties({
            "role",
            "password",
            "enabled",
            "createdAt",
            "updatedAt",
            "username",
            "firstName",
            "lastName",
            "email",
            "phone"
    }) // Ignore these fields from the JSON response
    private User user;

}
