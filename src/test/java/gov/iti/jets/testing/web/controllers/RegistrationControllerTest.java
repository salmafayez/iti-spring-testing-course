

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


public class RegistrationControllerTest extends  AbstractTest{

    @Autowired
    private EntityManager entityManager;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private RegistrationRepository registrationRepository;

    @Test
    public void full_Integeration_Test() throws Exception {


        /* =======================================  arrange ================================= */
        User organizer = buildUser();
        entityManager.persist(organizer);

        Event event = buildEvent(organizer);
        entityManager.persist(event);

        Ticket ticket = buildTicket(event);
        entityManager.persist(ticket);

        RegistrationDto registrationDto = new RegistrationDto(organizer.getId(),event.getId(),TicketType.Gold.getName());
//        System.out.println(registrationDto.getUserId());

        /* =======================================  act ================================= */

        this.mockMvc.perform(MockMvcRequestBuilders.post("/event/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(registrationDto))
                )
                .andExpect(MockMvcResultMatchers.status().isOk());

        /* =======================================  assert ================================= */
        long count = registrationRepository.count();
        Assertions.assertThat(count).isEqualTo(1);
        System.out.println(ticket.getQuantity());
    }

    private static Ticket buildTicket(Event event) {
        return Ticket.builder()
                .event(event)
                .price(150.5)
                .quantity(10)
                .type(TicketType.Gold)
                .build();
    }

    private static Event buildEvent(User organizer) {
        return Event.builder()
                .title("title")
                .description("description")
                .organizer(organizer)
                .build();
    }

    private static User buildUser() {
        return User.builder()
                .firstName("ahmad")
                .lastName("haroun")
                .role(UserRole.ORGANIZER)
                .email("ddd")
                .password("111")
                .build();
    }
}
