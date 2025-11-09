package myController;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@Slf4j
@RestController
public class CheckController {

    @GetMapping("/v1/check")
    public String check() {
        log.info("got request /v1/check successfully");
        return "Request /v1/check completed!";
    }

    @PostMapping("/v3/checkJson")
    public ResponseEntity<Map<String, String>> checkJson(@RequestBody Map<String, Object> inputJson) {
        log.info("got request /v2/checkJson successfully: {}", inputJson);

        Map<String, String> response = Map.of(
                "message", "Request /v2/checkJson completed!"
        );
        return ResponseEntity.ok(response);
    }
}
