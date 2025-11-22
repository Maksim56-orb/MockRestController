package myController.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class KafkaConsumer {

    @KafkaListener(topics = "my-topic", groupId = "new-group")
    public void listen(String message) {
        log.info("Out messages from Kafka: {}", message);
    }
}

