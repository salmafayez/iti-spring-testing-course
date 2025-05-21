package gov.iti.jets.testing.web.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import gov.iti.jets.testing.config.TestContainersConfig;
import gov.iti.jets.testing.config.WebSecurityConfig;
import gov.iti.jets.testing.domain.Event;
import gov.iti.jets.testing.domain.Ticket;
import gov.iti.jets.testing.domain.User;
import gov.iti.jets.testing.domain.enums.TicketType;
import gov.iti.jets.testing.domain.enums.UserRole;
import gov.iti.jets.testing.persistence.RegistrationRepository;
import gov.iti.jets.testing.service.FileService;
import gov.iti.jets.testing.service.UserService;
import gov.iti.jets.testing.web.dtos.RegistrationDto;
import jakarta.transaction.Transactional;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.AutoConfigureTestEntityManager;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.boot.test.autoconfigure.web.client.AutoConfigureMockRestServiceServer;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.mock.web.MockPart;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.assertj.MockMvcTester;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.io.File;
import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatReflectiveOperationException;
import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureTestEntityManager
@Transactional
@Import(WebSecurityConfig.class)
@AutoConfigureMockRestServiceServer
class RegistrationControllerTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private TestEntityManager manager;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private RegistrationRepository registrationRepository;

    @Autowired
    private MockMvcTester tester;


    @Value("classpath:/image.png")
    private Resource image;


    @MockitoBean
    private UserService userService;

    @MockitoBean
    private FileService filerService;
    @BeforeEach
    void setup() {

    }

    @Test
    void add_user_photo_successfully() throws Exception {
        MockMultipartFile mockFile = new MockMultipartFile("file", image.getInputStream());
        MockPart part  = new MockPart("id",null);

//        doNothing().when(userService).addUserPhoto(any(), any());
        doNothing().when(filerService).saveFile(any(), any());

//        doNothing(userService.addUserPhoto(any(), any()))


        //Act and assert
        mvc.perform(multipart("/users/{id}/photo", 1L)
                        .file(mockFile)
                        .part(part)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk());
        //
    }

//    @Container
//    static PostgreSQLContainer container = new PostgreSQLContainer<>("postgres:12.3")
//            .withDatabaseName("test")
//            .withUsername("duke")
//            .withPassword("secret");
//
//    @DynamicPropertySource
//    static void properties(DynamicPropertyRegistry registry) {
//        registry.add("spring.datasource.url", container::getJdbcUrl);
//        registry.add("spring.datasource.password", container::getPassword);
//        registry.add("spring.datasource.username", container::getUsername);
//    }

    @Test
    void test_successful_registration() throws Exception {
        //Arrange
        User user = buildOrganizer();
        manager.persistFlushFind(user);

        Event event = buildEvent(user);
        manager.persistFlushFind(event);

        Ticket ticket = buildTicket(event);
        manager.persistFlushFind(ticket);

        RegistrationDto dto = RegistrationDto.builder()
                .userId(user.getId())
                .eventId(event.getId())
                .ticketType(TicketType.Gold.getName())
                .build();

        //Act and Assert
        MvcResult mvcResult = mvc.perform(post("/event/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andDo(print())
                .andExpect(status().isOk()).andReturn();


        //Assert
        Assertions.assertThat(registrationRepository.findAll()).hasSize(1);
    }
//
//    @Test
//    void test_cleanUp() throws Exception {
//        assertThat(registrationRepository.findAll()).hasSize(0);
//    }


    private static Ticket buildTicket(Event event) {
        return Ticket.builder().type(TicketType.Gold).quantity(10).price(100.0).event(event).build();
    }

    private static Event buildEvent(User savedUser) {
        return Event.builder().organizer(savedUser).description("concert").title("concert").location("egypt").startDate(LocalDateTime.now()).endDate(LocalDateTime.now()).build();
    }

    private static User buildOrganizer() {
        return User.builder().firstName("salma").lastName("fayez").email("salma@gmail.com").role(UserRole.ORGANIZER).password("12345678").build();
    }

    @Test
    @WithMockUser
    void test_authenticated_endpoint() throws Exception {
        //Arrange

        //Act
        mvc.perform(MockMvcRequestBuilders.delete("/private/users/{id}", 1))
                .andExpect(MockMvcResultMatchers.status().isOk());

        //Assert
    }

//
//    @Test
//    void get_user_by_id_2_successfully() throws Exception {
//
//        //Arrange
//        when(userService.getById(any())).thenReturn(User.builder().firstName("salma").build());
//
//        //Act and assert
//        MvcResult result = mvc.perform(MockMvcRequestBuilders
//                        .get("/users/{id}", 1))
//                .andExpect(MockMvcResultMatchers.status().isOk())
//                .andReturn();
//
//        User user = objectMapper.readValue(result.getResponse()
//                .getContentAsString(), new TypeReference<>() {});
//
//        assertEquals("salma", user.getFirstName());
//    }
//
    @Test
    @WithMockUser(username = "salma", password = "12345678")
    void delete_user_successfully() throws Exception {

        //Arrange
//        doNothing().when(userService).deleteUser(any());

        //Act and assert
        MvcResult result = mvc.perform(delete("/private/users/{id}", 1))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();
        //401 => not authenticated
        //403 => forbidden (not authorized)

    }

    @Test
    void delete_user_successfully2() throws Exception {

        //Arrange
//        doNothing().when(userService).deleteUser(any());

        //Act and assert
        MvcResult result = mvc.perform(delete("/private/users/{id}", 1)
                        .with(jwt()))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();

    }

    //TODO write the same test again on your own + test create user and user test containers
}