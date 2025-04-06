package com.hask.hasktask.controller;

import com.hask.hasktask.model.Task;
import com.hask.hasktask.event.TaskProducer;
import com.hask.hasktask.service.TaskService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/api/v1/tasks")
@Tag(name = "Task Management")
public class TaskController {

    final private TaskService taskService;
    final private TaskProducer taskProducer;

    public TaskController(TaskService taskService, TaskProducer taskProducer) {
        this.taskService = taskService;
        this.taskProducer = taskProducer;
    }

    // Endpoint to create a new task
    @PostMapping
    public ResponseEntity<?> createTask(@RequestBody Task body) {
        var task = taskService.create(body); // Save task to the database
        taskProducer.sendTaskCreatedEvent(task.getTaskId(), task.getTaskName());  // Trigger Kafka event for task creation

        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    // Endpoint to complete a task
    @PostMapping("/{taskId}/complete")
    public ResponseEntity<?> completeTask(@PathVariable Long taskId) {
        taskService.completeTask(taskId);
        taskProducer.sendTaskCompletedEvent(taskId);  // Trigger Kafka event for task completion

        return ResponseEntity.noContent().build();
    }


    @GetMapping("/{userId}/user")
    public ResponseEntity<List<Task>> getTasks(@PathVariable Long userId) {
        return ResponseEntity.ok(taskService.getTasksByUserId(userId));
    }

    @GetMapping("/{taskId}")
    public ResponseEntity<Task> getTask(@PathVariable Long taskId) {
        return ResponseEntity.ok(taskService.getTaskById(taskId));
    }

    @DeleteMapping("/{taskId}")
    public ResponseEntity<Void> deleteTask(@PathVariable Long taskId) {
        taskService.deleteTask(taskId);  // Delete task from the database
        taskProducer.sendTaskDeletedEvent(taskId);  // Trigger Kafka event for task deletion
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{taskId}")
    public ResponseEntity<?> updateTask(@PathVariable Long taskId, @RequestBody Task task) {
        taskService.updateTask(taskId, task);  // Update task in the database
        //taskProducer.sendTaskUpdatedEvent(taskId, task.getTaskName());  // Trigger Kafka event for task update
        return ResponseEntity.noContent().build();
    }


    /* *@PutMapping("/{taskId}")
    public ResponseEntity<Task> completeTask(@PathVariable Long taskId) {
        return ResponseEntity.ok(taskService.completeTask(taskId));
    }
    @PostMapping
    public ResponseEntity<Task> createTask(@RequestBody Task task) {
        return ResponseEntity.status(HttpStatus.CREATED).body(taskService.createTask(task));
    }*/
}

