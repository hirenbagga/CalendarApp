package com.hask.hasktask.model;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.v3.oas.annotations.Hidden;
import jakarta.persistence.*;
import lombok.*;

@Setter
@Getter
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "timers")
public class Timer {

    @Hidden // Hide the taskId from the Swagger documentation
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long timerId;

    private Long duration;
    private String status;

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
