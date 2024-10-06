package gov.iti.jets.testing.service.impl;

import gov.iti.jets.testing.service.HelloService;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;


class HelloServiceImplTest {

    private HelloService helloService;

    @BeforeEach
    void setup() {
        helloService = new HelloServiceImpl();
    }

    @Test
    void return_hello_string() {
        //Arrange

        //Act
        String helloResponse = helloService.getHello();

        //Assert
        assertThat(helloResponse).isEqualTo("Hello");
    }
}