package gov.iti.jets.testing.persistence;

import gov.iti.jets.testing.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {

}
