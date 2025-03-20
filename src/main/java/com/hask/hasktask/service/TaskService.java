package com.hask.hasktask.service;

import com.hask.hasktask.model.Task;
import com.hask.hasktask.repository.TaskRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TaskService {

    final private TaskRepository taskRepository;

    public TaskService(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    public Task createTask(Task task) {
        return taskRepository.save(task);
    }

    public List<Task> getTasksByUserId(Long userId) {
        return taskRepository.findByUserId(userId);
    }

    public Task getTaskById(Long taskId) {
        Optional<Task> task = taskRepository.findById(taskId);
        return task.orElse(null);
    }

    public Task completeTask(Long taskId) {
        Task task = getTaskById(taskId);
        if (task != null) {
            task.setCompleted(true);
            return taskRepository.save(task);
        }
        return null;
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
}
