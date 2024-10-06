package gov.iti.jets.testing;

import gov.iti.jets.testing.domain.*;
import gov.iti.jets.testing.domain.enums.TicketType;
import gov.iti.jets.testing.domain.enums.UserRole;
import gov.iti.jets.testing.persistence.EventRepository;
import gov.iti.jets.testing.persistence.RegistrationRepository;
import gov.iti.jets.testing.persistence.TicketRepository;
import gov.iti.jets.testing.persistence.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.time.LocalDateTime;

@SpringBootApplication
public class SpringTestingApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringTestingApplication.class, args);
    }
//    @Bean
//    CommandLineRunner seedData(UserRepository userRepository, EventRepository eventRepository,
//                               TicketRepository ticketRepository, RegistrationRepository registrationRepository) {
//        return args -> {
//
//            registrationRepository.deleteAll();
//            ticketRepository.deleteAll();
//            eventRepository.deleteAll();
//            userRepository.deleteAll();
//
//            User organizer = User.builder()
//                    .firstName("salma")
//                    .lastName("fayez")
//                    .role(UserRole.ORGANIZER)
//                    .email("salma.fayez@gmail.com")
//                    .password("12345678")
//                    .build();
//
//            User attendee1 = User.builder()
//                    .firstName("ahmed").lastName("fayez")
//                    .role(UserRole.ATTENDEE)
//                    .email("ahmed.fayez@gmail.com")
//                    .password("12345678")
//                    .build();
//
//            User attendee2 = User.builder()
//                    .firstName("sahar").lastName("fayez")
//                    .role(UserRole.ATTENDEE)
//                    .email("sahar.fayez@gmail.com")
//                    .password("12345678")
//                    .build();
//
//
//            userRepository.save(organizer);
//            userRepository.save(attendee1);
//            userRepository.save(attendee2);
//
//            Event event1 = Event.builder()
//                    .title("event1")
//                    .description("event1-description")
//                    .location("Giza")
//                    .organizer(organizer)
//                    .startDate(LocalDateTime.now())
//                    .endDate(LocalDateTime.of(2033, 9, 29, 3, 0))
//                    .build();
//
//            Event event2 = Event.builder()
//                    .title("event2")
//                    .description("event2-description")
//                    .location("Giza")
//                    .organizer(organizer)
//                    .startDate(LocalDateTime.now())
//                    .endDate(LocalDateTime.of(2033, 9, 29, 3, 0))
//                    .build();
//
//
//            organizer.addEvent(event1);
//            organizer.addEvent(event2);
//
//            eventRepository.save(event1);
//            eventRepository.save(event2);
//
//            Ticket ticket1 = Ticket.builder()
//                    .price(50.0)
//                    .type(TicketType.Silver)
//                    .quantity(50)
//                    .event(event1)
//                    .build();
//
//            Ticket ticket2 = Ticket.builder()
//                    .price(100.0)
//                    .type(TicketType.Gold)
//                    .quantity(50)
//                    .event(event1)
//                    .build();
//
//            Ticket ticket3 = Ticket.builder()
//                    .price(50.0)
//                    .type(TicketType.Silver)
//                    .quantity(50)
//                    .event(event2)
//                    .build();
//
//            Ticket ticket4 = Ticket.builder()
//                    .price(100.0)
//                    .type(TicketType.Gold)
//                    .quantity(50)
//                    .event(event1)
//                    .build();
//
//            event1.addTicket(ticket1);
//            event1.addTicket(ticket2);
//            event2.addTicket(ticket3);
//            event2.addTicket(ticket4);
//
//            ticketRepository.save(ticket1);
//            ticketRepository.save(ticket2);
//            ticketRepository.save(ticket3);
//            ticketRepository.save(ticket4);
//
//            Registration registration1 = Registration.builder()
//                    .id(new RegistrationId(attendee1.getId(), event1.getId(), ticket1.getId()))
//                    .user(attendee1)
//                    .event(event1)
//                    .ticket(ticket1)
//                    .registrationDate(LocalDateTime.now())
//                    .build();
//
//            Registration registration2 = Registration.builder()
//                    .id(new RegistrationId(attendee1.getId(), event1.getId(), ticket2.getId()))
//                    .user(attendee1)
//                    .event(event1)
//                    .ticket(ticket2)
//                    .registrationDate(LocalDateTime.now())
//                    .build();
//
//            Registration registration3 = Registration.builder()
//                    .id(new RegistrationId(attendee2.getId(), event2.getId(), ticket3.getId()))
//                    .user(attendee2)
//                    .event(event2)
//                    .ticket(ticket3)
//                    .registrationDate(LocalDateTime.now())
//                    .build();
//
//            Registration registration4 = Registration.builder()
//                    .id(new RegistrationId(attendee2.getId(), event2.getId(), ticket4.getId()))
//                    .user(attendee2)
//                    .event(event2)
//                    .ticket(ticket4)
//                    .registrationDate(LocalDateTime.now())
//                    .build();
//
//            registrationRepository.save(registration1);
//            registrationRepository.save(registration2);
//            registrationRepository.save(registration3);
//            registrationRepository.save(registration4);
//
//        };
//    }

}


 /*
    DB

    2. Autowire a repository and don't include the H2 database to throw the exception
    3. Disable the automatic configuration of the database
    4. Try to autowire a controller to see that it isn't instantiated
    5. Explore the @DataJpaTest annotation
    6. Add the H2 dependency and try to run the migration scripts to throw exception
    6. Disable using the scripts and using the mapping from the entities
    7. See the disadvantages of using the in memory database
    8. add the test containers dependency and instantiate the Container
    9. change the container tag
    9. try to run and see the logs of the container
    10. make a break point to see the containers
    11. see the context sharing between the tests
    12. extract the container to an abstract class
*/


/*
    WEB

    1. Explore the annotation
    2. autowire the controller and check that it isn't null
    3. Introduce the MockBean annotation to autowire the service layer
    4. Check the NotNull again
    5. set up the mock
    6. perform a simple get request and check result is ok
    7. explore the different methods available like the content type
    8. perform a request for a get endpoint that accepts path variable or request param
    9. perform a request for a post endpoint that takes a body
    10. autowire the object mapper to be used to serialize and deserialize
    11. introduce the doNothing method of mockito
    12. perform a request for a post endpoint that accepts file and introduce the multipart
    13. introduce the andDo(print()) --> from the MockMvcResultHandlers
    14. Return the results and parse the response
    15. perform a request for an authenticated endpoint

 */

