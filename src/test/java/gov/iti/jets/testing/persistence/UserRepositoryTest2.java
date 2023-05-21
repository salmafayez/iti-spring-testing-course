package gov.iti.jets.testing.persistence;

import gov.iti.jets.testing.domain.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;


class UserRepositoryTest2 extends AbstractJpaTest{

    @Autowired
    private UserRepository userRepository;

    @Test
    void get_all_users() {

        List<User> users = userRepository.findAll();
        assertThat(userRepository).isNotNull();
//        assertThat(userRepository.count()).isEqualTo(3);
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

}