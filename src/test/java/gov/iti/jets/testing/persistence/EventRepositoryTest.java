package gov.iti.jets.testing.persistence;

import com.github.springtestdbunit.DbUnitTestExecutionListener;
import gov.iti.jets.testing.config.TestContainersConfig;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.jdbc.Sql;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest(properties = {
//        "spring.flyway.enabled=false",
//        "spring.jpa.generate-ddl=true"
})
@Import(TestContainersConfig.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@TestExecutionListeners(listeners = DbUnitTestExecutionListener.class,
        mergeMode = TestExecutionListeners.MergeMode.MERGE_WITH_DEFAULTS)
class EventRepositoryTest {

    @Autowired
    private EntityManager manager;

    @Autowired
    private TestEntityManager testEntityManager;

    @Autowired
    private UserRepository userRepository;

    @Test
//    @DatabaseSetup(value = "/scripts/dbunit/users.xml", type = DatabaseOperation.INSERT)
    @Sql("/scripts/sql/seeding.sql")
    void test_not_null() {
//        User user = User.builder()
//                .role(UserRole.ATTENDEE)
//                .email("salma@gmail")
//                .firstName("salma")
//                .lastName("fayez")
//                .password("12345678")
//                .build();
//        manager.persist(user);
        assertThat(userRepository).isNotNull();
        assertThat(userRepository.findAll()).hasSize(1);
    }

    /*
    1. using entity manager or test entity manager
    2. library Dbunit
    3. Sql scripts
     */

}