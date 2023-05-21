package gov.iti.jets.testing.demo.impl;

import gov.iti.jets.testing.service.FileService;
import gov.iti.jets.testing.service.impl.FileServiceImpl;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.client.AutoConfigureMockRestServiceServer;
import org.springframework.boot.test.autoconfigure.web.client.RestClientTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.test.web.client.match.MockRestRequestMatchers;
import org.springframework.test.web.client.response.MockRestResponseCreators;
import org.springframework.web.client.RestTemplate;

import java.io.File;
import java.io.IOException;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@RestClientTest(FileServiceImpl.class)
class FileServiceImplTest {

    @Value("classpath:/image.png")
    private Resource image;

    @Autowired
    private FileService fileService;

    @Autowired
    private MockRestServiceServer server;

    private String mediaServerURL = "/url";


    @Test
    void send_image_successfully() throws IOException {

        File file  = image.getFile();

        server.expect(MockRestRequestMatchers.requestTo(mediaServerURL))
                .andRespond(MockRestResponseCreators
                        .withSuccess("{\"path\": \"new-path\"}",
                                MediaType.APPLICATION_JSON));

        String path = fileService.saveFile(file, mediaServerURL);
        assertThat(path).isEqualTo("\"new-path\"");
    }

}