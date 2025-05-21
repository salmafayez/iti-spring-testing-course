package gov.iti.jets.testing.web.controllers;

import gov.iti.jets.testing.domain.Event;
import gov.iti.jets.testing.domain.Ticket;
import gov.iti.jets.testing.domain.User;
import gov.iti.jets.testing.domain.enums.TicketType;
import gov.iti.jets.testing.domain.enums.UserRole;
import gov.iti.jets.testing.web.dtos.RegistrationDto;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.mock.web.MockPart;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import java.time.LocalDateTime;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class RegistrationControllerTest extends AbstractTestClassTest {

    @Test
    void add_user_photo_successfully() throws Exception {
        MockMultipartFile mockFile = new MockMultipartFile("file", image.getInputStream());
        MockPart part  = new MockPart("id",null);

//        doNothing().when(userService).addUserPhoto(any(), any());
//        doNothing().when(filerService).saveFile(any(), any());

        //Act and assert
        mvc.perform(multipart("/users/{id}/photo", 1L)
                        .file(mockFile)
                        .part(part)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk());
        //
    }

    @Test
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
        MvcResult mvcResult = mvc.perform(post("/event/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andDo(print())
                .andExpect(status().isOk()).andReturn();


        //Assert
        Assertions.assertThat(registrationRepository.findAll()).hasSize(1);
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

    @Test
    @WithMockUser
    void test_authenticated_endpoint() throws Exception {
        //Arrange

        //Act
        mvc.perform(MockMvcRequestBuilders.delete("/private/users/{id}", 1))
                .andExpect(MockMvcResultMatchers.status().isOk());

        //Assert
    }

    @Test
    @WithMockUser(username = "salma", password = "12345678")
    void delete_user_successfully() throws Exception {

        //Arrange
//        doNothing().when(userService).deleteUser(any());

        //Act and assert
        MvcResult result = mvc.perform(delete("/private/users/{id}", 1))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();
        //401 => not authenticated
        //403 => forbidden (not authorized)
    }
}