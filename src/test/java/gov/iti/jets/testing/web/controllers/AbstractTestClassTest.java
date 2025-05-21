package gov.iti.jets.testing.web.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import gov.iti.jets.testing.config.WebSecurityConfig;
import gov.iti.jets.testing.persistence.RegistrationRepository;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeAll;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.orm.jpa.AutoConfigureTestEntityManager;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.boot.test.autoconfigure.web.client.AutoConfigureMockRestServiceServer;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.core.io.Resource;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.assertj.MockMvcTester;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureTestEntityManager
@Transactional
@Import({WebSecurityConfig.class})
@AutoConfigureMockRestServiceServer
abstract class AbstractTestClassTest {

    @Autowired
    protected MockMvc mvc;

    @Autowired
    protected TestEntityManager manager;

    @Autowired
    protected ObjectMapper objectMapper;

    @Autowired
    protected RegistrationRepository registrationRepository;

    @Autowired
    protected MockRestServiceServer mockServer;

    @Autowired
    protected MockMvcTester tester;

    @Value("classpath:/image.png")
    protected Resource image;

    @BeforeAll
    static void setup() {
        //setup here the mock server
    }

//    @Container
//    static PostgreSQLContainer container = new PostgreSQLContainer<>("postgres:12.3")
//            .withDatabaseName("test")
//            .withUsername("duke")
//            .withPassword("secret");
//
//    @DynamicPropertySource
//    static void properties(DynamicPropertyRegistry registry) {
//        registry.add("spring.datasource.url", container::getJdbcUrl);
//        registry.add("spring.datasource.password", container::getPassword);
//        registry.add("spring.datasource.username", container::getUsername);
//    }
}