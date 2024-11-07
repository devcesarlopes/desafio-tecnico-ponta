package com.desafiotecnico.ponta.cesarlopes.Tarefas;

import com.desafiotecnico.ponta.cesarlopes.Token.LoginRequest;
import com.desafiotecnico.ponta.cesarlopes.Usuarios.User;
import com.desafiotecnico.ponta.cesarlopes.Usuarios.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.Date;
import java.util.UUID;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class TarefaControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private TarefaRepository tarefaRepository;

    @Autowired
    private UserRepository userRepository;

    private Integer tarefaId;
    private UUID user1Id;
    private UUID user2Id;
    private String tokenUser1;
    private String tokenUser2;

    @BeforeAll
    public void dropTables(@Autowired UserRepository userRepository, @Autowired TarefaRepository tarefaRepository) throws Exception {
        tarefaRepository.deleteAll();
        userRepository.deleteAll();

        User user1 = new User();
        user1.setName("Test User1");
        user1.setLogin("testuser1");
        user1.setPassword("@Test12341");
        userRepository.save(user1);

        User user2 = new User();
        user2.setName("Test User2");
        user2.setLogin("testuser2");
        user2.setPassword("@Test12342");
        userRepository.save(user2);

        System.out.println(user1);
        System.out.println(user2);

        tokenUser1 = loginAndGetToken("testuser1", "@Test12341");
        tokenUser2 = loginAndGetToken("testuser2", "@Test12342");
    }

    private String loginAndGetToken(String login, String password) throws Exception {
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setLogin(login);
        loginRequest.setPassword(password);

        System.out.println(loginRequest);

        String response = mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/usuarios/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        return objectMapper.readTree(response).get("token").asText();
    }

    @Test
    @Order(1)
    public void testCriarTarefa() throws Exception {
        Tarefa tarefa = new Tarefa();
        tarefa.setTitulo("New Task 1");
        tarefa.setDescricao("Description for new task 1");
        tarefa.setDataDeCriacao(new Date());
        tarefa.setStatus("pendente");

        String response = mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/tarefas")
                        .header("Authorization", tokenUser1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(tarefa)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.titulo").value("New Task 1"))
                .andExpect(jsonPath("$.descricao").value("Description for new task 1"))
                .andExpect(jsonPath("$.status").value("pendente"))
                .andReturn()
                .getResponse()
                .getContentAsString();

        tarefaId = objectMapper.readTree(response).get("id").asInt();
        System.out.println("Created Tarefa ID: " + tarefaId);
    }

    @Test
    @Order(2)
    public void testGetAllTarefas() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/tarefas")
                        .header("Authorization", tokenUser1)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1));
    }

    @Test
    @Order(3)
    public void testUpdateTarefa() throws Exception {
        Tarefa tarefa = new Tarefa();
        tarefa.setTitulo("Updated Task 1");
        tarefa.setDescricao("Updated description for task 1");
        tarefa.setStatus("em andamento");

        mockMvc.perform(MockMvcRequestBuilders.put("/api/v1/tarefas/" + tarefaId)
                        .header("Authorization", tokenUser1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(tarefa)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.titulo").value("Updated Task 1"))
                .andExpect(jsonPath("$.descricao").value("Updated description for task 1"))
                .andExpect(jsonPath("$.status").value("em andamento"));
    }

    @Test
    @Order(4)
    public void testDeleteTarefa() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete("/api/v1/tarefas/" + tarefaId)
                        .header("Authorization", tokenUser1)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string("Tarefa removida com sucesso"));
    }

    @Test
    @Order(5)
    public void criarTarefaComStatusInvalido() throws Exception {
        Tarefa tarefa = new Tarefa();
        tarefa.setTitulo("New Task 1");
        tarefa.setDescricao("Description for new task 1");
        tarefa.setDataDeCriacao(new Date());
        tarefa.setStatus("teste");

        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/tarefas")
                        .header("Authorization", tokenUser1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(tarefa)))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("O atributo \"status\" deve ter valor: \"pendente\", \"em andamento\" ou \"concluída\""));
    }

    @Test
    @Order(6)
    public void updateOuDeleteDeTarefaDeOutroUsuario() throws Exception {
        Tarefa tarefa = new Tarefa();
        tarefa.setTitulo("New Task 1");
        tarefa.setDescricao("Description for new task 1");
        tarefa.setDataDeCriacao(new Date());
        tarefa.setStatus("pendente");

        String response = mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/tarefas")
                        .header("Authorization", tokenUser1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(tarefa)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.titulo").value("New Task 1"))
                .andExpect(jsonPath("$.descricao").value("Description for new task 1"))
                .andExpect(jsonPath("$.status").value("pendente"))
                .andReturn()
                .getResponse()
                .getContentAsString();

        int tarefaId = objectMapper.readTree(response).get("id").asInt();
        System.out.println("Created Tarefa ID: " + tarefaId);

        mockMvc.perform(MockMvcRequestBuilders.put("/api/v1/tarefas/" + tarefaId)
                        .header("Authorization", tokenUser2)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(tarefa)))
                .andExpect(status().isForbidden());

        mockMvc.perform(MockMvcRequestBuilders.delete("/api/v1/tarefas/" + tarefaId)
                        .header("Authorization", tokenUser2)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(tarefa)))
                .andExpect(status().isForbidden());
    }
}