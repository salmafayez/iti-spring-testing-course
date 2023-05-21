package gov.iti.jets.testing.demo.persistence;

import gov.iti.jets.testing.domain.*;
import gov.iti.jets.testing.domain.enums.TicketType;
import gov.iti.jets.testing.domain.enums.UserRole;
import gov.iti.jets.testing.persistence.EventRepository;
import gov.iti.jets.testing.persistence.RegistrationRepository;
import gov.iti.jets.testing.persistence.TicketRepository;
import gov.iti.jets.testing.persistence.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;




class RegistrationRepositoryTest22 extends AbstractJpaTests{

    @Autowired
    private RegistrationRepository registrationRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TicketRepository ticketRepository;

    @Autowired
    private EventRepository eventRepository;


    @Test
    void getRegistrations(){
        registrationRepository.deleteAll();

            User organizer = User.builder()
                    .firstName("salma")
                    .lastName("fayez")
                    .role(UserRole.ORGANIZER)
                    .email("salma.fayez@gmail.com")
                    .password("12345678")
                    .build();

            User attendee1 = User.builder()
                    .firstName("ahmed").lastName("fayez")
                    .role(UserRole.ATTENDEE)
                    .email("ahmed.fayez@gmail.com")
                    .password("12345678")
                    .build();

            User attendee2 = User.builder()
                    .firstName("sahar").lastName("fayez")
                    .role(UserRole.ATTENDEE)
                    .email("sahar.fayez@gmail.com")
                    .password("12345678")
                    .build();


            userRepository.save(organizer);
            userRepository.save(attendee1);
            userRepository.save(attendee2);

            Event event1 = Event.builder()
                    .title("event1")
                    .description("event1-description")
                    .location("Giza")
                    .organizer(organizer)
                    .startDate(LocalDateTime.now())
                    .endDate(LocalDateTime.of(2033, 9, 29, 3, 0))
                    .build();

            Event event2 = Event.builder()
                    .title("event2")
                    .description("event2-description")
                    .location("Giza")
                    .organizer(organizer)
                    .startDate(LocalDateTime.now())
                    .endDate(LocalDateTime.of(2033, 9, 29, 3, 0))
                    .build();


            organizer.addEvent(event1);
            organizer.addEvent(event2);

            eventRepository.save(event1);
            eventRepository.save(event2);

            Ticket ticket1 = Ticket.builder()
                    .price(50.0)
                    .type(TicketType.Silver)
                    .quantity(50)
                    .event(event1)
                    .build();

            Ticket ticket2 = Ticket.builder()
                    .price(100.0)
                    .type(TicketType.Gold)
                    .quantity(50)
                    .event(event1)
                    .build();

            Ticket ticket3 = Ticket.builder()
                    .price(50.0)
                    .type(TicketType.Silver)
                    .quantity(50)
                    .event(event2)
                    .build();

            Ticket ticket4 = Ticket.builder()
                    .price(100.0)
                    .type(TicketType.Gold)
                    .quantity(50)
                    .event(event1)
                    .build();

            event1.addTicket(ticket1);
            event1.addTicket(ticket2);
            event2.addTicket(ticket3);
            event2.addTicket(ticket4);

            ticketRepository.save(ticket1);
            ticketRepository.save(ticket2);
            ticketRepository.save(ticket3);
            ticketRepository.save(ticket4);

            Registration registration1 = Registration.builder()
                    .id(new RegistrationId(attendee1.getId(), event1.getId(), ticket1.getId()))
                    .user(attendee1)
                    .event(event1)
                    .ticket(ticket1)
                    .registrationDate(LocalDateTime.now())
                    .build();

            Registration registration2 = Registration.builder()
                    .id(new RegistrationId(attendee1.getId(), event1.getId(), ticket2.getId()))
                    .user(attendee1)
                    .event(event1)
                    .ticket(ticket2)
                    .registrationDate(LocalDateTime.now())
                    .build();

            Registration registration3 = Registration.builder()
                    .id(new RegistrationId(attendee2.getId(), event2.getId(), ticket3.getId()))
                    .user(attendee2)
                    .event(event2)
                    .ticket(ticket3)
                    .registrationDate(LocalDateTime.now())
                    .build();

            Registration registration4 = Registration.builder()
                    .id(new RegistrationId(attendee2.getId(), event2.getId(), ticket4.getId()))
                    .user(attendee2)
                    .event(event2)
                    .ticket(ticket4)
                    .registrationDate(LocalDateTime.now())
                    .build();

            registrationRepository.save(registration1);
            registrationRepository.save(registration2);
            registrationRepository.save(registration3);
            registrationRepository.save(registration4);

            //Act
        List<Registration> registrationByUserEmail =
                registrationRepository.getRegistrationByUserEmailAndRegistrationDateAfter("ahmed.fayez@gmail.com",
                        LocalDateTime.of(1999, 7, 3,4,4));
        assertEquals(2, registrationByUserEmail.size());

    }

    @Test
    void getRegistrations1(){
        List<Registration> registrationByUserEmail =
                registrationRepository.getRegistrationByUserEmailAndRegistrationDateAfter("ahmed.fayez@gmail.com",
                        LocalDateTime.of(1999, 7, 3,4,4));

        assertEquals(0, registrationByUserEmail.size());
    }

}