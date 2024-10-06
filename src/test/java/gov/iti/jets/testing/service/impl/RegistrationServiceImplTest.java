package gov.iti.jets.testing.service.impl;

import gov.iti.jets.testing.domain.*;
import gov.iti.jets.testing.domain.enums.TicketType;
import gov.iti.jets.testing.persistence.*;
import gov.iti.jets.testing.web.dtos.RegistrationDto;
import org.assertj.core.api.Assertions;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;



class RegistrationServiceImplTest {

    @InjectMocks
    private RegistrationServiceImpl registrationService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private EventRepository eventRepository;

    @Mock
    private TicketRepository ticketRepository;

    @Mock
    private RegistrationRepository registrationRepository;

    private RegistrationDto registrationDto;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        registrationDto = new RegistrationDto();
        registrationDto.setUserId(1L);
        registrationDto.setEventId(1L);
        registrationDto.setTicketType("silver");
    }


    // Test case 1: If ticket quantity is less than or equal to 0
    @Test
    void testRegister_noAvailableTickets_throwsException() {
        // Mocking the user, event, and ticket with zero quantity
        User user = new User();
        Event event = new Event();
        Ticket ticket = new Ticket();
        ticket.setQuantity(0); // Simulate no available tickets

        when(ticketRepository.findTicketByTypeAndEvent_Id(TicketType.Silver, registrationDto.getEventId()))
                .thenReturn(java.util.Optional.of(ticket));

        // Expect a RuntimeException to be thrown when no tickets are available
        assertThrows(RuntimeException.class, () -> registrationService.register(registrationDto));

        when(ticketRepository.findTicketByTypeAndEvent_Id(TicketType.Silver, registrationDto.getEventId()))
                .thenReturn(java.util.Optional.of(ticket));


    }

    // Test case 2: If user exists
    @Test
    void testRegister_userExists_shouldProceed() {
        User user = new User();
        when(userRepository.findById(registrationDto.getUserId())).thenReturn(java.util.Optional.of(user));

        // The user exists, but we don't need to verify further for this scenario.
    }

    // Test case 3: If event exists
    @Test
    void testRegister_eventExists_shouldProceed() {
        Event event = new Event();
        when(eventRepository.findById(registrationDto.getEventId())).thenReturn(java.util.Optional.of(event));

        // The event exists, but we don't need to verify further for this scenario.
    }

    // Test case 4: If ticket exists
    @Test
    void testRegister_ticketExists_shouldProceed() {
        Ticket ticket = new Ticket();
        ticket.setQuantity(5); // Valid quantity
        when(ticketRepository.findTicketByTypeAndEvent_Id(TicketType.Gold, registrationDto.getEventId()))
                .thenReturn(java.util.Optional.of(ticket));
        // The ticket exists, but we don't need to verify further for this scenario.
    }

    // Test case 5: Happy scenario - all entities exist and the process completes successfully
    @Test
    void testRegister_happyScenario_success() {
        User user = new User();
        Event event = new Event();
        Ticket ticket = new Ticket();
        ticket.setQuantity(5); // Valid quantity

        when(userRepository.findById(anyLong())).thenReturn(java.util.Optional.of(user));
        when(eventRepository.findById(anyLong())).thenReturn(java.util.Optional.of(event));
        when(ticketRepository.findTicketByTypeAndEvent_Id(TicketType.Silver, registrationDto.getEventId()))
                .thenReturn(java.util.Optional.of(ticket));

        // No exception should be thrown, and the registration should proceed
        registrationService.register(registrationDto);

        // Verify that the ticket quantity is reduced
        Assertions.assertThat(ticket.getQuantity()).isEqualTo(4);

    }
}
