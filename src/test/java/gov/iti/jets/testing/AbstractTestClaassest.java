package gov.iti.jets.testing;

import com.fasterxml.jackson.databind.ObjectMapper;
import gov.iti.jets.testing.config.TestContainersConfig;
import gov.iti.jets.testing.domain.Event;
import gov.iti.jets.testing.domain.Ticket;
import gov.iti.jets.testing.domain.User;
import gov.iti.jets.testing.domain.enums.TicketType;
import gov.iti.jets.testing.domain.enums.UserRole;
import gov.iti.jets.testing.persistence.RegistrationRepository;
import gov.iti.jets.testing.web.dtos.RegistrationDto;
import jakarta.transaction.Transactional;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.AutoConfigureTestEntityManager;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.assertj.MockMvcTester;

import java.time.LocalDateTime;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureTestEntityManager
@Transactional
//@Import(WebSecurityConfig.class)
@Import(TestContainersConfig.class)
abstract class AbstractTestClassTest {

    @Autowired
    MockMvc mvc;

    @Autowired
    TestEntityManager manager;

    @Autowired
    protected ObjectMapper objectMapper;

    @Autowired
    protected RegistrationRepository registrationRepository;

    @Autowired
    protected MockMvcTester tester;

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
//
//    @Value("classpath:/image.png")
//    private Resource image;

//    @Test
//    void add_user_photo_successfully() throws Exception {
//
//        //Arrange
//        doNothing().when(userService).addUserPhoto(any(), any());
//
//        MockMultipartFile mockFile = new MockMultipartFile("file", image.getInputStream());
//        MockPart part = new MockPart("id",null);
//
//
//        //Act and assert
//        mvc.perform(multipart("/users/{id}/photo", 1L)
//                        .file(mockFile)
//                        .part(part)
//                        .contentType(MediaType.APPLICATION_JSON))
//                .andExpect(MockMvcResultMatchers.status().isOk());
//
//    }
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
//    @Test
//    @WithMockUser(username = "salma", password = "12345678")
//    void delete_user_successfully() throws Exception {
//
//
//        //Arrange
//        doNothing().when(userService).deleteUser(any());
//
//        //Act and assert
//        MvcResult result = mvc.perform(delete("/private/users/{id}", 1))
//                .andExpect(MockMvcResultMatchers.status().isOk())
//                .andReturn();
//
//    }
//
//    @Test
//    void delete_user_successfully2() throws Exception {
//
//        //Arrange
//        doNothing().when(userService).deleteUser(any());
//
//        //Act and assert
//        MvcResult result = mvc.perform(delete("/private/users/{id}", 1)
//                        .with(user(User.builder().email("salma")
//                                .password("1233").build())))
//                .andExpect(MockMvcResultMatchers.status().isOk())
//                .andReturn();
//
//    }

    //TODO write the same test again on your own + test create user and user test containers
}