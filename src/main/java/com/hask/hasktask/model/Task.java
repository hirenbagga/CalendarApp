package com.hask.hasktask.model;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonView;
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
@Table(name = "tasks")
public class Task {

    @Hidden // Hide the taskId from the Swagger documentation
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long taskId;

    private String taskName;
    private LocalDateTime dueDate;
    @Builder.Default
    private boolean isCompleted = false;
    private String taskDescription;
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
