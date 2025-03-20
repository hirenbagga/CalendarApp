# HASK TASK API DOCUMENTATION:

## Table of Contents

- [Introduction](#introduction)
- [Getting Started](#getting-started)
- [API Endpoints](#api-endpoints)
  - [Create Task](#create-task)
  - [Get Task](#get-task)
  - [Update Task](#update-task)
  - [Delete Task](#delete-task)
  - [Get Notifications](#get-notifications)
- [Authentication](#authentication)
- [Kafka Integration](#kafka-integration)
- [Error Handling](#error-handling)
- [Development Setup](#development-setup)
- [Testing](#testing)

## Introduction

The Hask Task API allows you to manage tasks and notifications. You can create tasks, update them, and monitor their status through notifications. The API integrates with Kafka to receive event-driven messages related to task updates.
>[API Endpoints Documentation Link] (http://localhost:8080/swagger-ui/index.html)

```markdown
src
└── main
├── java
│    └── com
│         └── hasktask
│              ├── controller
│              │    ├── EventController.java
│              │    ├── TaskController.java
│              │    ├── NotificationController.java
│              │    └── TimerController.java
│              ├── service
│              │    ├── EventService.java
│              │    ├── TaskService.java
│              │    ├── NotificationService.java
│              │    └── TimerService.java
│              ├── model
│              │    ├── Event.java
│              │    ├── Task.java
│              │    ├── Notification.java
│              │    └── Timer.java
│              ├── repository
│              │    ├── EventRepository.java
│              │    ├── TaskRepository.java
│              │    ├── NotificationRepository.java
│              │    └── TimerRepository.java
│              └── config
│                   └── KafkaConfig.java
|                   └── OpenAPIConfig.java
│
├── resources
│    └── application.properties
│    └── kafka-config.properties

# Hask Task API Documentation

Welcome to the Hask Task API! This document serves as a guide for developers to understand how to interact with the API, the available endpoints, and how to use them effectively.

````

## Getting Started

1. Clone the repository:


```bash
git clone https://github.com/your-repo/hask-task.git
```

2. Navigate to the project directory:

   ```bash
   cd hask-task
   ```

3. Set up the application by providing the necessary configuration in the `application.properties` file:

    - `kafka.bootstrap.servers`: Kafka server URL.
    - `kafka.consumer.group.id`: Kafka consumer group ID.

   Example:
   ```properties
   kafka.bootstrap.servers=localhost:9092
   kafka.consumer.group.id=task-group
   ```

4. Install dependencies (if using Maven):

   ```bash
   mvn clean install
   ```

5. Start the application:

   ```bash
   mvn spring-boot:run
   ```

## API Endpoints

### Create Task

- **Endpoint:** `POST /api/tasks`
- **Description:** Creates a new task.
- **Request Body:**
   ```json
   {
     "taskName": "Task Name",
     "dueDate": "2025-03-20T18:08:05.549Z",
     "userId": "User ID",
     "taskDescription": "Task description",
     "completed": true
  }
   ```
- **Response:**
    - `200 OK`: Task created successfully.
    - `400 Bad Request`: Invalid input.

### Get Task

- **Endpoint:** `GET /api/tasks/{id}`
- **Description:** Retrieves the details of a task by its ID.
- **Path Parameter:**
    - `id` (required): The ID of the task you want to retrieve.
- **Response:**
    - `200 OK`: Task retrieved successfully.
    - `404 Not Found`: Task not found.

### Update Task

- **Endpoint:** `PUT /api/tasks/{id}`
- **Description:** Updates an existing task.
- **Path Parameter:**
    - `id` (required): The ID of the task to update.
- **Request Body:**
   ```json
   {
     "taskName": "Updated Task Name",
     "description": "Updated description",
     "dueDate": "2025-03-22T14:00:00",
     "completed": true
   }
   ```
- **Response:**
    - `200 OK`: Task updated successfully.
    - `400 Bad Request`: Invalid input.
    - `404 Not Found`: Task not found.

### Delete Task

- **Endpoint:** `DELETE /api/tasks/{id}`
- **Description:** Deletes a task by its ID.
- **Path Parameter:**
    - `id` (required): The ID of the task to delete.
- **Response:**
    - `200 OK`: Task deleted successfully.
    - `404 Not Found`: Task not found.

### Get Notifications

- **Endpoint:** `GET /api/notifications`
- **Description:** Retrieves all notifications.
- **Response:**
    - `200 OK`: Notifications retrieved successfully.
    - `404 Not Found`: No notifications found.

## Authentication

The API uses Spring Security to handle user authentication. The application requires a user to be authenticated before performing any operations. Ensure that the user is logged in using JWT or other authentication methods.

For JWT-based authentication, include the token in the `Authorization` header:

```
Authorization: Bearer {JWT_TOKEN}
```

## Kafka Integration

The application uses Kafka for event-driven architecture. A Kafka consumer listens for events on the `task-events` topic. When a task is marked as completed, an event is produced and consumed by the API.

### Events:

- **TASK_COMPLETED:** When a task is marked as completed, the event is sent, and notifications are triggered.

## Error Handling

Errors are returned in a standard format:

```json
{
  "status": "error",
  "message": "Error message describing the issue",
  "details": "Additional details (optional)"
}
```

### Common Error Codes:

- `400 Bad Request`: Invalid input data.
- `404 Not Found`: Resource not found.
- `500 Internal Server Error`: Server error, please try again later.

## Development Setup

1. Clone the repository as mentioned above.
2. Set up a local Kafka instance if not already available. You can use Docker to run Kafka locally:

   ```bash
   docker-compose -f kafka-docker-compose.yml up
   ```

3. Build and run the application using Maven or your preferred build tool.
4. Use Postman or any API client to interact with the API.

## Testing

You can write unit tests for your services using JUnit. Tests should cover all business logic, including task creation, updates, and Kafka event handling.

- **Unit Tests:** Located in the `src/test/java/com/hask/hasktask` directory.
- **Test Example:**
   ```java
   @Test
   public void testCreateTask() {
       // Test task creation logic
   }
   ```

## License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

---

Feel free to modify this file based on your specific use case and project requirements.
```

### Notes:
- You can adjust the endpoint descriptions, payloads, and authentication details based on your actual API structure.
- The section on **Kafka Integration** can be expanded if you have more specific event types or Kafka consumer configurations.
