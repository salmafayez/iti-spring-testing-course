package gov.iti.jets.testing.web.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import gov.iti.jets.testing.domain.Event;
import gov.iti.jets.testing.domain.Registration;
import gov.iti.jets.testing.domain.Ticket;
import gov.iti.jets.testing.domain.User;
import gov.iti.jets.testing.domain.enums.TicketType;
import gov.iti.jets.testing.domain.enums.UserRole;

import gov.iti.jets.testing.persistence.RegistrationRepository;
import gov.iti.jets.testing.web.dtos.RegistrationDto;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.AutoConfigureTestEntityManager;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;



import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


public class RegistrationControllerTest extends AbstractTest{

    @Autowired
    private EntityManager entityManager;
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private RegistrationRepository registrationRepository;

    @Autowired
    private RegistrationController registrationController;

    @Test
    public void fully_integration_test() throws Exception {
        //arrange
        User user = User.builder().firstName("test1")
                .lastName("test2")
                .email("test@test.com")
                .role(UserRole.ORGANIZER)
                .password("password")
                .build();
        entityManager.persist(user);

        Event event= Event.builder()
                .title("event1")
                .description("event1")
                .organizer(user)
                .build();
        entityManager.persist(event);

        Ticket ticket=Ticket.builder()
                .event(event)
                .type(TicketType.Gold)
                .price(100.0)
                .quantity(50)
                .build();
        entityManager.persist(ticket);
        RegistrationDto dto=new RegistrationDto(user.getId(), event.getId(),TicketType.Gold.getName());
        System.out.println(dto.toString());

        // Act
        var result = this.mockMvc.perform(MockMvcRequestBuilders.post("/event/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk()) // Assert the response status
                .andReturn();

    }
    @Test
    public void count(){
        long count=registrationRepository.count();
        assertEquals(0,count);
    }
}