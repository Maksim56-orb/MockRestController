package myController.service;

import org.bson.Document;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class MongoInsertService {

    private final MongoTemplate mongoTemplate;

    public MongoInsertService(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    public void insertSampleData() {
        // создаём тестовый документ
        Document sample = new Document();
        sample.put("name", "Test User");
        sample.put("email", "test@example.com");
        sample.put("createdAt", LocalDateTime.now());

        // вставляем в коллекцию "users"
        mongoTemplate.insert(sample, "users");
    }
}
