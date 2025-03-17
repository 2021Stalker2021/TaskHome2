package com.dmdev.taskhome2.controller.integration;

import com.dmdev.taskhome2.dto.AddressDto;
import com.dmdev.taskhome2.repository.AddressRepository;
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

import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@Testcontainers
@SpringBootTest
@AutoConfigureMockMvc
class AddressControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private AddressRepository addressRepository;

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
        addressRepository.deleteAll(); // Очистка базы данных перед каждым тестом
    }

    @Test
    void getAllAddresses_shouldReturnEmptyList() throws Exception {
        mockMvc.perform(get("/api/v1/addresses"))
                .andExpect(status().isOk())
                .andExpect(content().json("[]")); // Должен быть пустой массив
    }

    @Test
    void createAddress_shouldReturnCreatedAddress() throws Exception {
        AddressDto addressDto = AddressDto.builder()
                .building(10)
                .street("Lenina")
                .city("Moscow")
                .region("Central")
                .build();

        mockMvc.perform(post("/api/v1/addresses")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(addressDto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.building").value(10))
                .andExpect(jsonPath("$.street").value("Lenina"))
                .andExpect(jsonPath("$.city").value("Moscow"))
                .andExpect(jsonPath("$.region").value("Central"));
    }

    @Test
    void getAddressById_shouldReturnAddress() throws Exception {
        // Создание адреса
        AddressDto addressDto = AddressDto.builder()
                .building(10)
                .street("Lenina")
                .city("Moscow")
                .region("Central")
                .build();

        String response = mockMvc.perform(post("/api/v1/addresses")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(addressDto)))
                .andReturn()
                .getResponse()
                .getContentAsString();

        AddressDto createdAddress = objectMapper.readValue(response, AddressDto.class);

        // Получение адреса по ID
        mockMvc.perform(get("/api/v1/addresses/{id}", createdAddress.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(createdAddress.getId().toString()))
                .andExpect(jsonPath("$.building").value(10))
                .andExpect(jsonPath("$.street").value("Lenina"))
                .andExpect(jsonPath("$.city").value("Moscow"))
                .andExpect(jsonPath("$.region").value("Central"));
    }

    @Test
    void updateAddress_shouldReturnUpdatedAddress() throws Exception {
        // Создание адреса
        AddressDto addressDto = AddressDto.builder()
                .building(10)
                .street("Lenina")
                .city("Moscow")
                .region("Central")
                .build();

        String response = mockMvc.perform(post("/api/v1/addresses")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(addressDto)))
                .andReturn()
                .getResponse()
                .getContentAsString();

        AddressDto createdAddress = objectMapper.readValue(response, AddressDto.class);

        // Обновление адреса
        AddressDto updatedAddressDto = AddressDto.builder()
                .building(20)
                .street("Nevsky Prospect")
                .city("Saint Petersburg")
                .region("Northwestern")
                .build();

        mockMvc.perform(put("/api/v1/addresses/{id}", createdAddress.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updatedAddressDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.building").value(20))
                .andExpect(jsonPath("$.street").value("Nevsky Prospect"))
                .andExpect(jsonPath("$.city").value("Saint Petersburg"))
                .andExpect(jsonPath("$.region").value("Northwestern"));
    }

    @Test
    void deleteAddress_shouldReturnNoContent() throws Exception {
        // Создание адреса
        AddressDto addressDto = AddressDto.builder()
                .building(10)
                .street("Lenina")
                .city("Moscow")
                .region("Central")
                .build();

        String response = mockMvc.perform(post("/api/v1/addresses")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(addressDto)))
                .andReturn()
                .getResponse()
                .getContentAsString();

        AddressDto createdAddress = objectMapper.readValue(response, AddressDto.class);

        // Удаление адреса
        mockMvc.perform(delete("/api/v1/addresses/{id}", createdAddress.getId()))
                .andExpect(status().isNoContent()); // Проверка на: 204 No Content
    }

    @Test
    void deleteAddress_shouldReturnNotFound() throws Exception {
        // Попытка удалить несуществующий адрес
        UUID nonExistentId = UUID.randomUUID();

        mockMvc.perform(delete("/api/v1/addresses/{id}", nonExistentId))
                .andExpect(status().isNotFound()); // Проверка на: 404 Not Found
    }

    @Test
    void deleteAllAddresses_shouldReturnNoContent() throws Exception {
        // Создание нескольких адресов
        AddressDto addressDto1 = AddressDto.builder()
                .building(10)
                .street("Lenina")
                .city("Moscow")
                .region("Central")
                .build();

        AddressDto addressDto2 = AddressDto.builder()
                .building(20)
                .street("Nevsky Prospect")
                .city("Saint Petersburg")
                .region("Northwestern")
                .build();

        mockMvc.perform(post("/api/v1/addresses")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(addressDto1)));

        mockMvc.perform(post("/api/v1/addresses")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(addressDto2)));

        // Удаляем все адреса
        mockMvc.perform(delete("/api/v1/addresses/delete/all"))
                .andExpect(status().isNoContent());

        // Проверяем, что адресов больше нет
        mockMvc.perform(get("/api/v1/addresses"))
                .andExpect(status().isOk())
                .andExpect(content().json("[]"));
    }
}
