package gov.iti.jets.testing.service.impl;

import gov.iti.jets.testing.configs.DbConfigurations;
import gov.iti.jets.testing.domain.Event;
import gov.iti.jets.testing.domain.Ticket;
import gov.iti.jets.testing.domain.User;
import gov.iti.jets.testing.domain.enums.TicketType;
import gov.iti.jets.testing.domain.enums.UserRole;
import gov.iti.jets.testing.persistence.EventRepository;
import gov.iti.jets.testing.persistence.TicketRepository;
import gov.iti.jets.testing.persistence.UserRepository;
import gov.iti.jets.testing.web.dtos.RegistrationDto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class RegistrationServiceImplTest extends DbConfigurations {

    @Autowired
    private TestEntityManager testEntityManager;
    @MockBean
    private UserRepository userRepository;
    @MockBean
    private EventRepository eventRepository;
    @MockBean
    private TicketRepository ticketRepository;
    @Autowired
    private RegistrationServiceImpl registrationService;

    @Test
    @DisplayName("Getting the user")
    void getUserForRegistration() {
        User user = User.builder()
                .firstName("Abdulrahman")
                .lastName("Almohandis")
                .email("a@a.com")
                .password("123456")
                .role(UserRole.ORGANIZER)
                .build();

        testEntityManager.persist(user);

        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));

        User user1 = userRepository.findById(user.getId()).orElse(null);

        assertThat(user1).isNotNull();
        assertThat(user1.getId()).isEqualTo(user.getId());
        verify(userRepository, times(1)).findById(user.getId());
    }

    @Test
    @DisplayName("Getting the event")
    void getEventForRegistration() {
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

        when(eventRepository.findById(event.getId())).thenReturn(Optional.of(event));
        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));

        User user1 = userRepository.findById(user.getId()).orElse(null);
        Event event1 = eventRepository.findById(event.getId()).orElse(null);

        assertThat(event1).isNotNull();
        assertThat(event1.getId()).isEqualTo(event.getId());
        assertThat(user1).isNotNull();
        assertThat(user1.getId()).isEqualTo(user.getId());
        verify(userRepository, times(1)).findById(user.getId());
        verify(eventRepository, times(1)).findById(event.getId());
    }

    @Test
    @DisplayName("Getting the ticket")
    void getTicketForRegistration() {
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

        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));
        when(eventRepository.findById(event.getId())).thenReturn(Optional.of(event));
        when(ticketRepository.findTicketByTypeAndEvent_Id(TicketType.Gold, event.getId())).thenReturn(Optional.of(ticket));

        User user1 = userRepository.findById(user.getId()).orElse(null);
        Event event1 = eventRepository.findById(event.getId()).orElse(null);
        Ticket ticket1 = ticketRepository.findTicketByTypeAndEvent_Id(TicketType.Gold, event.getId()).orElse(null);

        assertThat(user1).isNotNull();
        assertThat(user1.getId()).isEqualTo(user.getId());
        verify(userRepository, times(1)).findById(user.getId());
        assertThat(event1).isNotNull();
        assertThat(event1.getId()).isEqualTo(event.getId());
        verify(eventRepository, times(1)).findById(event.getId());
        assertThat(ticket1).isNotNull();
        assertThat(ticket1.getId()).isEqualTo(ticket.getId());
        verify(ticketRepository, times(1)).findTicketByTypeAndEvent_Id(TicketType.Gold, event.getId());
    }

    @Test
    @DisplayName("Throwing exception when ticket quantity is less than or equal to 0")
    void throwsExceptionWhenTicketQuantityIsZero() {
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
                .quantity(0)
                .build();

        testEntityManager.persist(ticket);

        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));
        when(eventRepository.findById(event.getId())).thenReturn(Optional.of(event));
        when(ticketRepository.findTicketByTypeAndEvent_Id(TicketType.Gold, event.getId())).thenReturn(Optional.of(ticket));

        RuntimeException exception = org.junit.jupiter.api.Assertions.assertThrows(RuntimeException.class, () -> {
            registrationService.register(RegistrationDto.builder()
                    .userId(user.getId())
                    .eventId(event.getId())
                    .ticketType(TicketType.Gold.getName())
                    .build());
        });
        assertThat(exception.getMessage()).isEqualTo("there is no available tickets");
    }

    @Test
    @DisplayName("Reducing ticket quantity and saving ticket and registration")
    void reduceTicketQuantityAndSaveTicketAndRegistration() {
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
                .quantity(2)
                .build();

        testEntityManager.persist(ticket);

        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));
        when(eventRepository.findById(event.getId())).thenReturn(Optional.of(event));
        when(ticketRepository.findTicketByTypeAndEvent_Id(TicketType.Gold, event.getId())).thenReturn(Optional.of(ticket));

        registrationService.register(RegistrationDto.builder()
                .userId(user.getId())
                .eventId(event.getId())
                .ticketType(TicketType.Gold.getName())
                .build());

        assertThat(ticket.getQuantity()).isEqualTo(1);
        verify(ticketRepository, times(1)).save(ticket);
    }
}