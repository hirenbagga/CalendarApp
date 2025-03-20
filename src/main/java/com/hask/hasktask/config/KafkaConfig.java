/*
package com.hask.hasktask.config;

import com.hask.hasktask.model.Notification;
import com.hask.hasktask.model.Task;
import com.hask.hasktask.service.NotificationService;
import com.hask.hasktask.service.TaskService;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.listener.ConcurrentMessageListenerContainer;
import org.springframework.kafka.listener.MessageListener;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Configuration
@EnableKafka
public class KafkaConfig {

    @Value("${kafka.bootstrap.servers}")
    private String bootstrapServers;

    @Value("${kafka.consumer.group.id}")
    private String consumerGroupId;

    final private TaskService taskService;
    final private NotificationService notificationService;

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

    @KafkaListener(topics = "task-events", groupId = "${kafka.consumer.group.id}")
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
*/
