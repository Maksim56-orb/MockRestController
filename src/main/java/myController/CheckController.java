package myController;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import myController.service.FileLoaderJsonRPC;
import myController.service.KafkaProducerService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import myController.service.DataInsertService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;


import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;


@Slf4j
@Tag(name = "CheckController", description = "Тетсовый ко контроллер")
@RestController
@RequiredArgsConstructor
public class CheckController {

    //информация по методам контроллера описана в Swagger  http://92.242.61.11:9001/swagger-ui/index.html

    @GetMapping("/v1/check")
    @Operation(summary = "Get метод /v1/check", description = "Простой метод, предназначен для тестового запроса/проверке соединения и т.д. ")
    public String check() {
        log.info("got request /v1/check successfully");
        return "Request /v1/check completed!";
    }

    @PostMapping("/v2/checkJson")
    @Operation(summary = "Post метод /v2/checkJson", description = "Простой метод, предназначен для тестового запроса/проверке соединения, принимает тело в запросе, отдает тело в ответе.")
    public ResponseEntity<Map<String, String>> checkJson(@RequestBody Map<String, Object> inputJson) {
        log.info("got request /v2/checkJson successfully: {}", inputJson);

        Map<String, String> response = Map.of(
                "message", "Request /v2/checkJson completed!"
        );
        return ResponseEntity.ok(response);
    }

    @GetMapping("/v3/checkHeaders")
    @Operation(summary = "Get Метод /v3/checkHeaders", description = "Данный метод, предназначен для проверки отправляемых заголовков во входящем запросе. Метод едает слепок фактически пришедших заголовков в запросе и возвращает их.")

    public Map<String, Object> checkRequest(@RequestHeader Map<String, String> headers) {
        log.info("Got request /check");
        Map<String, Object> response = new LinkedHashMap<>();
        response.put("message", "Tvoy zapros");
        response.put("headers", headers);
        log.info("Log return value: {}", response);
        return response;
    }

    private final ObjectMapper objectMapper = new ObjectMapper();

    @PostMapping("/v4/checkPlusTelo")
    @Operation(summary = "Post метод /v3/checkHeaders", description = "Данный метод, предназначен для проверки отправляемых заголовков и передаваемого тела во входящем запросе. Метод делает слепок фактически пришедших заголовков и передаваемого тела (json) в запросе и возвращает их списком.")

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

    private final DataInsertService dataInsertService;

    @PostMapping("/v5/sendToDB")
    @Operation(summary = "Post Метод /v5/sendToDB", description = "Данный метод, предназначен для наполнения базы данных.")

    public ResponseEntity<String> insertData() {
        log.info("Got request /v5/sendToDB");
        dataInsertService.insertSampleData();

        return ResponseEntity.ok("Отправка данных в БД прошла успешно!");
    }

    private final KafkaProducerService kafkaProducerService;

    @PostMapping("/v6/sendToKafka")
    @Operation(summary = "Post Метод /v6/sendToKafka", description = "Данный метод отправляет сообщение в Kafka.")
    public ResponseEntity<String> sendToKafka() {
        log.info("Got request /v6/sendToKafka");

        String message = "Моё сообщение для Kafka в топике my-topic";
        kafkaProducerService.sendMessage("my-topic", message);

        return ResponseEntity.ok("Сообщение успешно отправлено в Kafka!");
    }

    private final FileLoaderJsonRPC fileLoader;

    @PostMapping("/v7/jsonRpc")
    @Operation(summary = "Post Метод /v7/jsonRpc", description = "POST метод JsonRPC. Принимает несколько методов на один End-point и в зависомости от метода возвращает разные ответы.")
    public String ihapi(@RequestBody JsonNode json) {
        var method = json.get("method").asText();
        switch (method) {
            case "engagement.user-api.get-profile" -> {
                return fileLoader.getFilesContent().get("get-profile.json");
            }
            case "engagement.user-api.get-collection" -> {
                return fileLoader.getFilesContent().get("get-collection.json");
            }
            case "engagement.user-api.send-event" -> {
                return fileLoader.getFilesContent().get("send-event.json");
            }
            default -> {
                return "404";
            }
        }
    }
}
