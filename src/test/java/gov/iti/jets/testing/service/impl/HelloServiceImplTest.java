package gov.iti.jets.testing.service.impl;

import gov.iti.jets.testing.service.HelloService;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;
import org.junit.jupiter.params.provider.EnumSource;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.Assertions.assertThat;

class HelloServiceImplTest {

    private HelloService cut;

    HelloServiceImplTest() {
        System.out.println("from constructor");
    }

    @BeforeEach
    void setUp() {
        //clean for database
        this.cut = new HelloServiceImpl();
    }

    @AfterEach
    void cleanUp() {
        System.out.println("after each");
    }

    @Test
    @DisplayName("testing hello method successfully")
    void test_hello() {
        //Arrange
        //Act
        String result = cut.sayHello();

        //Assert
        assertThat(result).isEqualTo("Hello");
    }

    @ParameterizedTest
    @MethodSource(value = "getParameters")
    void test_parameterized_hello(String name) {
        //Arrange
        //Act
        String result = cut.sayHelloTo(name);

        //Assert
        assertThat(result).isEqualTo("Hello " + name);
    }


    @Test
    void test_failure_scenario() {
        Assertions.assertThrows(RuntimeException.class, () -> cut.throwException());
    }

    static String[] getParameters() {
        return new String[]{"ahmed", "moamed"};
    }

}