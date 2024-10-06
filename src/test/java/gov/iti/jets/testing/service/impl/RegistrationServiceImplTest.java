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
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest(
        properties = {
                "spring.flyway.enabled=false", "spring-jpa.generate-ddl=true"
        }
)
@AutoConfigureTestDatabase
@AutoConfigureMockMvc
@Transactional
class RegistrationServiceImplTest {
    
    @InjectMocks RegistrationServiceImpl registrationService;
    @Autowired EntityManager entityManager;
    @Mock private EventRepository eventRepository;
    @Mock private TicketRepository ticketRepository;
    @Mock private UserRepository userRepository;
    @Mock private RegistrationRepository registrationRepository;
    
    private static Ticket buildTicket(Event event) {
        return Ticket.builder()
                     .price(999.99)
                     .type(TicketType.Silver)
                     .quantity(500)
                     .event(event)
                     .build();
    }
    
    private static User buildUser() {
        return User.builder()
                   .firstName("Mohamed")
                   .lastName("Saber")
                   .email("mohamed.saber.mohamed.dev@gmail.com")
                   .password("12121212")
                   .role(UserRole.ATTENDEE)
                   .build();
    }
    
    private static Event buildEvent(User user) {
        return Event.builder()
                    .title("Test Event")
                    .description("Test Description")
                    .organizer(user)
                    .build();
    }
    
    @BeforeEach void setUp() {
    }
    
    @Test void getUserSuccessTest() {
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
    
    @Test void getEventSuccessTest() {
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
        Assertions.assertThat(fetchedEvent)
                  .isEqualTo(event);
        verify(eventRepository, times(1)).findById(event.getId());
        
    }
    
    @Test void getTicketSuccessTest() {
        // Arrange
        User user = buildUser();
        entityManager.persist(user);
        
        Event event = buildEvent(user);
        entityManager.persist(event);
        
        Ticket ticket = buildTicket(event);
        entityManager.persist(ticket);
        entityManager.flush();
        
        // Act
        when(ticketRepository.findTicketByTypeAndEvent_Id(TicketType.Gold,
                                                          event.getId())).thenReturn(Optional.of(ticket));
        Ticket fetchedTicket = registrationService.getTicket(ticket.getId(), TicketType.Gold.getName());
        
        // Assert
        Assertions.assertThat(fetchedTicket)
                  .isEqualTo(ticket);
        verify(ticketRepository, times(1)).findTicketByTypeAndEvent_Id(TicketType.Gold, event.getId());
    }
    
    @Test void shouldThrowExceptionWhenNoTicketsAvailable() {
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
        when(ticketRepository.findTicketByTypeAndEvent_Id(TicketType.Gold,
                                                          event.getId())).thenReturn(Optional.of(ticket)); // Update here
        when(eventRepository.findById(event.getId())).thenReturn(Optional.of(event));
        
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            registrationService.register(registrationDto);
        });
        
        // Assert
        assertThat(exception.getMessage()).isEqualTo("there is no available tickets");
    }
    
    @Test void shouldReduceTicketQuantityAndSaveTicketAndRegistration() {
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
        when(ticketRepository.findTicketByTypeAndEvent_Id(TicketType.getByName(TicketType.Gold.getName()),
                                                          event.getId())).thenReturn(Optional.of(ticket));
        when(eventRepository.findById(event.getId())).thenReturn(Optional.of(event));
        
        registrationService.register(registrationDto);
        
        // Assert
        assertThat(ticket.getQuantity()).isEqualTo(4);
        verify(ticketRepository, times(1)).save(ticket);
        verify(registrationRepository, times(1)).save(any(Registration.class));
    }
}