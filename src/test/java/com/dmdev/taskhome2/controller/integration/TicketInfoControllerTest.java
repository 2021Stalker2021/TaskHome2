package com.dmdev.taskhome2.controller.integration;

import com.dmdev.taskhome2.dto.TicketInfoDto;
import com.dmdev.taskhome2.entity.TicketInfo;
import com.dmdev.taskhome2.repository.TicketInfoRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

import java.math.BigDecimal;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Testcontainers
@SpringBootTest
@AutoConfigureMockMvc
class TicketInfoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private TicketInfoRepository ticketInfoRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @Container
    private static final PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>(DockerImageName.parse("postgres:13"));

    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgres::getJdbcUrl);
        registry.add("spring.datasource.username", postgres::getUsername);
        registry.add("spring.datasource.password", postgres::getPassword);
        registry.add("spring.jpa.hibernate.ddl-auto", () -> "create-drop");
    }

    @BeforeEach
    void setUp() {
        ticketInfoRepository.deleteAll(); // Очистка базы данных перед каждым тестом
    }

    @Test
    void createTicketInfo_shouldReturnCreatedStatus() throws Exception {
        // Подготовка данных
        TicketInfoDto ticketInfoDto = TicketInfoDto.builder()
                .price(BigDecimal.valueOf(100.50))
                .currency("USD")
                .availability(true)
                .build();

        // Выполнение POST-запроса
        mockMvc.perform(post("/api/v1/ticketInfo")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(ticketInfoDto)))
                .andExpect(status().isCreated());
    }

    @Test
    void getTicketInfoById_shouldReturnTicketInfo() throws Exception {
        // Создание тестового объекта в базе данных
        TicketInfo ticketInfo = TicketInfo.builder()
                .price(BigDecimal.valueOf(200.75))
                .currency("EUR")
                .availability(true)
                .build();
        TicketInfo savedTicket = ticketInfoRepository.save(ticketInfo);

        // Выполнение GET-запроса
        mockMvc.perform(get("/api/v1/ticketInfo/{id}", savedTicket.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(savedTicket.getId().toString()))
                .andExpect(jsonPath("$.price").value(200.75))
                .andExpect(jsonPath("$.currency").value("EUR"))
                .andExpect(jsonPath("$.availability").value(true));
    }

    @Test
    void updateTicketInfo_shouldReturnUpdatedTicketInfo() throws Exception {
        // Создание тестового объекта в базе данных
        TicketInfo ticketInfo = TicketInfo.builder()
                .price(BigDecimal.valueOf(150.00))
                .currency("GBP")
                .availability(false)
                .build();
        TicketInfo savedTicket = ticketInfoRepository.save(ticketInfo);

        // Подготовка данных для обновления
        TicketInfoDto updatedTicketInfoDto = TicketInfoDto.builder()
                .price(BigDecimal.valueOf(300.00))
                .currency("USD")
                .availability(true)
                .build();

        // Выполнение PUT-запроса
        mockMvc.perform(put("/api/v1/ticketInfo/{id}", savedTicket.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updatedTicketInfoDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.price").value(300.00))
                .andExpect(jsonPath("$.currency").value("USD"))
                .andExpect(jsonPath("$.availability").value(true));
    }

    @Test
    void deleteTicketInfo_shouldReturnNoContent() throws Exception {
        // Создание тестового объекта в базе данных
        TicketInfo ticketInfo = TicketInfo.builder()
                .price(BigDecimal.valueOf(99.99))
                .currency("JPY")
                .availability(true)
                .build();
        TicketInfo savedTicket = ticketInfoRepository.save(ticketInfo);

        // Выполнение DELETE-запроса
        mockMvc.perform(delete("/api/v1/ticketInfo/{id}", savedTicket.getId()))
                .andExpect(status().isNoContent());
    }

    @Test
    void deleteAllTicketInfo_shouldReturnNoContent() throws Exception {
        // Создание тестовых объектов в базе данных
        ticketInfoRepository.save(TicketInfo.builder()
                .price(BigDecimal.valueOf(10.00))
                .currency("USD")
                .availability(true)
                .build());

        ticketInfoRepository.save(TicketInfo.builder()
                .price(BigDecimal.valueOf(20.00))
                .currency("EUR")
                .availability(false)
                .build());

        // Выполнение DELETE-запроса для удаления всех записей
        mockMvc.perform(delete("/api/v1/ticketInfo/delete/all"))
                .andExpect(status().isNoContent());
    }

    @Test
    void getAllTicketInfo_shouldReturnListOfTickets() throws Exception {
        // Создание тестовых объектов в базе данных
        ticketInfoRepository.save(TicketInfo.builder()
                .price(BigDecimal.valueOf(50.00))
                .currency("USD")
                .availability(true)
                .build());

        ticketInfoRepository.save(TicketInfo.builder()
                .price(BigDecimal.valueOf(75.00))
                .currency("EUR")
                .availability(false)
                .build());

        // Выполнение GET-запроса
        mockMvc.perform(get("/api/v1/ticketInfo"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2));
    }
}