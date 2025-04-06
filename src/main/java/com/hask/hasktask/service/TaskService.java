package com.hask.hasktask.service;

import com.hask.hasktask.customException.GeneralException;
import com.hask.hasktask.model.Task;
import com.hask.hasktask.repository.TaskRepository;
import com.hask.hasktask.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class TaskService {

    final private TaskRepository taskRepository;
    private final UserRepository userRepository;

    public TaskService(TaskRepository taskRepository, UserRepository userRepository) {
        this.taskRepository = taskRepository;
        this.userRepository = userRepository;
    }

    public List<Task> findDueTasks() {
        LocalDateTime now = LocalDateTime.now(); // Get the current time
        LocalDateTime windowEnd = now.plusMinutes(5); // Current time + 5 minutes window
        return taskRepository.findByDueDateBetween(now, windowEnd); // Query based on LocalDateTime
    }

    public Task create(Task task) {
        var user = userRepository.findById(task.getUser().getId())
                .orElseThrow(() -> new GeneralException("User", "User not found"));

        // Set the fetched User in the Event object
        task.setUser(user);

        return taskRepository.save(task);
    }

    public List<Task> getTasksByUserId(Long userId) {
        return taskRepository.findByUserId(userId);
    }

    public Task getTaskById(Long taskId) {
        Optional<Task> task = taskRepository.findById(taskId);
        return task.orElse(null);
    }

    public void completeTask(Long taskId) {
        Task task = getTaskById(taskId);
        if (task != null && !task.isCompleted()) {
            task.setCompleted(true);
            taskRepository.save(task);
        }
    }

    public void deleteTask(Long taskId) {
        taskRepository.deleteById(taskId);
    }

    public List<Task> getAllTasks() {
        return taskRepository.findAll();
    }

    // Update task
    public void updateTask(Long taskId, Task task) {
        Task existingTask = getTaskById(taskId);
        if (existingTask != null) {
            existingTask.setTaskName(task.getTaskName());
            existingTask.setTaskDescription(task.getTaskDescription());
            existingTask.setDueDate(task.getDueDate());
            existingTask.setCompleted(task.isCompleted());
            taskRepository.save(existingTask);
        }
    }

    public void setReminderSent(Task event) {
        event.setTaskId(event.getTaskId());
        taskRepository.save(event);
    }
}
