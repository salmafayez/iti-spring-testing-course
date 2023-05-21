package gov.iti.jets.testing.demo.persistence;

import gov.iti.jets.testing.domain.User;
import gov.iti.jets.testing.domain.enums.UserRole;
import gov.iti.jets.testing.persistence.UserRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import static org.junit.jupiter.api.Assertions.*;


class RegistrationRepositoryTest extends AbstractJpaTests{

    @Autowired
    private UserRepository userRepository;

    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    private TestEntityManager testEntityManager;

    @Test
    void getRegistrations(){
        User u = User.builder()
                .firstName("salma")
                .lastName("fayez")
                .role(UserRole.ORGANIZER)
                .password("ddd")
                .email("fff")
                .build();
        User uu =testEntityManager.persistAndFlush(u);

//        assertTrue(testEntityManager.f);
assertNotNull(uu);
//        long count = userRepository.count();
//        assertEquals(1, count);
//        assertNotNull(user.getId());
    }

    @Test
    void getRegistrations2(){

        long count = userRepository.count();
        assertEquals(0, count);
    }


     /*
    Scenario for Demo
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
    - the context starts with the bean needed in each test and add to it when needed
    - the context tries to add the new bean and refresh
    - it starts different contexts when use different annotation
    - when the context refreshes it validates the scripts if they are already migrated and doesn't migrate them again
    - if one test annotated with test containers and other tests with none database it will refresh the context
    for the new test and connect to the development database
    - every test will start its container unless you stared the container by yourself
    - the connection will close after the test class finishes and the next class will try to connect to the same conatiner
    - there is no problem with entity manager and test entity manager both commit and roll back at the end
     */
}