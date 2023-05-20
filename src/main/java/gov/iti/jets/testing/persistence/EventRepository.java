package gov.iti.jets.testing.persistence;

import gov.iti.jets.testing.domain.Event;
import gov.iti.jets.testing.domain.Registration;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface EventRepository extends JpaRepository<Event, Long> {

}
