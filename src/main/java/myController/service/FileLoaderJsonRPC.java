package myController.service;

import jakarta.annotation.PostConstruct;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;
import org.springframework.util.FileCopyUtils;

import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Map;

@Component
@Slf4j
public class FileLoaderJsonRPC {

@Value("classpath:get-profile.json")
private Resource configFile;

@Value("classpath:get-collection.json")
private Resource getCollection;

@Value("classpath:send-event.json")
private Resource sendEvent;

@Getter
private Map<String, String> filesContent;

@PostConstruct
public void init() {
    filesContent = Map.of(
            getFileName(configFile), getFileContent(configFile),
            getFileName(getCollection), getFileContent(getCollection),
            getFileName(sendEvent), getFileContent(sendEvent)
    );
   // log.info("Files content: {}", filesContent);
}

private String getFileName(Resource resource) {
    return resource.getFilename();
}

private String getFileContent(Resource resource) {
    try {
        InputStream inputStream = resource.getInputStream();
        byte[] bytes = FileCopyUtils.copyToByteArray(inputStream);
        return new String(bytes, StandardCharsets.UTF_8);
    } catch (Exception e) {
        throw new RuntimeException("Failed to load file: " + getFileName(resource), e);
    }
}
}