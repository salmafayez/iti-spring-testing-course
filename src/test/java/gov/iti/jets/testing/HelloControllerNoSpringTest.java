package gov.iti.jets.testing;

import gov.iti.jets.testing.service.HelloService;
import gov.iti.jets.testing.web.controllers.HelloController;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class HelloControllerNoSpringTest {

    @Mock
    private HelloService helloService;

    @InjectMocks
    private HelloController cut;

    @Test
    void test_hello_from_controller() {
        System.out.println("running test");
        //Arrange
        when(helloService.sayHello()).thenReturn("Hello");

        //Act
        ResponseEntity<String> response = cut.sayHello();

        //Assert
        Assertions.assertThat(response.getBody()).isEqualTo("Hello");

        verify(helloService, times(4)).sayHello();
        verifyNoInteractions(helloService.sayHello());
    }

    @Test
    void test_hello_from_controller2() {
        System.out.println("running test");
        //Arrange
        when(helloService.sayHello()).thenReturn("Hello");

        //Act
        ResponseEntity<String> response = cut.sayHello();

        //Assert
        Assertions.assertThat(response.getBody()).isEqualTo("Hello");

        verify(helloService, times(1)).sayHello();
    }

}