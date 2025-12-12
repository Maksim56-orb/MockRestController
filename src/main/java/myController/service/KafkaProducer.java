package myController.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Slf4j
@Service //делает клас сервисом
@RequiredArgsConstructor //автоматически создаёт конструктор для финальных полей
public class KafkaProducer {

    private final KafkaTemplate<String, String> kafkaTemplate; //шаблон для отправки сообщений в кафку

    public void sendMessage(String topic, String payload) { // принимает два аргумента, название топика и содержимое сообщения
        String messageId = UUID.randomUUID().toString(); //уникальный идентификатор
        String timestamp = String.valueOf(System.currentTimeMillis());// отправка времени в милисекундах
        String sender = "Kakoe to soobschenie"; //отправляет

        String fullMessage = String.format("ID:%s | Time:%s | Sender:%s | Payload:%s",
                messageId, timestamp, sender, payload); //итоговое сообщение

        kafkaTemplate.send(topic, fullMessage);//непосредственно
        log.info("Message sent to topic {} with payload {}", topic, fullMessage);
    }
}


