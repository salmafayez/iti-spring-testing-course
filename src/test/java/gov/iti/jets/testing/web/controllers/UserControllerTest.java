package gov.iti.jets.testing.web.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import gov.iti.jets.testing.config.WebSecurityConfig;
import gov.iti.jets.testing.domain.User;
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

import java.io.UnsupportedEncodingException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(UserController.class)
@Import(WebSecurityConfig.class)
class UserControllerTest {

    @Value("classpath:/image.png")
    private Resource image;

    @MockBean
    private UserService userService;

//    @MockBean
//    private Regi userService;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MockMvc mvc;


    //simple get request
    @Test
    void get_users() throws Exception {
        mvc.perform(MockMvcRequestBuilders.get("/users")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    //get request and parse the result returned
    @Test
    void get_user_by_id() throws Exception {

        when(userService.getById(any())).thenReturn(new User());

        MvcResult result = mvc.perform(MockMvcRequestBuilders.get("/users/{id}", 1L))
                .andExpect(status().isOk())
                .andReturn();

        User user = readJson(result);

        assertThat(user).isNotNull();
    }

    private User readJson(MvcResult result) throws JsonProcessingException, UnsupportedEncodingException {
        return objectMapper.readValue(result.getResponse().getContentAsString(),
                new TypeReference<>() {
                });
    }

    //post request and send body as json
    @Test
    void add_user() throws Exception {

        User user = new User();
        when(userService.addUser(any())).thenReturn(User.builder().id(1L).build());

        MvcResult result = mvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(user)))
                .andDo(print())
                .andExpect(status().isCreated())
                .andReturn();
    }

    @Test
    void add_user_photo() throws Exception {
        doNothing().when(userService).addUserPhoto(any(), any());

        MockMultipartFile file = new MockMultipartFile("image", image.getInputStream());
        MockPart part = new MockPart("dot", null);

        MvcResult result = mvc.perform(multipart("/users/{id}/photo", 1L)
                        .file(file)
                        .part(part))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();
    }

    @Test
    void test_authenticated_endpoint2() throws Exception {

        User user = new User();
        doNothing().when(userService).deleteUser(any());

        MvcResult result = mvc.perform(delete("/private/users/{id}",
                        1L)
                        .with(user(User.builder().build()))
                )
                .andExpect(status().isOk())
                .andReturn();
    }

    @Test
    @WithMockUser(value = "test", password = "test")
    void test_authenticated_endpoint() throws Exception {

        User user = new User();
        doNothing().when(userService).deleteUser(any());

        MvcResult result = mvc.perform(delete("/private/users/{id}",
                        1L))
                .andExpect(status().isOk())
                .andReturn();
    }


    /*
        1. perform a request for an endpoint that takes a file
        2. perform a request for an authenticates endpoint
        3. implement a simulation for the media server
     */

}