//package gov.iti.jets.testing.web.controllers;
//
//import gov.iti.jets.testing.domain.*;
//import gov.iti.jets.testing.domain.enums.TicketType;
//import gov.iti.jets.testing.domain.enums.UserRole;
//import gov.iti.jets.testing.persistence.EventRepository;
//import gov.iti.jets.testing.persistence.RegistrationRepository;
//import gov.iti.jets.testing.persistence.TicketRepository;
//import gov.iti.jets.testing.persistence.UserRepository;
//import gov.iti.jets.testing.service.UserService;
//import gov.iti.jets.testing.service.impl.RegistrationServiceImpl;
//import gov.iti.jets.testing.web.dtos.RegistrationDto;
//import jakarta.persistence.EntityManager;
//import org.assertj.core.api.Assertions;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.MockitoAnnotations;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
//import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.transaction.annotation.Transactional;
//
//import java.util.Optional;
//
//import static gov.iti.jets.testing.service.impl.RegistrationServiceImpl.creatRegistration;
//import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
//import static org.junit.jupiter.api.Assertions.*;
//import static org.mockito.ArgumentMatchers.any;
//import static org.mockito.Mockito.*;
//
//@SpringBootTest(properties = {
//        "spring.flyway.enabled=false",
//        "spring-jpa.generate-ddl=true"
//})
//@AutoConfigureTestDatabase
//@AutoConfigureMockMvc
//@Transactional
//class EventRegistrationTest {
//
//    @Mock
//    private EventRepository eventRepoMock;
//
//    @Mock
//    private TicketRepository ticketRepoMock;
//
//    @Mock
//    private UserRepository userRepoMock;
//
//    @Mock
//    private RegistrationRepository registrationRepoMock;
//
//    @InjectMocks
//    RegistrationServiceImpl registrationServiceImplMock;
//
//    @Autowired
//    EntityManager entityManagerInjected;
//
//    @BeforeEach
//    void initialize() {
//    }
//
//    @Test
//    void retrieveUserTest() {
//        // Arrange
//        User testUser = createUser();
//        entityManagerInjected.persist(testUser);
//        entityManagerInjected.flush();
//
//        // Act
//        when(userRepoMock.findById(testUser.getId())).thenReturn(Optional.of(testUser));
//        User resultUser = registrationServiceImplMock.getUser(testUser.getId());
//
//        // Assert
//        assertThat(resultUser).isEqualTo(testUser);
//        verify(userRepoMock, times(1)).findById(testUser.getId());
//    }
//
//    @Test
//    void retrieveEventTest() {
//        // Arrange
//        User testUser = createUser();
//        entityManagerInjected.persist(testUser);
//        entityManagerInjected.flush();
//
//        Event event = createEvent(testUser);
//        entityManagerInjected.persist(event);
//        entityManagerInjected.flush();
//
//        // Act
//        when(eventRepoMock.findById(event.getId())).thenReturn(Optional.of(event));
//        Event resultEvent = registrationServiceImplMock.getEvent(event.getId());
//
//        // Assert
//        Assertions.assertThat(resultEvent).isEqualTo(event);
//        verify(eventRepoMock, times(1)).findById(event.getId());
//    }
//
//
//    @Test
//    void retrieveTicketTest() {
//        // Arrange
//        User testUser = createUser();
//        entityManagerInjected.persist(testUser);
//
//        Event event = createEvent(testUser);
//        entityManagerInjected.persist(event);
//
//        Ticket ticket = createTicket(event);
//        entityManagerInjected.persist(ticket);
//        entityManagerInjected.flush();
//
//        // Act
//        when(ticketRepoMock.findTicketByTypeAndEvent_Id(TicketType.Gold, event.getId())).thenReturn(Optional.of(ticket));
//        Ticket resultTicket = registrationServiceImplMock.getTicket(ticket.getId(), TicketType.Gold.getName());
//
//        // Assert
//        Assertions.assertThat(resultTicket).isEqualTo(ticket);
//        verify(ticketRepoMock, times(1)).findTicketByTypeAndEvent_Id(TicketType.Gold, event.getId());
//    }
//
//    @Test
//    void shouldThrowExceptionWhenNoTicketsAvailableTest() {
//        // Arrange
//        User testUser = createUser();
//        entityManagerInjected.persist(testUser);
//
//        Event event = createEvent(testUser);
//        entityManagerInjected.persist(event);
//
//        Ticket ticket = createTicket(event);
//        ticket.setQuantity(0);
//        entityManagerInjected.persist(ticket);
//        entityManagerInjected.flush();
//
//        RegistrationDto regDto = new RegistrationDto(testUser.getId(), event.getId(), TicketType.Gold.getName());
//
//        // Act
//        when(userRepoMock.findById(testUser.getId())).thenReturn(Optional.of(testUser));
//        when(ticketRepoMock.findTicketByTypeAndEvent_Id(TicketType.Gold, event.getId())).thenReturn(Optional.of(ticket));
//        when(eventRepoMock.findById(event.getId())).thenReturn(Optional.of(event));
//
//        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
//            registrationServiceImplMock.register(regDto);
//        });
//
//        // Assert
//        assertThat(exception.getMessage()).isEqualTo("there is no available tickets");
//    }
//
//    @Test
//    void reduceTicketQuantityAndSaveTest() {
//        // Arrange
//        User testUser = createUser();
//        entityManagerInjected.persist(testUser);
//
//        Event event = createEvent(testUser);
//        entityManagerInjected.persist(event);
//
//        Ticket ticket = createTicket(event);
//        ticket.setQuantity(5);
//        entityManagerInjected.persist(ticket);
//        entityManagerInjected.flush();
//
//        RegistrationDto regDto = new RegistrationDto(testUser.getId(), event.getId(), TicketType.Gold.getName());
//
//        // Act
//        when(userRepoMock.findById(testUser.getId())).thenReturn(Optional.of(testUser));
//        when(ticketRepoMock.findTicketByTypeAndEvent_Id(TicketType.getByName(TicketType.Gold.getName()), event.getId())).thenReturn(Optional.of(ticket));
//        when(eventRepoMock.findById(event.getId())).thenReturn(Optional.of(event));
//
//        registrationServiceImplMock.register(regDto);
//
//        // Assert
//        assertThat(ticket.getQuantity()).isEqualTo(4);
//        verify(ticketRepoMock, times(1)).save(ticket);
//        verify(registrationRepoMock, times(1)).save(any(Registration.class));
//    }
//
//    private static Ticket createTicket(Event event) {
//        return Ticket.builder()
//                .price(100.0)
//                .type(TicketType.Gold)
//                .quantity(10)
//                .event(event)
//                .build();
//    }
//
//    private static User createUser() {
//        return User.builder()
//                .firstName("salma")
//                .lastName("fayez")
//                .email("salma.fayez@gmail.com")
//                .password("12345678")
//                .role(UserRole.ORGANIZER)
//                .build();
//    }
//
//    private static Event createEvent(User user) {
//        return Event.builder()
//                .title("test1")
//                .description("test")
//                .organizer(user)
//                .build();
//    }
//}



