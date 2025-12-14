package myController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import myController.service.*;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@Slf4j
@Tag(name = "CheckController", description = "Тетсовый ко контроллер")
@RestController
@RequiredArgsConstructor

public class DataBaseController {

        //информация по методам контроллера описана в Swagger  http://92.242.61.11:9001/swagger-ui/index.html

    private final DataInsertService dataInsertService;
    @PostMapping("/v5/sendToDB")
    @Operation(summary = "Post Метод /v5/sendToDB", description = "Данный метод, предназначен для наполнения базы данных.")

    public ResponseEntity<String> insertData() {
        log.info("Got request /v5/sendToDB");
        dataInsertService.insertSampleData();
        return ResponseEntity.ok("Отправка данных в БД прошла успешно!");
    }
    private final DataSelect dataSelect;

    // этот метод — вызывает PostgreSQL-функцию

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


}
