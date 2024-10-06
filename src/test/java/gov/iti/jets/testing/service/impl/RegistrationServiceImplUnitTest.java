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
import gov.iti.jets.testing.web.dtos.RegistrationDto;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.AutoConfigureTestEntityManager;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.mockito.Mockito.when;

@SpringBootTest(properties = {
        "spring.flyway.enabled=false",
        "spring.jpa.generate-ddl=true"
})
@AutoConfigureTestDatabase
@AutoConfigureTestEntityManager
@Transactional
public class RegistrationServiceImplUnitTest {
    @MockBean
    private UserRepository userRepository;

    @MockBean
    private EventRepository eventRepository;

    @MockBean
    private TicketRepository ticketRepository;

    @MockBean
    private RegistrationRepository registrationRepository;

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private RegistrationServiceImpl service;


    @Test
    public void givenQuantityLessThanOrEqualZeroWhenRegisteringThrowRunTimeException(){

        User organizer = buildUser();
        when(userRepository.findById(organizer.getId())).thenReturn(Optional.of(organizer));

        Event event = buildEvent(organizer);
        when(eventRepository.findById(event.getId())).thenReturn(Optional.of(event));

        Ticket ticket = buildTicket(event,0 );
        when(ticketRepository.findTicketByTypeAndEvent_Id(
                TicketType.Gold, event.getId())).thenReturn(Optional.of(ticket));

        RegistrationDto registrationDto =
                new RegistrationDto(organizer.getId(),event.getId(),
                        TicketType.Gold.getName());

        Assertions.assertThrows(RuntimeException.class, () -> service.register(registrationDto));

    }

    @Test
    public void givenNoUserWhenRegisteringThrowRunTimeException(){
        User organizer = buildUser();
        when(userRepository.findById(organizer.getId())).thenReturn(Optional.empty());

        Event event = buildEvent(organizer);
        when(eventRepository.findById(event.getId())).thenReturn(Optional.of(event));

        Ticket ticket = buildTicket(event,50 );
        when(ticketRepository.findTicketByTypeAndEvent_Id(
                TicketType.Gold, event.getId())).thenReturn(Optional.of(ticket));

        RegistrationDto registrationDto =
                new RegistrationDto(organizer.getId(),event.getId(),
                        TicketType.Gold.getName());

        Assertions.assertThrows(RuntimeException.class, () -> service.register(registrationDto));

    }


    @Test
    public void givenNoTicketWhenRegisteringThrowRunTimeException(){
        User organizer = buildUser();
        when(userRepository.findById(organizer.getId())).thenReturn(Optional.of(organizer));

        Event event = buildEvent(organizer);
        when(eventRepository.findById(event.getId())).thenReturn(Optional.of(event));

        Ticket ticket = buildTicket(event,50 );
        when(ticketRepository.findTicketByTypeAndEvent_Id(
                TicketType.Gold, event.getId())).thenReturn(Optional.empty());

        RegistrationDto registrationDto =
                new RegistrationDto(organizer.getId(),event.getId(),
                        TicketType.Gold.getName());

        Assertions.assertThrows(RuntimeException.class, () -> service.register(registrationDto));

    }

    @Test
    public void givenNoEventWhenRegisteringThrowRunTimeException(){

        User organizer = buildUser();
        when(userRepository.findById(organizer.getId())).thenReturn(Optional.of(organizer));

        Event event = buildEvent(organizer);
        when(eventRepository.findById(event.getId())).thenReturn(Optional.empty());

        Ticket ticket = buildTicket(event,50 );
        when(ticketRepository.findTicketByTypeAndEvent_Id(
                TicketType.Gold, event.getId())).thenReturn(Optional.of(ticket));

        RegistrationDto registrationDto =
                new RegistrationDto(organizer.getId(),event.getId(),
                        TicketType.Gold.getName());

        Assertions.assertThrows(RuntimeException.class, () -> service.register(registrationDto));
    }

    @Test
    public void givenCompleteDataWhenRegisteringWork(){
        User organizer = buildUser();
        when(userRepository.findById(organizer.getId())).thenReturn(Optional.of(organizer));

        Event event = buildEvent(organizer);
        when(eventRepository.findById(event.getId())).thenReturn(Optional.of(event));

        Ticket ticket = buildTicket(event,50 );
        when(ticketRepository.findTicketByTypeAndEvent_Id(
                TicketType.Gold, event.getId())).thenReturn(Optional.of(ticket));

        RegistrationDto registrationDto =
                new RegistrationDto(organizer.getId(),event.getId(),
                        TicketType.Gold.getName());


        service.register(registrationDto);
        Assertions.assertEquals(ticket.getQuantity(),49);

    }

    private static Ticket buildTicket(Event event, int quantity) {
        return Ticket.builder()
                .id(1L)
                .event(event)
                .price(100.0)
                .quantity(quantity)
                .type(TicketType.Gold)
                .build();
    }

    private static Event buildEvent(User organizer) {
        return Event.builder()
                .id(1L)
                .title("event1")
                .description("desc")
                .organizer(organizer)
                .build();
    }

    private static User buildUser() {
        return User.builder()
                .id(1L)
                .firstName("first")
                .lastName("last")
                .email("email")
                .password("123456789")
                .role(UserRole.ORGANIZER)
                .build();
    }


}
