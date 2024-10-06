package gov.iti.jets.testing.web.controllers;

import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.AutoConfigureTestEntityManager;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest(properties = {
        "spring.flyway.enabled=false",
        "spring.jpa.generate-ddl=true"
})
@AutoConfigureTestDatabase
@AutoConfigureTestEntityManager
@AutoConfigureMockMvc
@Transactional
public class AbstractTest {
}
