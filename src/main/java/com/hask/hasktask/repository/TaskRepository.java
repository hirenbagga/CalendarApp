package com.hask.hasktask.repository;

import com.hask.hasktask.model.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {

    // Custom query to find tasks based on the range of dueDate
    List<Task> findByDueDateBetween(LocalDateTime start, LocalDateTime end); // Query tasks within a given time range

    List<Task> findByUserId(Long userId);
}

