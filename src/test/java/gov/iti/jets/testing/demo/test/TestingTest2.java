package gov.iti.jets.testing.demo.test;

import gov.iti.jets.testing.web.controllers.HelloController;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

//Integration test
@SpringBootTest
public class TestingTest2 {

    @Autowired
    private HelloController controller;

    @Test
    void test() {
        Assertions.assertThat(controller).isNotNull();
    }

    @Test
    void test2() {
        Assertions.assertThat(controller).isNotNull();
    }


}
