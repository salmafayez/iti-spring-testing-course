package gov.iti.jets.testing.web.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import gov.iti.jets.testing.configs.DbConfigurations;
import gov.iti.jets.testing.domain.Event;
import gov.iti.jets.testing.domain.Ticket;
import gov.iti.jets.testing.domain.User;
import gov.iti.jets.testing.domain.enums.TicketType;
import gov.iti.jets.testing.domain.enums.UserRole;
import gov.iti.jets.testing.persistence.RegistrationRepository;
import gov.iti.jets.testing.web.dtos.RegistrationDto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.assertj.core.api.Assertions.assertThat;


class RegistrationControllerTest extends DbConfigurations {

    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private TestEntityManager testEntityManager;
    @Autowired
    private LocalContainerEntityManagerFactoryBean entityManagerFactory;
    @Autowired
    private RegistrationRepository registrationRepository;

    @Test
    @DisplayName("Test the registration functionality as a whole")
//    @WithMockUser // to pass the authentication
    void registrationFunctionalityIntegration() throws Exception {

        User user = User.builder()
                .firstName("Abdulrahman")
                .lastName("Almohandis")
                .email("a@a.com")
                .password("123456")
                .role(UserRole.ORGANIZER)
                .build();

        testEntityManager.persist(user);

        Event event = Event.builder()
                .title("Event 1")
                .description("Event 1 description")
                .organizer(user)
                .build();

        testEntityManager.persist(event);

        Ticket ticket = Ticket.builder()
                .event(event)
                .type(TicketType.Gold)
                .price(100.0)
                .quantity(10)
                .build();

        testEntityManager.persist(ticket);

        RegistrationDto registrationDto = RegistrationDto.builder()
                .userId(user.getId())
                .eventId(event.getId())
                .ticketType(TicketType.Gold.getName())
                .build();
        System.out.println(registrationDto.getUserId());


        mockMvc.perform(MockMvcRequestBuilders.post("/event/register")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(registrationDto)))
                .andExpect(MockMvcResultMatchers.status().isOk());

        Long count = registrationRepository.count();

        assertThat(count).isEqualTo(1);
        assertThat(ticket.getQuantity()).isEqualTo(9);


    }

}



















