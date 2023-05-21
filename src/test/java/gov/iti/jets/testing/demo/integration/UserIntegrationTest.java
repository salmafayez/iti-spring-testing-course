package gov.iti.jets.testing.demo.integration;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import gov.iti.jets.testing.domain.User;
import gov.iti.jets.testing.domain.enums.UserRole;
import gov.iti.jets.testing.persistence.UserRepository;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.AutoConfigureTestEntityManager;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Testcontainers
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@AutoConfigureTestEntityManager
@Transactional
public class UserIntegrationTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private TestEntityManager testEntityManager;

    @Autowired
    private UserRepository userRepository;

    @Container
    static PostgreSQLContainer container = new PostgreSQLContainer<>("postgres:12.3")
            .withDatabaseName("test")
            .withUsername("duke")
            .withPassword("secret");

    @DynamicPropertySource
    static void properties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", container::getJdbcUrl);
        registry.add("spring.datasource.password", container::getPassword);
        registry.add("spring.datasource.username", container::getUsername);
    }

    @Test
    void getUsers() throws Exception {


        User user1  = User.builder()
                .firstName("salma")
                .lastName("fayez")
                .email("ddd")
                .password("ddd")
                .role(UserRole.ORGANIZER)
                .build();
        testEntityManager.persistAndFlush(user1);

        MvcResult result = mvc.perform(MockMvcRequestBuilders.get("/users"))
                .andExpect(status().isOk()).andReturn();

        List<User> user = objectMapper.readValue(result.getResponse().getContentAsString(),
                new TypeReference<>() {});

//        assertNotNull(user.getId());
        assertEquals(1, userRepository.count());
        assertNotNull(user);
        assertEquals(1, user.size());
    }
}
