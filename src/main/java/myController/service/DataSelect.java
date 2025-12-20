package myController.service;

import org.bson.Document;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class DataSelect {

    private final JdbcTemplate jdbcTemplate;
    private final MongoTemplate mongoTemplate;

    public DataSelect(JdbcTemplate jdbcTemplate, MongoTemplate mongoTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.mongoTemplate = mongoTemplate;
    }

    // этот метод — обычный SELECT
    public Integer countByName(String name) {
        String sql = """
            SELECT COUNT(*) 
            FROM staff
            WHERE name = ?
        """;
        return jdbcTemplate.queryForObject(sql, Integer.class, name);
    }

    // тут запрашиваю  SELECT через функцию PostgreSQL
    public Integer countByNameFunction(String name) {
        String sql = "SELECT count_staff_by_name(?)";  // вызов функции
        return jdbcTemplate.queryForObject(sql, Integer.class, name);
    }

    // Получение SELECT  из коллекции "it" по имени сотрудника
    public List<Document> findByName(String name) {
        Query query = new Query(Criteria.where("name").is(name))
                .limit(2);   // ← ограничение на 2 документа

        return mongoTemplate.find(query, Document.class, "it");
    }

}
