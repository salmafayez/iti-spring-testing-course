package gov.iti.jets.testing.web.controllers;


import com.fasterxml.jackson.databind.ObjectMapper;
import gov.iti.jets.testing.domain.Event;
import gov.iti.jets.testing.domain.Ticket;
import gov.iti.jets.testing.domain.User;
import gov.iti.jets.testing.domain.enums.TicketType;
import gov.iti.jets.testing.domain.enums.UserRole;
import gov.iti.jets.testing.web.dtos.RegistrationDto;
import jakarta.persistence.EntityManager;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest(properties = {
        "spring.flyway.enabled=false",
        "spring.jpa.generate-ddl=true"
})
@Transactional
@AutoConfigureTestDatabase
@AutoConfigureMockMvc
class RegistrationControllerTest {

    @Autowired
    private EntityManager entityManager;
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;


    @Test
    void registerTicket() throws Exception {

        // arrange
        User user = getUser();
        Event event= getEvent(user);
        Ticket ticket= getTicket(event,50);
        entityManager.persist(user);
        entityManager.persist(event);
        entityManager.persist(ticket);
        RegistrationDto registrationDto = new RegistrationDto(user.getId(), event.getId(), TicketType.Gold.getName());
        // Act
        this.mockMvc.perform(MockMvcRequestBuilders.post("/event/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(registrationDto)))
                .andExpect(MockMvcResultMatchers.status().isOk());

        // Assert
        Assertions.assertThat(ticket.getQuantity()).isEqualTo(49);
    }


    private static Ticket getTicket(Event event,int quantity) {
        return Ticket.builder().event(event).price(15.15).quantity(quantity).type(TicketType.Gold).build();
    }

    private static Event getEvent(User user) {
        return Event.builder().title("Wegz").description("lehh ya amiraa").organizer(user).build();
    }

    private static User getUser() {
        return User.builder().firstName("Sharpel").lastName("Malak").email("sharbel@gmail.com").role(UserRole.ORGANIZER).password("12345678").build();
    }

}