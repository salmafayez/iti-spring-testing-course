package gov.iti.jets.testing.web.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import gov.iti.jets.testing.config.TestContainersConfig;
import gov.iti.jets.testing.config.WebSecurityConfig;
import gov.iti.jets.testing.domain.Event;
import gov.iti.jets.testing.domain.Ticket;
import gov.iti.jets.testing.domain.User;
import gov.iti.jets.testing.domain.enums.TicketType;
import gov.iti.jets.testing.domain.enums.UserRole;
import gov.iti.jets.testing.persistence.RegistrationRepository;
import gov.iti.jets.testing.web.dtos.RegistrationDto;
import jakarta.transaction.Transactional;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.AutoConfigureTestEntityManager;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureTestEntityManager
@Transactional
@Import(WebSecurityConfig.class)
//@Import(TestContainersConfig.class)
class RegistrationControllerTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private TestEntityManager manager;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private RegistrationRepository registrationRepository;

    @Test
    @Order(1)
    void test_successful_registration() throws Exception {
        //Arrange
        User user = buildOrganizer();
        manager.persistFlushFind(user);

        Event event = buildEvent(user);
        manager.persistFlushFind(event);

        Ticket ticket = buildTicket(event);
        manager.persistFlushFind(ticket);

        RegistrationDto dto = RegistrationDto.builder()
                .userId(user.getId())
                .eventId(event.getId())
                .ticketType(TicketType.Gold.getName())
                .build();

        //Act and Assert
        MvcResult mvcResult = mvc.perform(post("/event/register/{id}", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andDo(print())
                .andExpect(status().isOk()).andReturn();

        //Assert

        Assertions.assertThat(registrationRepository.findAll()).hasSize(1);
    }

    @Test
    void test_cleanUp() throws Exception {
        Assertions.assertThat(registrationRepository.findAll()).hasSize(0);
    }


    private static Ticket buildTicket(Event event) {
        return Ticket.builder().type(TicketType.Gold).quantity(10).price(100.0).event(event).build();
    }

    private static Event buildEvent(User savedUser) {
        return Event.builder().organizer(savedUser).description("concert").title("concert").location("egypt").startDate(LocalDateTime.now()).endDate(LocalDateTime.now()).build();
    }

    private static User buildOrganizer() {
        return User.builder().firstName("salma").lastName("fayez").email("salma@gmail.com").role(UserRole.ORGANIZER).password("12345678").build();
    }

    //TODO write the same test again on your own + test create user and user test containers
}