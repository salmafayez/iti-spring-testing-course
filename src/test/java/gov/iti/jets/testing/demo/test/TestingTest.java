package gov.iti.jets.testing.demo.test;

import gov.iti.jets.testing.web.controllers.HelloController;
import jakarta.transaction.Transactional;
import org.assertj.core.api.Assertions;
import org.junit.After;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.transaction.PlatformTransactionManager;

//Integration test
@SpringBootTest
public class TestingTest {

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
