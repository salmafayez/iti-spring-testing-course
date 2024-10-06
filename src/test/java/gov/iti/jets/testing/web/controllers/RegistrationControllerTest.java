package gov.iti.jets.testing.web.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import gov.iti.jets.testing.domain.Event;
import gov.iti.jets.testing.domain.Ticket;
import gov.iti.jets.testing.domain.User;
import gov.iti.jets.testing.domain.enums.TicketType;
import gov.iti.jets.testing.domain.enums.UserRole;
import gov.iti.jets.testing.persistence.RegistrationRepository;
import gov.iti.jets.testing.web.dtos.RegistrationDto;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;


public class RegistrationControllerTest extends AbstractTest {
    @Autowired
    private TestEntityManager entityManager;
    @Autowired
    private MockMvc mvc;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private RegistrationRepository registrationRepository;

    @Test
    void fully_integration_test() throws Exception {
        //Arrange
        User organizer = buildUser();
        entityManager.persist(organizer);

        Event event = buildEvent(organizer);
        entityManager.persist(event);

        Ticket ticket = buildTicket(event);
        entityManager.persist(ticket);

        RegistrationDto registrationDto =
                new RegistrationDto(organizer.getId(),event.getId(),
                        TicketType.Gold.getName());

        //Act
        mvc.perform(MockMvcRequestBuilders.post("/event/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(registrationDto))
        ).andExpect(MockMvcResultMatchers.status().isOk());

        //Assert
        long count = registrationRepository.count();

        Assertions.assertThat(count).isEqualTo(1);
        Assertions.assertThat(ticket.getQuantity()).isEqualTo(49);

    }

    private static Ticket buildTicket(Event event) {
        return Ticket.builder()
                .event(event)
                .price(100.0)
                .quantity(50)
                .type(TicketType.Gold)
                .build();
    }

    private static Event buildEvent(User organizer) {
        return Event.builder()
                .title("event1")
                .description("desc")
                .organizer(organizer)
                .build();
    }

    private static User buildUser() {
        return User.builder()
                .firstName("first")
                .lastName("last")
                .email("email")
                .password("123456789")
                .role(UserRole.ORGANIZER)
                .build();
    }
}
