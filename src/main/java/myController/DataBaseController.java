package myController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import myController.service.*;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@Slf4j
@Tag(name = "CheckController", description = "Контроллер с методами для работы с Базами Данных")
@RestController
@RequiredArgsConstructor

public class DataBaseController {

        //информация по методам контроллера описана в Swagger  http://92.242.61.11:9001/swagger-ui/index.html
        // PostgreSQL
    private final DataInsertService dataInsertService;
    private final DataSelect dataSelect; //правлю сейчас
    @PostMapping("/v5/sendToDB")
    @Operation(summary = "Post Метод /v5/sendToDB", description = "Данный метод, предназначен для наполнения базы данных.")

    public ResponseEntity<String> insertData() {
        log.info("Got request /v5/sendToDB");
        dataInsertService.insertSampleData();
        return ResponseEntity.ok("Отправка данных в БД прошла успешно!");
    }

    // этот метод — вызывает PostgreSQL-запрос

    @GetMapping("/staff/count")
    public String countStaffByName(@RequestParam String name) {
        Integer count = dataSelect.countByName(name);
        return "Количество сотрудников с именем " + name + ": " + count;
    }

    // этот метод — вызывает PostgreSQL-функцию
    @GetMapping("/staff/count/function")
    public String countStaffByNameFunction(@RequestParam String name) {
        Integer count = dataSelect.countByNameFunction(name);
        return "Количество сотрудников (через функцию) с именем " + name + ": " + count;
    }

    //Метод добавления данных в MongoDB
    private final MongoInsertService mongoInsertService;

    @PostMapping("/sendToMongo")
    @Operation(summary = "Post Метод /sendToMongo", description = "Метод для наполнения MongoDB тестовыми данными.")
    public ResponseEntity<String> insertDataToMongo() {
        log.info("Got request /sendToMongo");
        mongoInsertService.insertSampleData();
        return ResponseEntity.ok("Отправка данных в MongoDB в коллекцию it прошла успешно!");
    }

}
