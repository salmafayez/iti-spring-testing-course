package gov.iti.jets.testing.persistence;

import gov.iti.jets.testing.domain.Event;
import gov.iti.jets.testing.domain.Ticket;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TicketRepository extends JpaRepository<Ticket, Long> {

}
