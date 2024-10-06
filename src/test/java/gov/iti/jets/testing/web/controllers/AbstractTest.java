package gov.iti.jets.testing.web.controllers;

import jakarta.transaction.Transactional;
import org.junit.runner.RunWith;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.AutoConfigureTestEntityManager;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@SpringBootTest(properties = {
        "spring.flyway.enabled=false",
        "spring.jpa.generate-ddl=true"
})
@AutoConfigureTestDatabase
@AutoConfigureTestEntityManager
@Transactional
@AutoConfigureMockMvc
@RunWith(SpringRunner.class)
public class AbstractTest {

}
