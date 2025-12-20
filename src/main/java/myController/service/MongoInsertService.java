package myController.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

import org.bson.Document;

@Slf4j
@Service
public class MongoInsertService {

    private final MongoTemplate mongoTemplate;

    public MongoInsertService(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    public void insertSampleData() {
        List<String> names = List.of("Alisa", "Boris", "Cosmos", "Dmitry", "Elena");
        List<String> positions = List.of("Developer", "QA", "Manager", "Analyst", "Performance tester");
        String company = "Digital Technology";

        String name = names.get(ThreadLocalRandom.current().nextInt(names.size()));
        String position = positions.get(ThreadLocalRandom.current().nextInt(positions.size()));
        int age = ThreadLocalRandom.current().nextInt(28, 46);
        BigDecimal salary = BigDecimal.valueOf(ThreadLocalRandom.current().nextInt(3000, 4001));
        String phone = generatePhoneNumber();
        String email = generateEmail(name);

        Document doc = new Document();
        doc.put("name", name);
        doc.put("age", age);
        doc.put("position", position);
        doc.put("average_salary", salary);
        doc.put("company", company);
        doc.put("phone_number", phone);
        doc.put("email", email);
        doc.put("createdAt", Date.from(LocalDateTime.now().atZone(ZoneId.systemDefault()).toInstant()));

        mongoTemplate.insert(doc, "it"); // вставка в коллекцию "it"
        log.info("Документ с именем '{}' успешно добавлен в коллекцию 'it'", name);
    }

    private String generatePhoneNumber() {
        StringBuilder phone = new StringBuilder("+7");
        for (int i = 0; i < 10; i++) {
            phone.append(ThreadLocalRandom.current().nextInt(0, 10));
        }
        return phone.toString();
    }

    private String generateEmail(String name) {
        String cleanName = name.toLowerCase().replaceAll("[^a-z0-9]", "");
        int suffix = ThreadLocalRandom.current().nextInt(100, 999);
        return cleanName + suffix + "@mail.ru";
    }
}
