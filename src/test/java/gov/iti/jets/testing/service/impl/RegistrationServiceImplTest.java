package gov.iti.jets.testing.service.impl;

import gov.iti.jets.testing.domain.*;
import gov.iti.jets.testing.domain.enums.TicketType;
import gov.iti.jets.testing.persistence.EventRepository;
import gov.iti.jets.testing.persistence.RegistrationRepository;
import gov.iti.jets.testing.persistence.TicketRepository;
import gov.iti.jets.testing.persistence.UserRepository;
import gov.iti.jets.testing.service.RegistrationService;
import gov.iti.jets.testing.web.dtos.RegistrationDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
@ContextConfiguration(classes = RegistrationServiceImpl.class)
class RegistrationServiceImplTest {

    @Autowired
    private RegistrationService registrationService;

    @MockBean
    private UserRepository userRepository;

    @MockBean
    private EventRepository eventRepository;

    @MockBean
    private TicketRepository ticketRepository;

    @MockBean
    private RegistrationRepository registrationRepository;

    private RegistrationDto registrationDto;
    private User user;
    private Event event;
    private Ticket ticket;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setId(1L);

        event = new Event();
        event.setId(1L);

        ticket = new Ticket();
        ticket.setId(1L);
        ticket.setQuantity(10);
        ticket.setType(TicketType.Gold);

        registrationDto = new RegistrationDto();
        registrationDto.setUserId(1L);
        registrationDto.setEventId(1L);
        registrationDto.setTicketType("gold");
    }

    @Test
    void register_successfulRegistration() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(eventRepository.findById(1L)).thenReturn(Optional.of(event));
        when(ticketRepository.findTicketByTypeAndEvent_Id(TicketType.Gold, 1L)).thenReturn(Optional.of(ticket));

        registrationService.register(registrationDto);

        assertEquals(9, ticket.getQuantity());
        verify(ticketRepository).save(ticket);
        verify(registrationRepository).save(any(Registration.class));
    }

    @Test
    void register_noUserFound() {
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            registrationService.register(registrationDto);
        });

        assertEquals("no user with this id", exception.getMessage());
    }

    @Test
    void register_noEventFound() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(eventRepository.findById(1L)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            registrationService.register(registrationDto);
        });

        assertEquals("no event with this id", exception.getMessage());
    }

    @Test
    void register_noTicketsFound() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(eventRepository.findById(1L)).thenReturn(Optional.of(event));
        when(ticketRepository.findTicketByTypeAndEvent_Id(TicketType.Gold, 1L)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            registrationService.register(registrationDto);
        });

        assertEquals("no tickets attached to this event", exception.getMessage());
    }

    @Test
    void register_noTicketsAvailable() {
        ticket.setQuantity(0);
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(eventRepository.findById(1L)).thenReturn(Optional.of(event));
        when(ticketRepository.findTicketByTypeAndEvent_Id(TicketType.Gold, 1L)).thenReturn(Optional.of(ticket));

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            registrationService.register(registrationDto);
        });

        assertEquals("there is no available tickets", exception.getMessage());
        verify(ticketRepository, never()).save(ticket);
        verify(registrationRepository, never()).save(any(Registration.class));
    }
}
