package gov.iti.jets.testing.demo.persistence;

import com.github.springtestdbunit.DbUnitTestExecutionListener;
import com.github.springtestdbunit.annotation.DatabaseOperation;
import com.github.springtestdbunit.annotation.DatabaseSetup;
import gov.iti.jets.testing.persistence.UserRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.jdbc.Sql;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;


@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Testcontainers
@TestExecutionListeners(value = DbUnitTestExecutionListener.class,
        mergeMode = TestExecutionListeners.MergeMode.MERGE_WITH_DEFAULTS)
class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Container
    static PostgreSQLContainer container = new PostgreSQLContainer<>("postgres:12.3")
            .withDatabaseName("test")
            .withUsername("duke")
            .withPassword("secret");

    @DynamicPropertySource
    static void properties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", container::getJdbcUrl);
        registry.add("spring.datasource.password", container::getPassword);
        registry.add("spring.datasource.username", container::getUsername);
    }

    @Test
    @DatabaseSetup(value = "/scripts/dbunit/users.xml", type = DatabaseOperation.INSERT)
    void test_get_user() {
        long count = userRepository.count();
        Assertions.assertThat(count).isEqualTo(1);
    }

    @Test
    @Sql(value = "/scripts/sql/seeding.sql")
    void test_get_user2() {
        long count = userRepository.count();
        Assertions.assertThat(count).isEqualTo(1);
    }

}