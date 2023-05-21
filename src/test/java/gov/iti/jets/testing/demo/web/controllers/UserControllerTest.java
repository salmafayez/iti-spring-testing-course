package gov.iti.jets.testing.demo.web.controllers;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import gov.iti.jets.testing.config.WebSecurityConfig;
import gov.iti.jets.testing.domain.User;
import gov.iti.jets.testing.service.RegistrationService;
import gov.iti.jets.testing.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.mock.web.MockPart;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

@WebMvcTest
@Import(WebSecurityConfig.class)
class UserControllerTest {

    @Value("classpath:/image.png")
    private Resource image;

    @Autowired
    private MockMvc mvc;

    @MockBean
    private UserService userService;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private RegistrationService registrationService;

    @Test
    void get_all_users_successfully() throws Exception {
        //Arrange
        when(userService.getAll()).thenReturn(List.of(new User(), new User()));

        //Act
        mvc.perform(MockMvcRequestBuilders
                .get("/users"))
                .andExpect(MockMvcResultMatchers.status().isOk());

    }

    @Test
    void get_user_by_id_successfully() throws Exception {

        //Arrange
        when(userService.getById(any())).thenReturn(new User());

        //Act and assert
        mvc.perform(MockMvcRequestBuilders
                        .get("/users/{id}", 1))
                .andExpect(MockMvcResultMatchers.status().isOk());

    }

    @Test
    void add_user_successfully() throws Exception {

        //Arrange
        when(userService.addUser(any()))
                .thenReturn(User.builder().id(1L).build());

        User user = new User();

        //Act and assert
        mvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(user)))
                .andExpect(MockMvcResultMatchers.status().isCreated());

    }

    @Test
    void add_user_photo_successfully() throws Exception {

        //Arrange
        doNothing().when(userService).addUserPhoto(any(), any());

        MockMultipartFile mockFile = new MockMultipartFile("file", image.getInputStream());
        MockPart part = new MockPart("id",null);


        //Act and assert
        mvc.perform(multipart("/users/{id}/photo", 1L)
                        .file(mockFile)
                        .part(part)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk());

    }

    @Test
    void get_user_by_id_2_successfully() throws Exception {

        //Arrange
        when(userService.getById(any())).thenReturn(User.builder().firstName("salma").build());

        //Act and assert
        MvcResult result = mvc.perform(MockMvcRequestBuilders
                        .get("/users/{id}", 1))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();

        User user = objectMapper.readValue(result.getResponse()
                .getContentAsString(), new TypeReference<>() {});

        assertEquals("salma", user.getFirstName());
    }

    @Test
    @WithMockUser(username = "salma", password = "12345678")
    void delete_user_successfully() throws Exception {


        //Arrange
        doNothing().when(userService).deleteUser(any());

        //Act and assert
        MvcResult result = mvc.perform(delete("/private/users/{id}", 1))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();

    }

    @Test
    void delete_user_successfully2() throws Exception {

        //Arrange
        doNothing().when(userService).deleteUser(any());

        //Act and assert
        MvcResult result = mvc.perform(delete("/private/users/{id}", 1)
                        .with(user(User.builder().email("salma")
                                .password("1233").build())))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();

    }
}

/*
    1. Explore the annotation
    2. autowire the controller and check that it isn't null
    3. Introduce the MockBean annotation to autowire the service layer
    4. Check the NotNull again
    5. set up the mock
    6. perform a simple get request and check result is ok
    7. explore the different methods available like the content type
    8. perform a request for a get endpoint that accepts path variable or request param
    9. perform a request for a post endpoint that takes a body
    10. autowire the object mapper to be used to serialize and deserialize
    11. introduce the doNothing method of mockito
    12. perform a request for a post endpoint that accepts file and introduce the multipart
    13. introduce the andDo(print()) --> from the MockMvcResultHandlers
    14. Return the results and parse the response
    15. perform a request for an authenticated endpoint

 */
/*
    1. mockMvc.perform(post("/hotels/{id}", 42).accept(MediaType.APPLICATION_JSON));
    2. mockMvc.perform(get("/hotels?thing={thing}", "somewhere"));
    3. andExpect, andExpectAll
    4.
 */
