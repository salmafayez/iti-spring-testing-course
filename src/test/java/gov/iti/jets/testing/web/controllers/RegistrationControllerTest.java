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
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

public class RegistrationControllerTest extends AbstractTest{

    //1 => if quantity less than zero
    //2 => the user exists
    //3 => the event exists
    //4 => the ticket exists
    //5 => happy scenario

    @Autowired
    private EntityManager entityManager;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private RegistrationRepository registrationRepository;

    @Test
    void fully_integeration_test() throws Exception {
        // Arrange

        User organizer = User.builder().firstName("test").lastName("test").email("test@test.com").password("12345678").role(UserRole.ORGANIZER).build();
        entityManager.persist(organizer);
        Event event = Event.builder().title("e1").description("desc").organizer(organizer).build();
        entityManager.persist(event);
        Ticket ticket = Ticket.builder().event(event).price(200.0).quantity(60).type(TicketType.Silver).build();
        entityManager.persist(ticket);
        RegistrationDto registrationDto =new RegistrationDto(organizer.getId(), event.getId(), TicketType.Silver.getName());

        //Act
        this.mockMvc.perform(MockMvcRequestBuilders.post("/event/register").contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(registrationDto))).andExpect(MockMvcResultMatchers.status().isOk());
        long count = registrationRepository.count();

        //Assert
        Assertions.assertEquals(1, count);
        System.out.println(ticket.getQuantity());
    }

    @Test
    void test_db_empty() throws Exception {
        long count = registrationRepository.count();
        Assertions.assertEquals(0, count);
    }

    public static Ticket buildTicket(Event event) {
        return Ticket.builder()
                .price(300.0)
                .type(TicketType.Gold)
                .quantity(70)
                .event(event)
                .build();
    }

    private static User buildUser() {
        return User.builder().
                firstName("fatma")
                .lastName("amr")
                .email("fatma@gmail.com")
                .password("12345678")
                .role(UserRole.ORGANIZER)
                .build();
    }

    public static Event buildEvent(User user) {
        return Event.builder()
                .title("test1")
                .description("test")
                .organizer(user)
                .build();
    }

}