package myController.service;

import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class HeaderProvider {
    public HttpHeaders getDefaultHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.add("X-Service-Name", "CheckJsonService");
        headers.add("X-Request-Id", UUID.randomUUID().toString());
        headers.add("X-Environment", "Test");
        return headers;
    }
}
