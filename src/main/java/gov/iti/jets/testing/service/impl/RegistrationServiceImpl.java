package gov.iti.jets.testing.service.impl;

import gov.iti.jets.testing.domain.*;
import gov.iti.jets.testing.domain.enums.TicketType;
import gov.iti.jets.testing.persistence.EventRepository;
import gov.iti.jets.testing.persistence.RegistrationRepository;
import gov.iti.jets.testing.persistence.TicketRepository;
import gov.iti.jets.testing.persistence.UserRepository;
import gov.iti.jets.testing.service.RegistrationService;
import gov.iti.jets.testing.web.dtos.RegistrationDto;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class RegistrationServiceImpl implements RegistrationService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private EventRepository eventRepository;

    @Autowired
    private TicketRepository ticketRepository;

    @Autowired
    private RegistrationRepository registrationRepository;

    @Override
    @Transactional
    public void register(RegistrationDto registrationDto) {

        //2 test user exist
        User user = getUser(registrationDto.getUserId());
        //3 test event exist
        Event even = getEvent(registrationDto.getEventId());
        // tickets exist
        Ticket ticket = getTicket(registrationDto.getEventId(), registrationDto.getTicketType());

        // 1 if the quantity less than zero
        if (ticket.getQuantity() <= 0) {
           throw new RuntimeException("there is no available tickets");
        }

        // 5 happy senario
        ticket.setQuantity(ticket.getQuantity() - 1);
        ticketRepository.save(ticket);

        Registration registration = creatRegistration(user, even, ticket);
        registrationRepository.save(registration);
    }

    private static Registration creatRegistration(User user, Event even, Ticket ticket) {
        Registration registration = Registration
                .builder()
                .id(new RegistrationId(user.getId(), even.getId(), ticket.getId()))
                .user(user)
                .event(even)
                .ticket(ticket)
                .registrationDate(LocalDateTime.now())
                .build();
        return registration;
    }

    public Ticket getTicket(Long id, String tickType) {
        return ticketRepository
                .findTicketByTypeAndEvent_Id(TicketType.getByName(tickType), id)
                .orElseThrow(() -> new RuntimeException("no tickets attached to this event"));
    }

    public Event getEvent(Long id) {
        return eventRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("no event with this id"));
    }

    public User getUser(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("no user with this id"));
    }
}
