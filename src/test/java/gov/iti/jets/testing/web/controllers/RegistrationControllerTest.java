package gov.iti.jets.testing.web.controllers;


import gov.iti.jets.testing.domain.Event;
import gov.iti.jets.testing.domain.Registration;
import gov.iti.jets.testing.domain.Ticket;
import gov.iti.jets.testing.domain.enums.TicketType;
import gov.iti.jets.testing.domain.enums.UserRole;
import gov.iti.jets.testing.persistence.RegistrationRepository;
import gov.iti.jets.testing.web.dtos.RegistrationDto;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureJdbc;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.AutoConfigureDataJpa;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import gov.iti.jets.testing.domain.User;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.testcontainers.shaded.com.fasterxml.jackson.core.JsonProcessingException;
//import org.testcontainers.shaded.com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectMapper;

@SpringBootTest(properties = {
        "spring.flyway.enabled=false",
        "spring.jpa.generate-ddl=true"
})
@AutoConfigureTestDatabase
//@AutoConfigureDataJpa
@AutoConfigureMockMvc
@Transactional
public class RegistrationControllerTest {

    @Autowired
    private EntityManager entityManager;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private RegistrationRepository registrationRepository;

    @Test
    @WithMockUser
    void fully_integration_test() throws Exception {
        User organizer = User.builder()
                .firstName("mohab")
                .lastName("wahdan")
                .role(UserRole.ORGANIZER)
                .email("mohab@gmail.com")
                .password("12345678")
                .build();
        entityManager.persist(organizer);

        Event event = Event.builder()
                .title("ev1")
                .description("description1")
                .organizer(organizer)
                .build();
        entityManager.persist(event);
        Ticket ticket = Ticket.builder()
                .event(event)
                .price(20.25)
                .quantity(25)
                .type(TicketType.Gold)
                .build();
        entityManager.persist(ticket);

        RegistrationDto registrationDto = new RegistrationDto(organizer.getId()
        , event.getId(), TicketType.Gold.getName());
        System.out.println(registrationDto.getUserId());

        this.mockMvc.perform(MockMvcRequestBuilders.post("/event/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(registrationDto))
        ).andExpect(MockMvcResultMatchers.status().isOk());

        long count = registrationRepository.count();
        Assertions.assertThat(count).isEqualTo(1);
        System.out.println(ticket.getQuantity());
    }
}
