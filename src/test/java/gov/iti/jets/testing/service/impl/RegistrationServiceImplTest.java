package gov.iti.jets.testing.service.impl;


import gov.iti.jets.testing.domain.Event;
import gov.iti.jets.testing.domain.Ticket;
import gov.iti.jets.testing.domain.User;
import gov.iti.jets.testing.domain.enums.TicketType;
import gov.iti.jets.testing.domain.enums.UserRole;
import gov.iti.jets.testing.persistence.EventRepository;
import gov.iti.jets.testing.persistence.RegistrationRepository;
import gov.iti.jets.testing.persistence.TicketRepository;
import gov.iti.jets.testing.persistence.UserRepository;
import jakarta.persistence.EntityManager;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.transaction.annotation.Transactional;
import gov.iti.jets.testing.web.dtos.RegistrationDto;



import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;


@SpringBootTest(properties = {
        "spring.flyway.enabled=false",
        "spring-jpa.generate-ddl=true"
})
@AutoConfigureTestDatabase
@AutoConfigureMockMvc
@Transactional
class RegistrationServiceImplTest {

    @Mock
    private EventRepository eventRepository;

    @Mock
    private TicketRepository ticketRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private RegistrationRepository registrationRepository;

    @InjectMocks
    RegistrationServiceImpl registrationService;

    @Autowired
    EntityManager entityManager;


    @Test
    @WithMockUser(username = "testuser", roles = {"USER"})
    void get_user_test() {
        // Arrange
        User user = getUser();
        entityManager.persist(user);

        // Act
        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));
        User fetchedUser = registrationService.getUser(user.getId());

        // Assert
        assertThat(fetchedUser).isEqualTo(user);
        verify(userRepository, times(1)).findById(user.getId());

    }

    @Test
    @WithMockUser(username = "testuser", roles = {"USER"})
    void get_event_test() {
        // Arrange
        User user = getUser();
        Event event = getEvent(user);

        entityManager.persist(user);
        entityManager.persist(event);

        // Act
        when(eventRepository.findById(event.getId())).thenReturn(Optional.of(event));

        Event fetchedEvent = registrationService.getEvent(event.getId());

        // Assert
        Assertions.assertThat(fetchedEvent).isEqualTo(event);
    }

    @Test
    @WithMockUser(username = "testuser", roles = {"USER"})
    void get_ticket_test() {
        // Arrange
        User user = getUser();
        Event event = getEvent(user);
        Ticket ticket = getTicket(event,10);
        entityManager.persist(user);
        entityManager.persist(event);
        entityManager.persist(ticket);
        entityManager.flush();

        // Act
        when(ticketRepository.findTicketByTypeAndEvent_Id(TicketType.Gold, event.getId())).thenReturn(Optional.of(ticket));
        Ticket fetchedTicket = registrationService.getTicket(ticket.getId(), TicketType.Gold.getName());

        // Assert
        Assertions.assertThat(fetchedTicket).isEqualTo(ticket);
    }

    @Test
    @WithMockUser(username = "testuser", roles = {"USER"})
    void throw_error_no_tickets() {
        // Arrange
        User user = getUser();
        Event event = getEvent(user);
        Ticket ticket = getTicket(event,10);
        entityManager.persist(user);
        entityManager.persist(event);
        entityManager.persist(ticket);

        RegistrationDto registrationDto = new RegistrationDto(user.getId(), event.getId(), TicketType.Gold.getName());

        // Act
        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));
        when(ticketRepository.findTicketByTypeAndEvent_Id(TicketType.Gold, event.getId())).thenReturn(Optional.of(ticket)); // Update here
        when(eventRepository.findById(event.getId())).thenReturn(Optional.of(event));

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            registrationService.register(registrationDto);
        });

        // Assert
        assertThat(exception.getMessage()).isEqualTo("no tickets");
    }

    @Test
    @WithMockUser(username = "testuser", roles = {"USER"})
    void ticket_quantity_test() {
        // Arrange
        User user = getUser();
        Event event = getEvent(user);
        Ticket ticket = getTicket(event,50);

        entityManager.persist(user);
        entityManager.persist(event);
        entityManager.persist(ticket);

        RegistrationDto registrationDto = new RegistrationDto(user.getId(), event.getId(), TicketType.Gold.getName());

        // Act
        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));
        when(ticketRepository.findTicketByTypeAndEvent_Id(TicketType.getByName(TicketType.Gold.getName()), event.getId())).thenReturn(Optional.of(ticket));
        when(eventRepository.findById(event.getId())).thenReturn(Optional.of(event));

        registrationService.register(registrationDto);

        // Assert
        assertThat(ticket.getQuantity()).isEqualTo(49);

    }

    private static Ticket getTicket(Event event, int quantity) {
        return Ticket.builder().event(event).price(15.15).quantity(quantity).type(TicketType.Gold).build();
    }

    private static Event getEvent(User user) {
        return Event.builder().title("Wegz").description("lehh ya amiraa").organizer(user).build();
    }

    private static User getUser() {
        return User.builder().firstName("Sharpel").lastName("Malak").email("sharbel@gmail.com").role(UserRole.ORGANIZER).password("12345678").build();
    }
}