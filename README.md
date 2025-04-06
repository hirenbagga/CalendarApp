# HASK TASK API DOCUMENTATION:

## Table of Contents

- [Introduction](#introduction)
- [Possible Architectures](#possible-architectures)
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

The Hask Task API allows you to manage tasks and notifications. You can create tasks, update them, and monitor their
status through notifications. The API integrates with Kafka to receive event-driven messages related to task updates.
> [API Endpoints Documentation Link] (http://localhost:8080/swagger-ui/index.html)
> [IF SERVER IS UP OR DOWN] (http://localhost:8181/actuator/health)

```markdown
# Modular Design for Scalability:

This structure follows best practices for modular design and separation of concerns,
allowing your backend and frontend to scale efficiently as the project grows.
Each component is encapsulated in its own package or directory, making the codebase
easier to maintain, extend, and test.

src
├── main
│         
├── resources
│ ├── application.yml # Main application configuration
│ │     └── application-dev.yml # Development environment configuration
│ └── template
│
java
├── com
│   └── hask
│      └── hasktask
            │
            ├── config # Configuration files (Security, Kafka, etc.)
            │       │
            │       ├── ApplicationConfig.java # General application configurations
            │       ├── JwtAuthenticationFilter.java # JWT Authentication filter
            │       ├── KafkaConfig.java # Kafka producer/consumer configurations
            │       ├── OpenAPIConfig.java # OpenAPI (Swagger) configurations
            │       ├── SecurityConfig.java # Security configurations (Spring Security, CORS, etc.)
            │       └── services # Helper services (JWT, logout, etc.)
            │           ├── <JWTServ>     </JWTServ>ice.java # Handles JWT token generation/validation
            │           └── LogoutService.java # Handles user logout
            │
            ├── controller # REST Controllers (API endpoints)
            │       ├── AuthenticationController.java # Handles login, registration, etc.
            │       ├── EventController.java # Manages event-related API calls
            │       ├── TaskController.java # Manages task-related API calls
            │       ├── TimerController.java # Manages timer-related API calls
            │       └── UserController.java # Manages user-related API calls
            │
            ├── model # Entities, DTOs, Request/Response objects
            │       ├── AccessToken.java # Model for access tokens
            │       ├── AuthenticateRequest.java # DTO for authentication request
            │       ├── EmailConfirmation.java # Email confirmation model
            │       ├── ChangePasswordRequest.java # DTO for password change request
            │       ├── Event.java # Event entity/model
            │       ├── JWTResponse.java # JWT token response DTO
            │       ├── VerificationToken.java # Model for verification tokens
            │       ├── RefreshToken.java # Refresh token model
            │       ├── RegisterRequest.java # DTO for user registration
            │       ├── Timer.java # Timer entity/model
            │       ├── Task.java # Task entity/model
            │       └── User.java # User entity/model
            │
            ├── service # Business logic and services
            │       ├── AccessTokenService.java # Handles access token-related logic
            │       ├── AuthenticationService.java # Handles authentication-related business logic
            │       ├── VerificationTokenService.java # Handles email confirmation logic
            │       ├── EventService.java # Handles event-related business logic
            │       ├── KafkaConsumer.java # Kafka consumer for event processing
            │       ├── EventDueReminder.java # Handles event due reminders
            │       ├── TaskDueReminder.java # Handles task due reminders
            │       ├── NotificationService.java # Handles steveNotification sending logic
            │       ├── RefreshTokenService.java # Handles refresh token-related logic
            │       ├── Role.java # Handles roles/permissions logic
            │       ├── TaskService.java # Task-related business logic
            │       ├── TimerService.java # Timer-related business logic
            │       ├── TokenType.java # Defines token types (e.g., access, refresh)
            │       └── UserService.java # Handles user-related business logic
            │
            ├── repository # JPA repositories for data access
            │       ├── AccessTokenRepository.java # Repository for AccessToken entity
            │       ├── EventRepository.java # Repository for Event entity
            │       ├── VerificationTokenRepository.java # Repository for verification tokens
            │       ├── RefreshTokenRepository.java # Repository for RefreshToken entity
            │       ├── TaskRepository.java # Repository for Task entity
            │       ├── TimerRepository.java # Repository for Timer entity
            │       └── UserRepository.java # Repository for User entity
            │
            ├── customException # Custom exceptions and handlers
            │       ├── CustomAccessDeniedHandler.java # Handles 403 (access denied) errors
            │       ├── CustomAccessDeniedHandler.java # Handles 403 (access denied) errors
            │       ├── CustomAuthenticationEntryPoint.java # Handles 401 (unauthorized) errors
            │       ├── CustomNotFound.java # Custom exception for 404 errors
            │       ├── EntityNotFoundException.java # Custom exception for entity not found
            │       ├── GeneralException.java # General exception handler
            │       ├── RestExceptionHandler.java # Global exception handler for REST APIs
            │       │
            │       └── apiError # Error handling and response models
            │               ├── ApiError.java # Generic API error response model
            │               ├── ApiErrorResponse.java # API error response structure
            │               └── ApiValidationError.java # Validation error handling
            │
            ├── event # Kafka event Producers 
            │       ├── AccountProducer.java # User Account producer for event publishing
            │       ├── EventProducer.java # Calender Event producer for event publishing
            │       └── TaskProducer.java # Calender Task producer for event publishing
            │
            └── listener # Kafka Event Consumers/Listeners
                    ├── AccountConsumer.java # User Account consumer for event subscription
                    ├── EventConsumer.java # Calender Event consumer for event subscription
                    └── TaskConsumer.java # Calender Task consumer for event subscription

# Hask Task API Documentation

Welcome to the Hask Task API! This document serves as a guide for developers to understand how to interact with the API,
the available endpoints, and how to use them effectively.

````

## Possible Architectures

- **Client-Server Architecture:** The API follows a client-server architecture, where the client (frontend) interacts
  with the server (backend) to perform various operations.
- ![Screenshot 2025-04-01 at 10.24.23 AM.png](src/main/resources/static/Screenshot%202025-04-01%20at%2010.24.23%E2%80%AFAM.png)
- **Event-Based Architecture:** The API integrates with Kafka to handle event-driven messages related to task updates
  and notifications.
- ![Screenshot 2025-04-01 at 10.22.54 AM.png](src/main/resources/static/Screenshot%202025-04-01%20at%2010.22.54%E2%80%AFAM.png)
- **RESTful API Design:** The API is designed following RESTful principles, with endpoints for creating, updating, and
  deleting tasks, as well as retrieving notifications.
- **JWT Authentication:** The API uses JWT tokens for user authentication, ensuring secure access to the endpoints.
- **Error Handling:** The API provides detailed error messages and status codes for different scenarios, making it
  easier to debug and troubleshoot issues.
- **Modular Design:** The API is structured in a modular way, with separate packages for controllers, services, models,
  and repositories, making it easier to maintain and extend.
- **Unit Testing:** The API includes unit tests for services and controllers, ensuring that the business logic is tested
  and working as expected.
- **Documentation:** The API documentation provides detailed information about the available endpoints, request/response
  payloads, and authentication requirements.
- **Scalability:** The API is designed to be scalable, allowing you to add new features, endpoints, or integrations as
  needed.
- **Security:** The API includes security features such as JWT authentication, role-based access control, and error
  handling to protect against common security threats.
- **Notifications:** The API includes functionality to send notifications to users based on task updates, providing
  real-time updates and reminders.
- **Configuration:** The API includes configuration files for Kafka, Spring Security, and other settings, making it easy
  to customize and deploy in different environments.

## Getting Started

1. Clone the repository:

```bash
git clone https://github.com/your-repo/hask-task.git
```

2. Navigate to the project directory:

   ```bash
   cd hask-task
   ```

3. Kafka Configuration:

    - Download and unzip Kafka from the [Apache Kafka website](https://kafka.apache.org/quickstart).
    - Rename the unzip folder to `kafka_server`.
    - Change directory(cd) into the `kafka_server` folder.
      ```bash
      cd kafka_server
      ```
        - Generate a Cluster UUID for the Kafka server:
          ```bash
          KAFKA_CLUSTER_ID="$(bin/kafka-storage.sh random-uuid)"
          ```
          - Format Log Directories for the Kafka server:
            ```bash
            bin/kafka-storage.sh format --standalone -t $KAFKA_CLUSTER_ID -c config/server.properties
            ```
          - Run the following command to start Kafka server:
            ```bash
            bin/kafka-server-start.sh config/server.properties
            ```
    - TO READ THE EVENTS: Open a new terminal. Then run the following command:
        ```bash
        bin/kafka-console-consumer.sh --topic hask-task-app --from-beginning --bootstrap-server localhost:9092
        ```
    - By default, Kafka server runs on `localhost:9092`.

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

The API uses Spring Security to handle user authentication. The application requires a user to be authenticated before
performing any operations. Ensure that the user is logged in using JWT or other authentication methods.

For JWT-based authentication, include the token in the `Authorization` header:

```
Authorization: Bearer {JWT_TOKEN}
```

## Kafka Integration

The application uses Kafka for event-driven architecture. A Kafka consumer listens for events on the `task-events`
topic. When a task is marked as completed, an event is produced and consumed by the API.

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

You can write unit tests for your services using JUnit. Tests should cover all business logic, including task creation,
updates, and Kafka event handling.

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
