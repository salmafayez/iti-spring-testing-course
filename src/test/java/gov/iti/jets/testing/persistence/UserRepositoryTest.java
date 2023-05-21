package gov.iti.jets.testing.persistence;

import com.github.springtestdbunit.DbUnitTestExecutionListener;
import com.github.springtestdbunit.annotation.DatabaseOperation;
import com.github.springtestdbunit.annotation.DatabaseSetup;
import gov.iti.jets.testing.domain.User;
import gov.iti.jets.testing.domain.enums.UserRole;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.jdbc.Sql;

import static org.assertj.core.api.Assertions.assertThat;

@TestExecutionListeners(value = DbUnitTestExecutionListener.class,
        mergeMode = TestExecutionListeners.MergeMode.MERGE_WITH_DEFAULTS)
class UserRepositoryTest extends AbstractJpaTest{

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private EntityManager entityManager;

    /*
    how to seed data
        1. programmatically
        2. sql scripts
        3. third party libraries --> dbunit
     */

    // seed data programmatically
    @Test
    void get_all_users() {
        //Arrange
        User user = creatUser();
        entityManager.persist(user);


        //Act and assert
        assertThat(userRepository.count()).isEqualTo(1);
    }

    private static User creatUser() {
        return User.builder()
                .firstName("salma")
                .lastName("fayez")
                .email("salma")
                .password("ddd")
                .role(UserRole.ORGANIZER)
                .build();
    }

    //seed data using sql scripts
    @Test
    @Sql("/scripts/sql/seeding.sql")
    void get_all_users2() {
        assertThat(userRepository.count()).isEqualTo(1);
    }

    //seed data using dbunit
    @Test
    @DatabaseSetup(value = "/scripts/dbunit/users.xml", type = DatabaseOperation.INSERT)
    void get_all_users3() {
        assertThat(userRepository.count()).isEqualTo(1);
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