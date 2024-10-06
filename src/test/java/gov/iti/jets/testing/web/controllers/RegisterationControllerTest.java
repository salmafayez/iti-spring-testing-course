package gov.iti.jets.testing.web.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import gov.iti.jets.testing.domain.Event;
import gov.iti.jets.testing.domain.Ticket;
import gov.iti.jets.testing.domain.User;
import gov.iti.jets.testing.domain.enums.TicketType;
import gov.iti.jets.testing.domain.enums.UserRole;
import gov.iti.jets.testing.persistence.RegistrationRepository;
import gov.iti.jets.testing.web.dtos.RegistrationDto;
import jakarta.persistence.EntityManager;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;


//one integration test for event register
//unit tests for registration method in service


public class RegisterationControllerTest extends AbstractTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    EntityManager entityManager;

    @Autowired
    private RegistrationRepository registrationRepository;

    @Test
    void fullyIntergratedTest() throws Exception {
        // Arrange
        User user = buildUser();
        entityManager.persist(user);

        Event event = buildEvent(user);
        entityManager.persist(event);

        Ticket ticket = buildTicket(event);
        entityManager.persist(ticket);

        RegistrationDto registrationDto = new RegistrationDto(user.getId(), event.getId(), TicketType.Gold.getName());
        System.out.println(registrationDto.getUserId() + " " + registrationDto.getEventId() + " " + registrationDto.getTicketType());

        // Act
        this.mockMvc.perform(MockMvcRequestBuilders.post("/event/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(registrationDto)))
                .andExpect(MockMvcResultMatchers.status().isOk());

        long count = registrationRepository.count();

        // Assert
        Assertions.assertThat(count).isEqualTo(1);
        System.out.println(ticket.getQuantity());

    }

    private static Ticket buildTicket(Event event) {
        return Ticket.builder()
                .price(100.0)
                .type(TicketType.Gold)
                .quantity(10)
                .event(event)
                .build();
    }

    private static User buildUser() {
        return User.builder().
                firstName("salma")
                .lastName("fayez")
                .email("salma.fayez@gmail.com")
                .password("12345678")
                .role(UserRole.ORGANIZER)
                .build();
    }

    private static Event buildEvent(User user) {
        return Event.builder()
                .title("test1")
                .description("test")
                .organizer(user)
                .build();
    }

}
