package myController.service;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class DataSelect {

    private final JdbcTemplate jdbcTemplate;

    public DataSelect(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public Integer countByName(String name) {
        String sql = """
            SELECT COUNT(*) 
            FROM staff
            WHERE name = ?
        """;

        return jdbcTemplate.queryForObject(sql, Integer.class, name);
    }
}
