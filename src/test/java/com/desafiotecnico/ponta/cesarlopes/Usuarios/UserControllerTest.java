package com.desafiotecnico.ponta.cesarlopes.Usuarios;

import com.desafiotecnico.ponta.cesarlopes.Tarefas.TarefaRepository;
import com.desafiotecnico.ponta.cesarlopes.Token.LoginRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import java.util.UUID;

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.hamcrest.Matchers.not;

@SpringBootTest
@AutoConfigureMockMvc
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private TarefaRepository tarefaRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private static UUID testUserId;

    @BeforeAll
    public static void dropTables(@Autowired UserRepository userRepository, @Autowired TarefaRepository tarefaRepository) {
        tarefaRepository.deleteAll();
        userRepository.deleteAll();

        User initialUser = new User();
        initialUser.setName("Initial User");
        initialUser.setLogin("initialuser");
        initialUser.setPassword("@Test1234");
        testUserId = userRepository.save(initialUser).getId();
    }

    @Test
    @Order(1)
    public void testCreateUsers() throws Exception {
        User user1 = new User();
        user1.setName("Test User1");
        user1.setLogin("test1");
        user1.setPassword("@Test12341");

        User user2 = new User();
        user2.setName("Test User2");
        user2.setLogin("test2");
        user2.setPassword("@Test12342");


        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/usuarios")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(user1)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Test User1"))
                .andExpect(jsonPath("$.login").value("test1"))
                .andExpect(jsonPath("$.password").value("@Test12341"));

        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/usuarios")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(user2)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Test User2"))
                .andExpect(jsonPath("$.login").value("test2"))
                .andExpect(jsonPath("$.password").value("@Test12342"));
    }

    @Test
    @Order(2)
    public void testGetAllUsers() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/usuarios")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(3));
    }

    @Test
    @Order(3)
    public void testGetUserById() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/usuarios/" + testUserId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Initial User"))
                .andExpect(jsonPath("$.login").value("initialuser"));
    }

    @Test
    @Order(4)
    public void testUpdateUser() throws Exception {
        User updatedUser = new User();
        updatedUser.setName("Updated User");
        updatedUser.setLogin("updateduser");
        updatedUser.setPassword("@Updated1234");

        mockMvc.perform(MockMvcRequestBuilders.put("/api/v1/usuarios/" + testUserId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updatedUser)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Updated User"))
                .andExpect(jsonPath("$.login").value("updateduser"))
                .andExpect(jsonPath("$.password").value("@Updated1234"));
    }

    @Test
    @Order(5)
    public void testDeleteUser() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete("/api/v1/usuarios/" + testUserId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string("User with id " + testUserId + " was successfully deleted"));
    }

    @Test
    @Order(6)
    public void testLoginUser() throws Exception {
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setLogin("test1");
        loginRequest.setPassword("@Test12341");

        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/usuarios/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").exists());
    }
}