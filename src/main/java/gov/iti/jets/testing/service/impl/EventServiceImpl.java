package gov.iti.jets.testing.service.impl;

import gov.iti.jets.testing.domain.Event;
import gov.iti.jets.testing.persistence.EventRepository;
import gov.iti.jets.testing.service.EventService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EventServiceImpl implements EventService {

    @Autowired
    private EventRepository eventRepository;

    @Override
    public List<Event> getAll() {
        return eventRepository.findAll();
    }

    @Override
    public Event getById(Long id) {
        return eventRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("event not found"));
    }
}
