package gov.iti.jets.testing.demo.web.controllers;

import gov.iti.jets.testing.web.controllers.HelloController;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;


@SpringBootTest
class HelloControllerTest {

    @Autowired
    private HelloController helloController;

    @Test
    void hello_test() {
        Assertions.assertThat(helloController).isNotNull();
    }
}