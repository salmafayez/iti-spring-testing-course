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
        "spring-jpa.generate-ddl=true"
})
@AutoConfigureTestDatabase
@AutoConfigureMockMvc
@Transactional
public class RegisterationControllerTest {

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
        User user = User.builder().
                firstName("Ahmed")
                .lastName("Mandour").email("asdasd@dsad.com")
                .role(UserRole.ATTENDEE)
                .build();
        entityManager.persist(user);

        Event event = Event.builder()
                .title("test1")
                .description("test")
                .organizer(user)
                .build();
        entityManager.persist(event);

        Ticket ticket = Ticket.builder()
                .price(50.0)
                .type(TicketType.Gold)
                .quantity(3)
                .event(event)
                .build();
        entityManager.persist(ticket);

        RegistrationDto registrationDto = new RegistrationDto(user.getId(), event.getId(), TicketType.Gold.getName());

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
}
