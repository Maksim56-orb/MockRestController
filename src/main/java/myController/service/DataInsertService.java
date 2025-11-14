package myController.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.sql.*;

@Slf4j
@Service
public class DataInsertService {

    private static final String URL = "jdbc:postgresql://92.242.61.11:5432/mydb";
    private static final String USER = "postgres";
    private static final String PASSWORD = "12345";

    public void insertSampleData() {
        String sql = "INSERT INTO newtable (name) VALUES (?)";

        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            for (int i = 1; i <= 15; i++) {
                pstmt.setString(1, "Name " + i);
                pstmt.executeUpdate();
            }

            log.info("15 записей успешно добавлены в базу данных.");
        } catch (SQLException e) {
            log.error("Ошибка при вставке данных: {}", e.getMessage(), e);
            throw new RuntimeException("Ошибка при вставке данных", e);
        }
    }
}
