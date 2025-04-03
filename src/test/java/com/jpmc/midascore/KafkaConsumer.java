package com.jpmc.midascore;


//import com.jpmc.midascore.entity.TransactionRecord;
import com.jpmc.midascore.entity.TransactionRecord;
import com.jpmc.midascore.entity.UserRecord;
import com.jpmc.midascore.foundation.Transaction;
import com.jpmc.midascore.foundation.Incentive;
import com.jpmc.midascore.repository.TransRepository;
import com.jpmc.midascore.repository.UserRepository;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
//import com.jpmc.midascore.foundation.Incentive;


@Component
public class KafkaConsumer {

    private final UserRepository userRepository;
    private final TransRepository transRepository;

    private final RestTemplate restTemplate;

    //    private final UserRepository userRepository;
//    private final TransactionRepository transactionRepository;
//    private final RestTemplate restTemplate;
    public KafkaConsumer(UserRepository userRepository, TransRepository transRepository, RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
        this.userRepository = userRepository;
        this.transRepository = transRepository;
    }

    @KafkaListener(topics = "${general.kafka-topic}", groupId = "midas-core-group")
//    public void listen(String message) {
//        System.out.println("Received message: " + message);
//    }
    public void listen(ConsumerRecord<String, Transaction> record) {
        Transaction transaction = record.value();
        System.out.println("Transaction Amount: " + transaction.getAmount());

        long sendId = transaction.getSenderId();
        long recId = transaction.getRecipientId();
        UserRecord sender = userRepository.findById(sendId);
        UserRecord receiver = userRepository.findById(recId);

        if (sender != null && receiver != null) {
            float sendBal = sender.getBalance();
            float transAmt = transaction.getAmount();
            if (sendBal >= transAmt) {
                float sendNewBal = sendBal - transAmt;
                sender.setBalance(sendNewBal);

                String incentiveAPI = "http://localhost:8080/incentive";

                Incentive incentive = restTemplate.postForObject(incentiveAPI, transaction, Incentive.class);
                float incentiveAdd = 0;
                if (incentive != null) {
                    incentiveAdd = incentive.getAmount();
                }

                float recNewBal = receiver.getBalance() + transAmt + incentiveAdd;
                receiver.setBalance(recNewBal);

                userRepository.save(sender);
                userRepository.save(receiver);

                TransactionRecord transRecord = new TransactionRecord(sender, receiver, transAmt);
                transRepository.save(transRecord);
                System.out.println(receiver.getName() + "balance is " + receiver.getBalance());
            }
            else {
                System.out.println("Error");
            }
        }
        else {
            System.out.println("Error");
        }
    }
//    public void listen(ConsumerRecord<String, Transaction> record) {
//        Transaction transaction = record.value();
//        System.out.println("Received Transaction: " + transaction.getAmount());
//
////        UserRecord sender = userRepository.findById(transaction.getSenderId());
////        UserRecord recipient = userRepository.findById(transaction.getRecipientId());
////
////        if (sender != null && recipient != null) {
////
////            if (sender.getBalance() >= transaction.getAmount()) {
////                sender.setBalance(sender.getBalance() - transaction.getAmount());
////
////
////                // Get incentive amount from API
////                String incentiveApiUrl = "http://localhost:8080/incentive";
////                Incentive incentive = restTemplate.postForObject(incentiveApiUrl, transaction, Incentive.class);
////
////                float incentiveAmount = (incentive != null) ? incentive.getAmount() : 0;
////
////                // Add to recipient (transaction amount + incentive)
////                recipient.setBalance(recipient.getBalance() + transaction.getAmount() + incentiveAmount);
////
////
////                userRepository.save(sender);
////                userRepository.save(recipient);
////
////                TransactionRecord transactionRecord = new TransactionRecord(sender, recipient, transaction.getAmount());
////                transactionRepository.save(transactionRecord);
////
////                System.out.println("Transaction processed successfully!");
////                System.out.println("Transaction processed successfully! Incentive applied: " + incentiveAmount);
////
////            } else {
////                System.out.println("Transaction rejected: Insufficient funds.");
////            }
////        } else {
////            System.out.println("Transaction rejected: Invalid sender or recipient.");
////        }
//    }
}