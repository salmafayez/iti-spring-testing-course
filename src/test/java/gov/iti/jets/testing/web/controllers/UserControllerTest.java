package gov.iti.jets.testing.web.controllers;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import gov.iti.jets.testing.config.WebSecurityConfig;
import gov.iti.jets.testing.domain.User;
import gov.iti.jets.testing.service.UserService;
import org.assertj.core.api.Assertions;
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
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;

@WebMvcTest(UserController.class)
@Import(WebSecurityConfig.class)
class UserControllerTest {

    @MockBean
    private UserService userService;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserController userController;

    @Autowired
    private ObjectMapper objectMapper;

    @Value("classpath:/image.png")
    private Resource image;

    @Test
    void get_all_users() throws Exception {
        //Arrange
        when(userService.getAll()).thenReturn(List.of(new User(), new User()));

        //Act
        MvcResult mvcResult = this.mockMvc.perform(MockMvcRequestBuilders.get("/users")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();

        //Assert
        List<User> users = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), new TypeReference<>() {});
        assertThat(users.size()).isEqualTo(2);
    }

    @Test
    void create_user() throws Exception {
        //Arrange
        when(userService.addUser(any())).thenReturn(new User());

        User user = new User();

        //Act
        MvcResult mvcResult = this.mockMvc.perform(MockMvcRequestBuilders.post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(user))
                )
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andReturn();

        System.out.println(mvcResult.getResponse().getContentAsString());
        //Assert
//        String response = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), new TypeReference<>() {});
//        assertThat(response).isEqualTo("added successfully");
    }

    @Test
    @WithMockUser
    void delete_user() throws Exception {
        //Arrange
        doNothing().when(userService).deleteUser(any());

        //Act
        MvcResult mvcResult = this.mockMvc.perform(MockMvcRequestBuilders.delete("/private/users/{id}", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();
    }

    @Test
    void multi_part_request() throws Exception {
        //Arrange
        doNothing().when(userService).addUserPhoto(any(), any());

        MockMultipartFile file = new MockMultipartFile("file", image.getInputStream());

        //Act
        MvcResult mvcResult = this.mockMvc.perform(MockMvcRequestBuilders.multipart("/users/{id}/photo", 1)
                        .file(file)
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();
    }

}