package gov.iti.jets.testing.service.impl;

import gov.iti.jets.testing.service.FileService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.client.RestClientTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.client.MockRestServiceServer;

import static org.assertj.core.api.Assertions.assertThat;

@RestClientTest
@Import(FileServiceImpl.class)
class FileServiceImplTest {

    @Autowired
    private MockRestServiceServer server;

    @Autowired
    private FileService fileService;


    @Test
    void test_setup(){
        assertThat(server).isNotNull();
        assertThat(fileService).isNotNull();
    }


}