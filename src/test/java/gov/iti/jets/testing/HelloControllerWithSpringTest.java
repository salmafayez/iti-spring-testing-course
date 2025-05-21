package gov.iti.jets.testing;

import gov.iti.jets.testing.config.TestContainersConfig;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@AutoConfigureMockMvc
//@Import(WebSecurityConfig.class)
@Transactional
@Import(value= {TestContainersConfig.class, })
class HelloControllerWithSpringTest {

    @Autowired
    private MockMvc mvc;

    @Test
    void test_not_null() throws Exception {
        mvc.perform(MockMvcRequestBuilders.get("/hello"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

}