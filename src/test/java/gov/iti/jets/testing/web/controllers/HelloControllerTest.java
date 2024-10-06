package gov.iti.jets.testing.web.controllers;

import gov.iti.jets.testing.service.HelloService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class HelloControllerTest {

    @Mock
    private HelloService helloService;

    @InjectMocks
    private HelloController helloController;

    @Test
    void return_hello_successfully() {
        //Arrange
        when(helloService.getHello()).thenReturn("Hello");

        //Act
        ResponseEntity<String> greeting = helloController.greeting();
        String helloResponse = greeting.getBody();

        //Assert
        assertThat(helloResponse).isEqualTo("Hello");
        verify(helloService, times(1)).getHello();
    }
}