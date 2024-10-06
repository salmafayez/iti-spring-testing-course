package gov.iti.jets.testing.web.controllers;

import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;


@SpringBootTest(properties = {
        "spring.flyway.enabled=false",
        "spring.jpa.generate-ddl=true"
})
@AutoConfigureTestDatabase// default behaviour is to use the in-memory database (H2) instead of real database
@AutoConfigureMockMvc
@Transactional  // Add this annotation
class AbstractTest {

}