package gov.iti.jets.testing.service.impl;

import gov.iti.jets.testing.service.FileService;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.client.RestClientTest;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.test.web.client.match.MockRestRequestMatchers;
import org.springframework.test.web.client.response.MockRestResponseCreators;

import java.io.File;
import java.io.IOException;


@RestClientTest(FileServiceImpl.class)
//Wire Mock => actual server
class FileServiceImplTest {

    @Autowired
    private FileService fileService;

    @Autowired
    private MockRestServiceServer server;

    private String mediaServerURL = "/url/save";

    @Value("classpath:/image.png")
    private Resource image;

    @BeforeEach
    void  setup() {
        server.expect(MockRestRequestMatchers.requestTo(mediaServerURL))
                .andRespond(MockRestResponseCreators
                        .withSuccess("{\"path\": \"new-path\"}",
                                MediaType.APPLICATION_JSON));
    }

    //Unit test
    @Test
    void call_mock_server() throws IOException {
        File file = image.getFile();

        String filePath = fileService.saveFile(file, "/save");
        Assertions.assertThat(filePath).isEqualTo("\"new-path\"");

    }
}