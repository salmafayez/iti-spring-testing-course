package gov.iti.jets.testing.service.impl;

import gov.iti.jets.testing.domain.Event;
import gov.iti.jets.testing.domain.Registration;
import gov.iti.jets.testing.domain.Ticket;
import gov.iti.jets.testing.domain.User;
import gov.iti.jets.testing.domain.enums.TicketType;
import gov.iti.jets.testing.domain.enums.UserRole;
import gov.iti.jets.testing.persistence.EventRepository;
import gov.iti.jets.testing.persistence.RegistrationRepository;
import gov.iti.jets.testing.persistence.TicketRepository;
import gov.iti.jets.testing.persistence.UserRepository;
import gov.iti.jets.testing.web.dtos.RegistrationDto;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
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

    @MockBean
    private TicketRepository ticketRepository;

    @MockBean
    private RegistrationRepository registrationRepository;

    @MockBean
    private UserRepository userRepository;

    @MockBean
    private EventRepository eventRepository;

    @Autowired
    private RegistrationServiceImpl registrationServiceImpl;

    @Autowired
    private EntityManager entityManager;

    //1 => if quantity less than zero
    //2 => the user exists
    //3 => the event exists
    //4 => the ticket exists
    //5 => happy scenario

    @BeforeEach
    void setUp() {
    }

    @Test
    void getUserSuccessTest() {
        // Arrange
        User user = buildUser();
        entityManager.persist(user);
        entityManager.flush();

        // Act
        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));
        User fetchedUser = registrationServiceImpl.getUser(user.getId());

        // Assert
        assertThat(fetchedUser).isEqualTo(user);
        verify(userRepository, times(1)).findById(user.getId());
    }

    @Test
    void getTicketSuccessTest() {
        // Arrange
        User user = buildUser();
        entityManager.persist(user);

        Event event = buildEvent(user);
        entityManager.persist(event);

        Ticket ticket = buildTicket(event);
        entityManager.persist(ticket);
        entityManager.flush();

        // Act
        when(ticketRepository.findTicketByTypeAndEvent_Id(TicketType.Silver, event.getId())).thenReturn(Optional.of(ticket));
        Ticket fetchedTicket = registrationServiceImpl.getTicket(ticket.getId(), TicketType.Silver.getName());

        // Assert
        assertThat(fetchedTicket).isEqualTo(ticket);
        verify(ticketRepository, times(1)).findTicketByTypeAndEvent_Id(TicketType.Silver, event.getId());
    }

    @Test
    void getRegistrationSuccessTest() {
        User user = buildUser();
        entityManager.persist(user);

        Event event = buildEvent(user);
        entityManager.persist(event);

        Ticket ticket = buildTicket(event);
        ticket.setQuantity(50);
        entityManager.persist(ticket);
        entityManager.flush();

        RegistrationDto registrationDto = new RegistrationDto(user.getId(), event.getId(), TicketType.Silver.getName());

        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));
        when(ticketRepository.findTicketByTypeAndEvent_Id(TicketType.getByName(TicketType.Silver.getName()), event.getId())).thenReturn(Optional.of(ticket)); // Update here
        when(eventRepository.findById(any(Long.class))).thenReturn(Optional.of(event));

        registrationServiceImpl.register(registrationDto);

        // Assert
        assertThat(ticket.getQuantity()).isEqualTo(49);
        verify(ticketRepository, times(1)).save(ticket);
        verify(registrationRepository, times(1)).save(any(Registration.class));

    }

    @Test
    void getEventSuccessTest() {
        // Arrange
        User user = buildUser();
        entityManager.persist(user);
        entityManager.flush();

        Event event = buildEvent(user);
        entityManager.persist(event);
        entityManager.flush();

        // Act
        when(eventRepository.findById(event.getId())).thenReturn(Optional.of(event));
        Event fetchedEvent = registrationServiceImpl.getEvent(event.getId());

        // Assert
        assertThat(fetchedEvent).isEqualTo(event);
        verify(eventRepository, times(1)).findById(event.getId());
    }

    @Test
    void shouldThrowExceptionWhenNoTicketsAvailable() {
        // Arrange
        User user = buildUser();
        entityManager.persist(user);

        Event event = buildEvent(user);
        entityManager.persist(event);

        Ticket ticket = buildTicket(event);
        ticket.setQuantity(0);
        entityManager.persist(ticket);
        entityManager.flush();

        RegistrationDto registrationDto = new RegistrationDto(user.getId(), event.getId(), TicketType.Silver.getName());

        // Act
        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));
        when(ticketRepository.findTicketByTypeAndEvent_Id(TicketType.Silver, event.getId())).thenReturn(Optional.of(ticket)); // Update here
        when(eventRepository.findById(event.getId())).thenReturn(Optional.of(event));

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            registrationServiceImpl.register(registrationDto);
        });

        // Assert
        assertThat(exception.getMessage()).isEqualTo("there is no available tickets");
    }

    private User buildUser() {
        return User.builder().firstName("fatma").lastName("amr").email("fatma@gmail.com").password("12345678").role(UserRole.ORGANIZER).build();
    }

    private Ticket buildTicket(Event event) {
        return Ticket.builder().price(200.0).type(TicketType.Silver).quantity(30).event(event).build();
    }

    private Event buildEvent(User user) {
        return Event.builder().title("test").description("test test").organizer(user).build();
    }


}

