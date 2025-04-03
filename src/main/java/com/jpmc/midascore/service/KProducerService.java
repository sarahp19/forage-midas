package com.jpmc.midascore.service;


import com.jpmc.midascore.foundation.Transaction;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class KProducerService {

    private static final String TOPIC = "my_topic";

    private final KafkaTemplate<String, Transaction> kafkaTemplate;

    public KProducerService(KafkaTemplate<String, Transaction> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }


    public void sendMessage(Transaction transaction) {
        kafkaTemplate.send(TOPIC, transaction);
        System.out.println("Message sent: " + transaction);
    }
}