package gov.iti.jets.testing.web.controllers;

import gov.iti.jets.testing.domain.*;
import gov.iti.jets.testing.domain.enums.TicketType;
import gov.iti.jets.testing.domain.enums.UserRole;
import gov.iti.jets.testing.persistence.EventRepository;
import gov.iti.jets.testing.persistence.RegistrationRepository;
import gov.iti.jets.testing.persistence.TicketRepository;
import gov.iti.jets.testing.persistence.UserRepository;
import gov.iti.jets.testing.service.UserService;
import gov.iti.jets.testing.service.impl.RegistrationServiceImpl;
import gov.iti.jets.testing.web.dtos.RegistrationDto;
import jakarta.persistence.EntityManager;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static gov.iti.jets.testing.service.impl.RegistrationServiceImpl.creatRegistration;
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
class RegisterFunctionControllerTest {

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
        User fetchedUser = registrationService.getUser(user.getId());

        // Assert
        assertThat(fetchedUser).isEqualTo(user);
        verify(userRepository, times(1)).findById(user.getId());

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

        Event fetchedEvent = registrationService.getEvent(event.getId());

        // Assert
        Assertions.assertThat(fetchedEvent).isEqualTo(event);
        verify(eventRepository, times(1)).findById(event.getId());

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
        when(ticketRepository.findTicketByTypeAndEvent_Id(TicketType.Gold, event.getId())).thenReturn(Optional.of(ticket));
        Ticket fetchedTicket = registrationService.getTicket(ticket.getId(), TicketType.Gold.getName());

        // Assert
        Assertions.assertThat(fetchedTicket).isEqualTo(ticket);
        verify(ticketRepository, times(1)).findTicketByTypeAndEvent_Id(TicketType.Gold, event.getId());
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

        RegistrationDto registrationDto = new RegistrationDto(user.getId(), event.getId(), TicketType.Gold.getName());

        // Act
        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));
        when(ticketRepository.findTicketByTypeAndEvent_Id(TicketType.Gold, event.getId())).thenReturn(Optional.of(ticket)); // Update here
        when(eventRepository.findById(event.getId())).thenReturn(Optional.of(event));

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            registrationService.register(registrationDto);
        });

        // Assert
        assertThat(exception.getMessage()).isEqualTo("there is no available tickets");
    }

    @Test
    void shouldReduceTicketQuantityAndSaveTicketAndRegistration() {
        // Arrange
        User user = buildUser();
        entityManager.persist(user);

        Event event = buildEvent(user);
        entityManager.persist(event);

        Ticket ticket = buildTicket(event);
        ticket.setQuantity(5);
        entityManager.persist(ticket);
        entityManager.flush();

        RegistrationDto registrationDto = new RegistrationDto(user.getId(), event.getId(), TicketType.Gold.getName());

        // Act
        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));
        when(ticketRepository.findTicketByTypeAndEvent_Id(TicketType.getByName(TicketType.Gold.getName()), event.getId())).thenReturn(Optional.of(ticket));
        when(eventRepository.findById(event.getId())).thenReturn(Optional.of(event));

        registrationService.register(registrationDto);

        // Assert
        assertThat(ticket.getQuantity()).isEqualTo(4);
        verify(ticketRepository, times(1)).save(ticket);
        verify(registrationRepository, times(1)).save(any(Registration.class));
    }

    private static Ticket buildTicket(Event event) {
        return Ticket.builder()
                .price(100.0)
                .type(TicketType.Gold)
                .quantity(10)
                .event(event)
                .build();
    }

    private static User buildUser() {
        return User.builder().
                firstName("salma")
                .lastName("fayez")
                .email("salma.fayez@gmail.com")
                .password("12345678")
                .role(UserRole.ORGANIZER)
                .build();
    }

    private static Event buildEvent(User user) {
        return Event.builder()
                .title("test1")
                .description("test")
                .organizer(user)
                .build();
    }
}
