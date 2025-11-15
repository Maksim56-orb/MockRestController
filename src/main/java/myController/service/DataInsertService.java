package myController.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.sql.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

@Slf4j
@Service
public class DataInsertService {

    private static final String URL = "jdbc:postgresql://92.242.61.11:5432/mydb";
    private static final String USER = "postgres";
    private static final String PASSWORD = "12345";
    private static final String NAMES_FILE = "names.csv";
    private static final String POSITIONS_FILE = "positions.csv";
    private static final String COMPANY_NAME = "Digital Technology";

    public void insertSampleData() {
        List<String> names = loadValuesFromCSV(NAMES_FILE);
        List<String> positions = loadValuesFromCSV(POSITIONS_FILE);

        if (names.size() < 5 || positions.size() < 5) {
            throw new RuntimeException("Недостаточно данных в CSV-файлах.");
        }

        Collections.shuffle(names);
        Collections.shuffle(positions);

        String sql = """
            INSERT INTO staff (name, age, position, average_salary, company, phone_number, email)
            VALUES (?, ?, ?, ?, ?, ?, ?)
            """;

        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            conn.setAutoCommit(false);

            for (int i = 0; i < 5; i++) {
                String name = names.get(i);
                String position = positions.get(i);
                int age = ThreadLocalRandom.current().nextInt(28, 46); // возраст от 28 до 45 включительно
                BigDecimal salary = BigDecimal.valueOf(ThreadLocalRandom.current().nextInt(3000, 4001));
                String phone = generatePhoneNumber();
                String email = generateEmail(name);

                pstmt.setString(1, name);
                pstmt.setInt(2, age);
                pstmt.setString(3, position);
                pstmt.setBigDecimal(4, salary);
                pstmt.setString(5, COMPANY_NAME);
                pstmt.setString(6, phone);
                pstmt.setString(7, email);
                pstmt.addBatch();
            }


            pstmt.executeBatch();
            conn.commit();
            log.info("5 сотрудников успешно добавлены в таблицу staff.");
        } catch (Exception e) {
            log.error("Ошибка при вставке данных: {}", e.getMessage(), e);
            throw new RuntimeException("Ошибка при вставке данных", e);
        }
    }

    private List<String> loadValuesFromCSV(String fileName) {
        List<String> values = new ArrayList<>();
        try {
            ClassPathResource resource = new ClassPathResource(fileName);
            try (BufferedReader reader = new BufferedReader(
                    new InputStreamReader(resource.getInputStream(), StandardCharsets.UTF_8))) {

                String line;
                boolean skipHeader = true;
                while ((line = reader.readLine()) != null) {
                    if (skipHeader) {
                        skipHeader = false;
                        continue;
                    }
                    values.add(line.trim());
                }
            }
        } catch (Exception e) {
            log.error("Ошибка при чтении CSV '{}': {}", fileName, e.getMessage(), e);
        }
        return values;
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






//package myController.service;
//
//
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.core.io.ClassPathResource;
//import org.springframework.stereotype.Service;
//
//import java.io.BufferedReader;
//import java.io.InputStreamReader;
//import java.math.BigDecimal;
//import java.nio.charset.StandardCharsets;
//import java.sql.*;
//import java.util.ArrayList;
//import java.util.Collections;
//import java.util.List;
//import java.util.concurrent.ThreadLocalRandom;
//
//@Slf4j
//@Service
//public class DataInsertService {
//
//    private static final String URL = "jdbc:postgresql://92.242.61.11:5432/mydb";
//    private static final String USER = "postgres";
//    private static final String PASSWORD = "12345";
//    private static final String NAMES_FILE = "names.csv";
//    private static final String POSITIONS_FILE = "positions.csv";
//
//    public void insertSampleData() {
//        List<String> names = loadValuesFromCSV(NAMES_FILE);
//        List<String> positions = loadValuesFromCSV(POSITIONS_FILE);
//
//        if (names.size() < 5 || positions.size() < 5) {
//            throw new RuntimeException("Недостаточно данных в CSV-файлах.");
//        }
//
//        Collections.shuffle(names);
//        Collections.shuffle(positions);
//
//        String sql = "INSERT INTO employees (name, age, position, average_salary) VALUES (?, ?, ?, ?)";
//        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
//             PreparedStatement pstmt = conn.prepareStatement(sql)) {
//
//            conn.setAutoCommit(false);
//
//            for (int i = 0; i < 5; i++) {
//                String name = names.get(i);
//                String position = positions.get(i);
//                int age = 20 + i;
//                int salary = ThreadLocalRandom.current().nextInt(2500, 3501); // случайная зарплата от 3000 до 4000
//
//                pstmt.setString(1, name);
//                pstmt.setInt(2, age);
//                pstmt.setString(3, position);
//                pstmt.setBigDecimal(4, new BigDecimal(salary));
//                pstmt.addBatch();
//            }
//
//            pstmt.executeBatch();
//            conn.commit();
//            log.info("5 сотрудников с рандомными именами и должностями добавлены из двух CSV-файлов.");
//        } catch (Exception e) {
//            log.error("Ошибка при вставке данных: {}", e.getMessage(), e);
//            throw new RuntimeException("Ошибка при вставке данных", e);
//        }
//    }
//
//    private List<String> loadValuesFromCSV(String fileName) {
//        List<String> values = new ArrayList<>();
//        try {
//            ClassPathResource resource = new ClassPathResource(fileName);
//            try (BufferedReader reader = new BufferedReader(
//                    new InputStreamReader(resource.getInputStream(), StandardCharsets.UTF_8))) {
//
//                String line;
//                boolean skipHeader = true;
//                while ((line = reader.readLine()) != null) {
//                    if (skipHeader) {
//                        skipHeader = false;
//                        continue;
//                    }
//                    values.add(line.trim());
//                }
//            }
//        } catch (Exception e) {
//            log.error("Ошибка при чтении CSV '{}': {}", fileName, e.getMessage(), e);
//        }
//        return values;
//    }
//}
