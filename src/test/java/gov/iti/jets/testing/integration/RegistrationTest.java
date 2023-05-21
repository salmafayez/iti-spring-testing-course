package gov.iti.jets.testing.integration;


import com.fasterxml.jackson.databind.ObjectMapper;
import gov.iti.jets.testing.domain.Event;
import gov.iti.jets.testing.domain.Registration;
import gov.iti.jets.testing.domain.Ticket;
import gov.iti.jets.testing.domain.User;
import gov.iti.jets.testing.domain.enums.TicketType;
import gov.iti.jets.testing.domain.enums.UserRole;
import gov.iti.jets.testing.persistence.RegistrationRepository;
import gov.iti.jets.testing.service.RegistrationService;
import gov.iti.jets.testing.web.dtos.RegistrationDto;
import jakarta.transaction.Transactional;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.AutoConfigureTestEntityManager;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureTestEntityManager
@Transactional
@Testcontainers
@Import(ObjectMapper.class)
class RegistrationTest {

    @Autowired
    private RegistrationService registrationService;

    @Autowired
    private MockMvc mvc;

    @Autowired
    private TestEntityManager testEntityManager;

    @Autowired
    private ObjectMapper mapper;

    @Autowired
    private RegistrationRepository registrationRepository;

    @Container
    static PostgreSQLContainer container = new PostgreSQLContainer<>("postgres:12.3")
            .withDatabaseName("test")
            .withUsername("test")
            .withPassword("test");

    @DynamicPropertySource
    static void properties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", container::getJdbcUrl);
        registry.add("spring.datasource.password", container::getPassword);
        registry.add("spring.datasource.username", container::getUsername);
    }

//    static{
//        container.start();
//    }

    @Test
    void register_ticket_successfully() throws Exception {
        //Arrange
        User user = createUser();
        User savedUser = testEntityManager.persist(user);

        Event event = createEvent(user);
        Event savedEvent = testEntityManager.persist(event);

        Ticket ticket = createTicket(event);
        Ticket savedTicket = testEntityManager.persist(ticket);

        RegistrationDto dto = createRegistrationDto(savedUser, savedEvent);

        //Act
        mvc.perform(MockMvcRequestBuilders.post("/event/register")
                .content(mapper.writeValueAsString(dto))
                .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(MockMvcResultMatchers.status().isOk());


        //Assert
        assertThat(registrationRepository.findAll().size()).isEqualTo(1);
        assertThat(testEntityManager.find(Ticket.class, 1L).getQuantity()
                ).isEqualTo(9);
    }

    @Test
    void clean_up_test(){
        assertThat(registrationRepository.findAll().size()).isEqualTo(0);
    }

    private static RegistrationDto createRegistrationDto(User savedUser, Event savedEvent) {
        return RegistrationDto.builder()
                .userId(savedUser.getId())
                .eventId(savedEvent.getId())
                .ticketType("silver")
                .build();
    }

    private static Ticket createTicket(Event event) {
        Ticket ticket = Ticket.builder()
                .type(TicketType.Silver)
                .quantity(10)
                .price(50.0)
                .event(event)
                .build();
        return ticket;
    }

    private static Event createEvent(User user) {
        Event event = Event.builder()
                .title("event")
                .description("event-description")
                .startDate(LocalDateTime.now())
                .endDate(LocalDateTime.now())
                .location("egypt")
                .organizer(user)
                .build();
        return event;
    }

    private static User createUser() {
        User user = User.builder()
                .firstName("test")
                .lastName("test")
                .password("test")
                .email("test")
                .role(UserRole.ORGANIZER)
                .build();
        return user;
    }

}
