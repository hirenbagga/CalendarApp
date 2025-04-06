/* *
package com.hask.hasktask.service;

import org.apache.kafka.clients.consumer.ConsumerRecord;
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
        processEventMessage(message, "TASK");
    }

    // Kafka listener for user-event events (e.g., EVENT_CREATED, EVENT_DELETED)
    @KafkaListener(topics = "${haskTask.app.topics.eventTopic}", groupId = "event-consumer-group")
    public void listenEventEvents(ConsumerRecord<String, String> record) {
        String message = record.value();
        processEventMessage(message, "EVENT");
    }

    // Generalized method to process task and event messages
    private void processEventMessage(String message, String entityType) {
        // Checking if the message corresponds to a "CREATED" event
        if (message.contains(entityType + "_CREATED")) {
            long id = Long.parseLong(message.split("=")[1].split(",")[0]);
            String name = message.split("=")[2].split(",")[0];
            System.out.println(entityType + " created: ID=" + id + ", Name=" + name);
        }
        // Checking if the message corresponds to "COMPLETED" or "DELETED" events
        else if (message.contains(entityType + "_COMPLETED") || message.contains(entityType + "_DELETED")) {
            long id = Long.parseLong(message.split("=")[1]);
            String action = message.contains(entityType + "_COMPLETED") ? "completed" : "deleted";
            System.out.println(entityType + " " + action + ": ID=" + id);
        }
    }
}
*/
