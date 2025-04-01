
package com.hask.hasktask.config;

import com.hask.hasktask.model.Notification;
import com.hask.hasktask.model.Task;
import com.hask.hasktask.service.NotificationService;
import com.hask.hasktask.service.TaskService;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
//import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
//import org.springframework.kafka.listener.ConcurrentMessageListenerContainer;
//import org.springframework.kafka.listener.MessageListener;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Configuration
@EnableKafka
@ConfigurationProperties(prefix = "kafka")
public class KafkaConfig {

    @Value("${spring.kafka.bootstrap-servers}")
    private String bootstrapServers;

    @Value("${spring.kafka.consumer.group-id}")
    private String consumerGroupId;

    private final TaskService taskService;
    private final NotificationService notificationService;

    @Autowired
    public KafkaConfig(TaskService taskService, NotificationService notificationService) {
        this.taskService = taskService;
        this.notificationService = notificationService;
    }

    // Kafka Template (Producer) for sending events
    @Bean
    public KafkaTemplate<String, String> kafkaTemplate() {
        Map<String, Object> producerProps = new HashMap<>();
        producerProps.put("bootstrap.servers", bootstrapServers);
        producerProps.put("key.serializer", StringSerializer.class);
        producerProps.put("value.serializer", StringSerializer.class);
        return new KafkaTemplate<>(new DefaultKafkaProducerFactory<>(producerProps));
    }

    // Kafka Consumer Configuration (using @KafkaListener)
    @Bean
    public ConsumerFactory<String, String> consumerFactory() {
        Map<String, Object> consumerProps = new HashMap<>();
        consumerProps.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        consumerProps.put(ConsumerConfig.GROUP_ID_CONFIG, consumerGroupId);
        consumerProps.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        consumerProps.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);

        return new DefaultKafkaConsumerFactory<>(consumerProps);
    }

    @KafkaListener(topics = "${haskTask.app.topics.taskTopic}", groupId = "task-consumer-group")
    public void listen(ConsumerRecord<String, String> record) {
        String message = record.value();
        System.out.println("Received Kafka message: " + message);

        // Here you can process the message and invoke business logic
        processMessage(message);
    }

    private void processMessage(String message) {
        // Example: Processing a Task Completed event
        if (message.contains("TASK_COMPLETED")) {
            Long taskId = extractTaskIdFromMessage(message);
            Task task = taskService.getTaskById(taskId);

            if (task != null) {
                task.setCompleted(true);
                taskService.updateTask(taskId, task);  // Updating task status
                sendNotification("Task " + task.getTaskName() + " marked as completed.");
            }
        }

        // You can add more cases here for different event types
    }

    private Long extractTaskIdFromMessage(String message) {
        // Example: Extract task ID from message string
        return Long.parseLong(message.split("=")[1]);
    }

    private void sendNotification(String message) {
        Notification notification = new Notification();
        notification.setMessage(message);
        notification.setTimestamp(LocalDateTime.now());
        notification.setUserId(1L); // Use appropriate userId
        notificationService.createNotification(notification);
    }
}



/*
import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

@Configuration
public class KafkaConfig {

    @Value("${kafka.default-topic}")

    @Bean
    public NewTopic haskTopic() {
        return TopicBuilder.name("alibou").build();
    }
}
*/


/*@Component
public class EventDueChecker {

    @Autowired
    private EventService eventService;

    @Autowired
    private EmailService emailService;

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    @Scheduled(fixedRate = 60000)  // Runs every minute
    public void checkForDueEvents() {
        // Query events that are due now or within a specific time window
        List<Event> dueEvents = eventService.findDueEvents();

        // Use asynchronous processing to handle each event concurrently
        dueEvents.parallelStream().forEach(event -> {
            try {
                // Trigger email notification (synchronously or asynchronously)
                emailService.sendEmail(event.getUserEmail(), "Event Due Reminder",
                        "Your event '" + event.getTitle() + "' is due now.");

                // Optionally, send a Kafka message for further processing (e.g., logging, analytics)
                kafkaTemplate.send("event-due-topic", event.getId().toString());
            } catch (Exception e) {
                // Handle errors such as email failure or Kafka errors
                System.err.println("Failed to send notification for event ID " + event.getId() + ": " + e.getMessage());
            }
        });
    }
}

@Service
public class EventService {

    @Autowired
    private EventRepository eventRepository;

    // Query events that are due now or within the next 5 minutes
    public List<Event> findDueEvents() {
        Date now = new Date();
        Date windowEnd = new Date(now.getTime() + 5 * 60 * 1000);  // Current time + 5 minutes
        return eventRepository.findEventsByDueDateBetween(now, windowEnd);
    }
}

@Component
public class EventDueChecker {

    private static final ExecutorService executorService = Executors.newFixedThreadPool(10);  // Limit to 10 concurrent tasks

    @Autowired
    private EventService eventService;

    @Autowired
    private EmailService emailService;

    @Scheduled(fixedRate = 60000)  // Runs every minute
    public void checkForDueEvents() {
        // Query events that are due now or within a specific time window
        List<Event> dueEvents = eventService.findDueEvents();

        // Use ExecutorService to process each event concurrently
        for (Event event : dueEvents) {
            executorService.submit(() -> {
                try {
                    // Trigger email notification (asynchronously)
                    emailService.sendEmail(event.getUserEmail(), "Event Due Reminder",
                            "Your event '" + event.getTitle() + "' is due now.");

                    // Optionally, send a Kafka message for further processing (e.g., logging, analytics)
                    kafkaTemplate.send("event-due-topic", event.getId().toString());
                } catch (Exception e) {
                    // Handle errors
                    System.err.println("Failed to send notification for event ID " + event.getId() + ": " + e.getMessage());
                }
            });
        }
    }
}*/



