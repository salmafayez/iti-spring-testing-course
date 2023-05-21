package gov.iti.jets.testing.demo.impl;

import com.github.tomakehurst.wiremock.core.WireMockConfiguration;
import com.github.tomakehurst.wiremock.junit5.WireMockExtension;
import gov.iti.jets.testing.service.FileService;
import gov.iti.jets.testing.service.impl.FileServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.client.RestClientTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;

import java.io.File;
import java.io.IOException;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.post;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@RestClientTest(FileServiceImpl.class)
class FileServiceImplTest2 {

    @Value("classpath:/image.png")
    private Resource image;

    @Autowired
    private FileService fileService;

    private String mediaServerURL = "http://localhost:3333/url";

    @RegisterExtension
    static WireMockExtension wireMockServer = WireMockExtension.newInstance()
            .options(WireMockConfiguration.wireMockConfig().port(3333))
            .build();


    @Test
    void send_image_successfully2() throws IOException {

        wireMockServer.stubFor(
                post("/url").willReturn(aResponse()
                                .withBody("{\"path\": \"new-path\"}")
                                .withHeader("Content-Type", MediaType.APPLICATION_JSON_VALUE))

        );

        File file  = image.getFile();
        String path = fileService.saveFile(file, mediaServerURL);

        assertThat(path).isEqualTo("\"new-path\"");
    }

    @TestConfiguration
    static class Config {
        @Bean
        RestTemplateBuilder restTemplateBuilder(){
            return new RestTemplateBuilder();
        }
    }


}