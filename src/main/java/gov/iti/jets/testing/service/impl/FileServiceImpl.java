package gov.iti.jets.testing.service.impl;

import com.fasterxml.jackson.databind.node.ObjectNode;
import gov.iti.jets.testing.service.FileService;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.io.File;
import java.time.Duration;

@Service
public class FileServiceImpl implements FileService {

    //Web Client
    private final RestTemplate restTemplate;

    public FileServiceImpl(RestTemplateBuilder restTemplateBuilder) {
        this.restTemplate = restTemplateBuilder
                .rootUri("https://media-server-url")
                .setConnectTimeout(Duration.ofSeconds(2))
                .setReadTimeout(Duration.ofSeconds(2))
                .build();
    }

    @Override
    public String saveFile(File file, String URL) {
        HttpHeaders headers = new HttpHeaders();
        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
        body.add("file", new FileSystemResource(file));
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);

        HttpEntity<MultiValueMap<String, Object>> requestEntity =
                new HttpEntity<>(body, headers);

        ObjectNode body1 = restTemplate
                .exchange(URL, HttpMethod.POST, requestEntity, ObjectNode.class)
                .getBody();

        String path = String.valueOf(body1.get("path"));
        return path;
    }
}
