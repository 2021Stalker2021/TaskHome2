package com.dmdev.taskhome2.controller;

import com.dmdev.taskhome2.dto.ServiceDto;
import com.dmdev.taskhome2.entity.ServiceType;
import com.dmdev.taskhome2.repository.ServiceRepository;
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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@Testcontainers
@SpringBootTest
@AutoConfigureMockMvc
class ServiceControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ServiceRepository serviceRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @Container
    private static final PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>(DockerImageName.parse("postgres:13"));

    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgres::getJdbcUrl);
        registry.add("spring.datasource.username", postgres::getUsername);
        registry.add("spring.datasource.password", postgres::getPassword);
        registry.add("spring.jpa.generate-ddl", () -> true);
    }

    @BeforeEach
    void setUp() {
        serviceRepository.deleteAll(); // Очищаем базу данных перед каждым тестом
    }

    @Test
    void getAllServices_shouldReturnEmptyList() throws Exception {
        mockMvc.perform(get("/api/v1/service"))
                .andExpect(status().isOk())
                .andExpect(content().json("[]")); // Ожидаем пустой массив
    }

    @Test
    void createService_shouldReturnCreatedService() throws Exception {
        // Создаем объект ServiceDto
        ServiceDto serviceDto = ServiceDto.builder()
                .name("Audio guide")
                .description("Audio guide in English")
                .serviceType(ServiceType.AUDIO_GUIDE)
                .build();

        // Выполняем POST-запрос
        String response = mockMvc.perform(post("/api/v1/service")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(serviceDto)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString();

        // Десериализуем ответ
        ServiceDto createdService = objectMapper.readValue(response, ServiceDto.class);

        // Проверяем, что сервис создан
        assertNotNull(createdService.getId());
        assertEquals("Audio guide", createdService.getName());
        assertEquals("Audio guide in English", createdService.getDescription());
        assertEquals(ServiceType.AUDIO_GUIDE, createdService.getServiceType());
    }

    @Test
    void findServiceById_shouldReturnService() throws Exception {
        // Создаем объект ServiceDto
        ServiceDto serviceDto = ServiceDto.builder()
                .name("Audio guide")
                .description("Audio guide in English")
                .serviceType(ServiceType.AUDIO_GUIDE)
                .build();

        // Создаем сервис
        String response = mockMvc.perform(post("/api/v1/service")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(serviceDto)))
                .andReturn()
                .getResponse()
                .getContentAsString();

        ServiceDto createdService = objectMapper.readValue(response, ServiceDto.class);

        // Получаем сервис по ID
        mockMvc.perform(get("/api/v1/service/{id}", createdService.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(createdService.getId().toString()))
                .andExpect(jsonPath("$.name").value("Audio guide"))
                .andExpect(jsonPath("$.description").value("Audio guide in English"))
                .andExpect(jsonPath("$.serviceType").value("AUDIO_GUIDE"));
    }

    @Test
    void updateService_shouldReturnUpdatedService() throws Exception {
        // Создаем объект ServiceDto
        ServiceDto serviceDto = ServiceDto.builder()
                .name("Audio guide")
                .description("Audio guide in English")
                .serviceType(ServiceType.AUDIO_GUIDE)
                .build();

        // Создаем сервис
        String response = mockMvc.perform(post("/api/v1/service")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(serviceDto)))
                .andReturn()
                .getResponse()
                .getContentAsString();

        ServiceDto createdService = objectMapper.readValue(response, ServiceDto.class);

        // Обновляем сервис
        ServiceDto updatedServiceDto = ServiceDto.builder()
                .name("Updated Audio guide")
                .description("Updated Audio guide in English")
                .serviceType(ServiceType.AUDIO_GUIDE)
                .build();

        mockMvc.perform(put("/api/v1/service/{id}", createdService.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updatedServiceDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Updated Audio guide"))
                .andExpect(jsonPath("$.description").value("Updated Audio guide in English"))
                .andExpect(jsonPath("$.serviceType").value("AUDIO_GUIDE"));
    }

    @Test
    void deleteService_shouldReturnNoContent() throws Exception {
        // Создаем объект ServiceDto
        ServiceDto serviceDto = ServiceDto.builder()
                .name("Audio guide")
                .description("Audio guide in English")
                .serviceType(ServiceType.AUDIO_GUIDE)
                .build();

        // Создаем сервис
        String response = mockMvc.perform(post("/api/v1/service")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(serviceDto)))
                .andReturn()
                .getResponse()
                .getContentAsString();

        ServiceDto createdService = objectMapper.readValue(response, ServiceDto.class);

        // Удаляем сервис
        mockMvc.perform(delete("/api/v1/service/{id}", createdService.getId()))
                .andExpect(status().isNoContent());
    }

    @Test
    void deleteAllServices_shouldReturnNoContent() throws Exception {
        // Создаем несколько сервисов
        ServiceDto serviceDto1 = ServiceDto.builder()
                .name("Audio guide")
                .description("Audio guide in English")
                .serviceType(ServiceType.AUDIO_GUIDE)
                .build();

        ServiceDto serviceDto2 = ServiceDto.builder()
                .name("Boat rental")
                .description("Boat rental service")
                .serviceType(ServiceType.BOAT_RENTAL)
                .build();

        mockMvc.perform(post("/api/v1/service")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(serviceDto1)));

        mockMvc.perform(post("/api/v1/service")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(serviceDto2)));

        // Удаляем все сервисы
        mockMvc.perform(delete("/api/v1/service/delete/all"))
                .andExpect(status().isNoContent());

        // Проверяем, что сервисы удалены
        mockMvc.perform(get("/api/v1/service"))
                .andExpect(status().isOk())
                .andExpect(content().json("[]"));
    }
}