//package gov.iti.jets.testing.persistence;
//
//import com.github.springtestdbunit.DbUnitTestExecutionListener;
//import com.github.springtestdbunit.annotation.DatabaseOperation;
//import com.github.springtestdbunit.annotation.DatabaseSetup;
//import gov.iti.jets.testing.domain.User;
//import gov.iti.jets.testing.domain.enums.UserRole;
//import gov.iti.jets.testing.service.HelloService;
//import jakarta.persistence.EntityManager;
//import org.assertj.core.api.Assertions;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
//import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
//import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.test.context.DynamicPropertyRegistry;
//import org.springframework.test.context.DynamicPropertySource;
//import org.springframework.test.context.TestExecutionListeners;
//import org.springframework.test.context.jdbc.Sql;
//import org.testcontainers.containers.PostgreSQLContainer;
//import org.testcontainers.junit.jupiter.Container;
//import org.testcontainers.junit.jupiter.Testcontainers;
//
//@DataJpaTest(properties = {
////        "spring.flyway.enabled=false",
////        "spring.jpa.generate-ddl=true"
//})
//@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
//@Testcontainers
//@TestExecutionListeners(value = DbUnitTestExecutionListener.class,
//        mergeMode = TestExecutionListeners.MergeMode.MERGE_WITH_DEFAULTS)
//class UserRepositoryTest {
//
//    @Autowired
//    private UserRepository userRepository;
//
//    @Autowired
//    private TestEntityManager testEntityManager;
//
//    @Autowired
//    private EntityManager entityManager;
//
//    @Container
//    static PostgreSQLContainer container = new PostgreSQLContainer<>("postgres:latest")
//            .withDatabaseName("testing")
//            .withUsername("admin")
//            .withPassword("admin");
//
//    @DynamicPropertySource
//    static void properties(DynamicPropertyRegistry registry) {
//        registry.add("spring.datasource.url", container::getJdbcUrl);
//        registry.add("spring.datasource.password", container::getPassword);
//        registry.add("spring.datasource.username", container::getUsername);
//    }
//
//    @Test
//    void should_contains_one_user() {
//        //Act
//        User organizer = User.builder()
//                .firstName("salma")
//                .lastName("fayez")
//                .role(UserRole.ORGANIZER)
//                .email("salma.fayez@gmail.com")
//                .password("12345678")
//                .build();
//        userRepository.save(organizer);
//        entityManager.persist(organizer);
//        User user = userRepository.findById(1L).get();
//        System.out.println(user);
//    }
//
//    @Test
//    void should_be_empty() {
//        User organizer = User.builder()
//                .firstName("salma")
//                .lastName("fayez")
//                .role(UserRole.ORGANIZER)
//                .email("salma.fayez@gmail.com")
//                .password("12345678")
//                .build();
//        testEntityManager.persistFlushFind(organizer);
////        User user = userRepository.findById(1L).get();
////        System.out.println(user);
//    }
//}