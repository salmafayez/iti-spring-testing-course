package gov.iti.jets.testing.service;

import gov.iti.jets.testing.domain.Event;

import java.util.List;

public interface EventService {
    List<Event> getAll();

    Event getById(Long id);
}

