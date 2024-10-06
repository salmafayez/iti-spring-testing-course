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
    
    @Autowired EntityManager entityManager;
    @Autowired private MockMvc mockMvc;
    @Autowired private ObjectMapper objectMapper;
    @Autowired private RegistrationRepository registrationRepository;
    
    private static Ticket buildTicket(Event event) {
        return Ticket.builder()
                     .price(999.99)
                     .type(TicketType.Silver)
                     .quantity(500)
                     .event(event)
                     .build();
    }
    
    private static User buildUser() {
        return User.builder()
                   .firstName("Mohamed")
                   .lastName("Saber")
                   .email("mohamed.saber.mohamed.dev@gmail.com")
                   .password("12121212")
                   .role(UserRole.ATTENDEE)
                   .build();
    }
    
    private static Event buildEvent(User user) {
        return Event.builder()
                    .title("Test Event")
                    .description("Test Description")
                    .organizer(user)
                    .build();
    }
    
    @Test void fullyIntergratedTest()
            throws Exception {
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
                    .andExpect(MockMvcResultMatchers.status()
                                                    .isOk());
        
        long count = registrationRepository.count();
        
        // Assert
        Assertions.assertThat(count)
                  .isEqualTo(1);
        System.out.println(ticket.getQuantity());
        
    }
    
}
