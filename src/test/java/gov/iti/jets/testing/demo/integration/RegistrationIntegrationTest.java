package gov.iti.jets.testing.demo.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import gov.iti.jets.testing.domain.Event;
import gov.iti.jets.testing.domain.Ticket;
import gov.iti.jets.testing.domain.User;
import gov.iti.jets.testing.domain.enums.TicketType;
import gov.iti.jets.testing.domain.enums.UserRole;
import gov.iti.jets.testing.persistence.EventRepository;
import gov.iti.jets.testing.persistence.RegistrationRepository;
import gov.iti.jets.testing.persistence.TicketRepository;
import gov.iti.jets.testing.persistence.UserRepository;
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
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;

import java.time.LocalDateTime;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Transactional
@AutoConfigureTestEntityManager
@Import(ObjectMapper.class)
public class RegistrationIntegrationTest {

    @Autowired
    private TestEntityManager testEntityManager;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private EventRepository eventRepository;

    @Autowired
    private TicketRepository ticketRepository;

    @Autowired
    private RegistrationRepository registrationRepository;

    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper mapper;

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

    static{
        container.start();
    }
    @Test
    void register_to_event_successfully() throws Exception {

        User organizer = createOrganizer();

        User attendee  = User.builder()
                .firstName("salma")
                .lastName("fayez")
                .email("dddrrr")
                .password("ddd")
                .role(UserRole.ATTENDEE)
                .build();

        testEntityManager.persist(organizer);
        testEntityManager.persist(attendee);


        Event event = Event.builder()
                .title("event1")
                .description("event1-description")
                .location("Giza")
                .organizer(organizer)
                .startDate(LocalDateTime.now())
                .endDate(LocalDateTime.of(2033, 9, 29, 3, 0))
                .build();

//        organizer.addEvent(event);
        testEntityManager.persistAndFlush(event);

        Ticket ticket = Ticket.builder()
                .price(50.0)
                .type(TicketType.Silver)
                .quantity(50)
                .event(event)
                .build();

//        event.addTicket(ticket);
        testEntityManager.persistAndFlush(ticket);

        Assertions.assertThat(userRepository.findAll().size()).isEqualTo(2);
        Assertions.assertThat(eventRepository.findAll().size()).isEqualTo(1);
        Assertions.assertThat(ticketRepository.findAll().size()).isEqualTo(1);

        RegistrationDto registrationDto = RegistrationDto.builder()
                .userId(1L)
                .eventId(1L)
                .ticketType("silver")
                .build();


        mvc.perform(MockMvcRequestBuilders.post("/event/register")
                        .content(mapper.writeValueAsString(registrationDto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()).andReturn();

        Assertions.assertThat(registrationRepository.findAll().size()).isEqualTo(1);

        Ticket ticket1 = testEntityManager.find(Ticket.class, 1L);
        Assertions.assertThat(ticket1.getQuantity()).isEqualTo(49);

    }

    private static User createOrganizer() {
        User organizer  = User.builder()
                .firstName("salma")
                .lastName("fayez")
                .email("ddd")
                .password("ddd")
                .role(UserRole.ORGANIZER)
                .build();
        return organizer;
    }
}
