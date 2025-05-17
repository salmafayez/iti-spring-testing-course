package gov.iti.jets.testing.service.impl;

import gov.iti.jets.testing.domain.User;
import gov.iti.jets.testing.domain.enums.TicketType;
import gov.iti.jets.testing.persistence.EventRepository;
import gov.iti.jets.testing.persistence.RegistrationRepository;
import gov.iti.jets.testing.persistence.TicketRepository;
import gov.iti.jets.testing.persistence.UserRepository;
import gov.iti.jets.testing.service.RegistrationService;
import gov.iti.jets.testing.web.dtos.RegistrationDto;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RegistrationServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private EventRepository eventRepository;

    @Mock
    private TicketRepository ticketRepository;

    @Mock
    private RegistrationRepository registrationRepository;

    @InjectMocks
    private RegistrationServiceImpl cut;

    @Test
    void test_user_not_found() {
        //Arrange
        when(userRepository.findById(any())).thenThrow(new RuntimeException());

        RegistrationDto dto = RegistrationDto.builder()
                .userId(1L)
                .eventId(1L)
                .ticketType(TicketType.Gold.getName())
                .build();

        //Act and Assert
        Assertions.assertThrows(RuntimeException.class, () -> cut.register(dto));
        verify(userRepository, times(1)).findById(1L);
        verifyNoInteractions(eventRepository);
    }

    @Test
    void test_event_not_found() {
        //Arrange
        when(userRepository.findById(any())).thenReturn(Optional.of(new User()));
        when(eventRepository.findById(any())).thenThrow(new RuntimeException());

        RegistrationDto dto = RegistrationDto.builder()
                .userId(1L)
                .eventId(1L)
                .ticketType(TicketType.Gold.getName())
                .build();

        //Act and Assert
        Assertions.assertThrows(RuntimeException.class, () -> cut.register(dto));
        verify(userRepository, times(1)).findById(1L);
        verify(eventRepository, times(1)).findById(1L);
    }

    //TODO write unit test for tickets not available scenario
}