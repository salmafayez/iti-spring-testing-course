package gov.iti.jets.testing.demo.persistence;

import static org.junit.jupiter.api.Assertions.assertEquals;


//@DataJpaTest(properties = {"spring.flyway.enabled=false",
//        "spring.jpa.hibernate.ddl-auto=create-drop",})
//class RegistrationRepositoryInMemoryTest {
//
//    @Autowired
//    private RegistrationRepository registrationRepository;
//
//    @Test
//    void getRegistrations(){
//        List<Registration> registrationByUserEmail =
//                registrationRepository.getRegistrationByUserEmailAndRegistrationDateAfter("ahmed.fayez@gmail.com",
//                        LocalDateTime.of(1999, 7, 3,4,4));
//        assertEquals(2, registrationByUserEmail.size());
//    }
//
//
//}