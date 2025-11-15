package myController;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import myController.service.DataInsertService;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;


@Slf4j
@RestController
@RequiredArgsConstructor
public class CheckController {

    private final ObjectMapper objectMapper = new ObjectMapper();
    private final DataInsertService dataInsertService;

    @GetMapping("/v1/check")
    public String check() {
        log.info("got request /v1/check successfully");
        return "Request /v1/check completed!";
    }

    @PostMapping("/v2/checkJson")
    public ResponseEntity<Map<String, String>> checkJson(@RequestBody Map<String, Object> inputJson) {
        log.info("got request /v2/checkJson successfully: {}", inputJson);

        Map<String, String> response = Map.of(
                "message", "Request /v2/checkJson completed!"
        );
        return ResponseEntity.ok(response);
    }

    @RequestMapping("/v3/checkHeaders")
    public Map<String, Object> checkRequest(@RequestHeader Map<String, String> headers) {
        log.info("Got request /check");
        Map<String, Object> response = new LinkedHashMap<>();
        response.put("message", "Tvoy zapros");
        response.put("headers", headers);
        log.info("Log return value: {}", response);
        return response;
    }

    @PostMapping("/v4/checkPlusTelo")
    public Map<String, Object> checkRequest(
            @RequestHeader Map<String, String> headers,
            HttpServletRequest request
    ) throws IOException {
        Map<String, Object> response = new LinkedHashMap<>();
        response.put("message", "Tvoy zapros");
        response.put("headers", headers);

        //прочитаем тело запроса как строку.
        String body = request.getReader().lines()
                .reduce("", (accumulator, actual) -> accumulator + actual);

        //пытаемся распарсить как JSON
        Object parsedData;
        try {
            parsedData = objectMapper.readValue(body, Map.class);
        } catch (Exception e) {
            parsedData = body.isBlank() ? Map.of() : body;  //если не получилось распарсить как JSON, то просто считаем, что это тело запроса
        }

        response.put("Dannye tvoego zaprosa", parsedData);
        log.info("Got request /checkPlusTelo");
        log.info("Log return value: {}", response);
        return response;
    }

    @PostMapping("/v5/sendToDB")
    public ResponseEntity<String> insertData() {
        log.info("Got request /v5/sendToDB");
        dataInsertService.insertSampleData();

        return ResponseEntity.ok("Отправка данных в БД прошла успешно!");
    }

}
