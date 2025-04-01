package com.hask.hasktask.service;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@EnableKafka  // Enable Kafka listener in Spring Boot
@Component    // Register KafkaConsumer as a Spring bean
public class KafkaConsumer {

    // Kafka listener for user-task events (e.g., TASK_CREATED, TASK_COMPLETED, TASK_DELETED)
    @KafkaListener(topics = "${haskTask.app.topics.taskTopic}", groupId = "task-consumer-group")
    public void listenTaskEvents(ConsumerRecord<String, String> record) {
        String message = record.value();

        if (message.contains("TASK_CREATED")) {
            long taskId = Long.parseLong(message.split("=")[1].split(",")[0]);
            String taskName = message.split("=")[2].split(",")[0];
            System.out.println("Task created: ID=" + taskId + ", Name=" + taskName);

        } else if (message.contains("TASK_COMPLETED")) {

            long taskId = Long.parseLong(message.split("=")[1]);
            System.out.println("Task completed: ID=" + taskId);

        } else if (message.contains("TASK_DELETED")) {

            long taskId = Long.parseLong(message.split("=")[1]);
            System.out.println("Task deleted: ID=" + taskId);
        }
    }

    // Kafka listener for user-event events (e.g., EVENT_CREATED, EVENT_DELETED)
    @KafkaListener(topics = "${haskTask.app.topics.eventTopic}", groupId = "event-consumer-group")
    public void listenEventEvents(ConsumerRecord<String, String> record) {
        String message = record.value();

        if (message.contains("EVENT_CREATED")) {
            long eventId = Long.parseLong(message.split("=")[1].split(",")[0]);
            String eventName = message.split("=")[2].split(",")[0];
            System.out.println("Event created: ID=" + eventId + ", Name=" + eventName);

        } else if (message.contains("EVENT_DELETED")) {

            long eventId = Long.parseLong(message.split("=")[1]);
            System.out.println("Event deleted: ID=" + eventId);
        }
    }
}


