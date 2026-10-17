package com.dmdev.taskhome2.controller.integration;

import com.dmdev.taskhome2.dto.AddressDto;
import com.dmdev.taskhome2.dto.AttractionDto;
import com.dmdev.taskhome2.dto.ServiceDto;
import com.dmdev.taskhome2.dto.TicketInfoDto;
import com.dmdev.taskhome2.entity.AttractionType;
import com.dmdev.taskhome2.entity.ServiceType;
import com.dmdev.taskhome2.repository.AttractionRepository;
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
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@Testcontainers
@SpringBootTest
@AutoConfigureMockMvc
class AttractionControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private AttractionRepository attractionRepository;

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
        attractionRepository.deleteAll(); // Очистка базы данных перед каждым тестом
    }

    @Test
    void createAttraction_shouldReturnCreatedAttraction() throws Exception {
        // Создаем объект AttractionDto
        AttractionDto attractionDto = AttractionDto.builder()
                .name("Museum")
                .description("Historical museum")
                .attractionType(AttractionType.MUSEUM)
                .address(AddressDto.builder()
                        .city("Moscow")
                        .street("Lenina")
                        .building(10)
                        .region("Central")
                        .build())
                .ticketInfo(TicketInfoDto.builder()
                        .price(BigDecimal.valueOf(500))
                        .currency("RUB")
                        .availability(true)
                        .build())
                .services(List.of(
                        ServiceDto.builder()
                                .name("Audio guide")
                                .description("Audio guide in English")
                                .serviceType(ServiceType.AUDIO_GUIDE)
                                .build()
                ))
                .build();

        // Выполняем POST-запрос
        mockMvc.perform(post("/api/v1/attractions")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(attractionDto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.name").value("Museum"))
                .andExpect(jsonPath("$.description").value("Historical museum"))
                .andExpect(jsonPath("$.attractionType").value("MUSEUM"))
                .andExpect(jsonPath("$.address.city").value("Moscow"))
                .andExpect(jsonPath("$.ticketInfo.price").value(500))
                .andExpect(jsonPath("$.ticketInfo.currency").value("RUB"))
                .andExpect(jsonPath("$.ticketInfo.availability").value(true))
                .andExpect(jsonPath("$.services[0].name").value("Audio guide"))
                .andExpect(jsonPath("$.services[0].description").value("Audio guide in English"))
                .andExpect(jsonPath("$.services[0].serviceType").value("AUDIO_GUIDE"));
    }

    @Test
    void getAllAttractions_shouldReturnEmptyList() throws Exception {
        mockMvc.perform(get("/api/v1/attractions"))
                .andExpect(status().isOk())
                .andExpect(content().json("[]")); // Ожидаем пустой массив
    }

    @Test
    void findAttractionById_shouldReturnAttraction() throws Exception {
        // Создаем объект AttractionDto
        AttractionDto attractionDto = AttractionDto.builder()
                .name("Museum")
                .description("Historical museum")
                .attractionType(AttractionType.MUSEUM)
                .address(AddressDto.builder()
                        .city("Moscow")
                        .street("Lenina")
                        .building(10)
                        .region("Central")
                        .build())
                .ticketInfo(TicketInfoDto.builder()
                        .price(BigDecimal.valueOf(500))
                        .currency("RUB")
                        .availability(true)
                        .build())
                .services(List.of(
                        ServiceDto.builder()
                                .name("Audio guide")
                                .description("Audio guide in English")
                                .serviceType(ServiceType.AUDIO_GUIDE)
                                .build()
                ))
                .build();

        // Создаем достопремечательность
        String response = mockMvc.perform(post("/api/v1/attractions")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(attractionDto)))
                .andReturn()
                .getResponse()
                .getContentAsString();

        AttractionDto createdAttraction = objectMapper.readValue(response, AttractionDto.class);

        // Получаем аттракцион по ID
        mockMvc.perform(get("/api/v1/attractions/{id}", createdAttraction.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(createdAttraction.getId().toString()))
                .andExpect(jsonPath("$.name").value("Museum"))
                .andExpect(jsonPath("$.description").value("Historical museum"))
                .andExpect(jsonPath("$.attractionType").value("MUSEUM"))
                .andExpect(jsonPath("$.address.city").value("Moscow"))
                .andExpect(jsonPath("$.ticketInfo.price").value(500))
                .andExpect(jsonPath("$.ticketInfo.currency").value("RUB"))
                .andExpect(jsonPath("$.ticketInfo.availability").value(true))
                .andExpect(jsonPath("$.services[0].name").value("Audio guide"))
                .andExpect(jsonPath("$.services[0].description").value("Audio guide in English"))
                .andExpect(jsonPath("$.services[0].serviceType").value("AUDIO_GUIDE"));
    }

    @Test
    void findAttractionByServiceName_shouldReturnAttractions() throws Exception {
        // Создаем объект AttractionDto
        AttractionDto attractionDto = AttractionDto.builder()
                .name("Museum")
                .description("Historical museum")
                .attractionType(AttractionType.MUSEUM)
                .address(AddressDto.builder()
                        .city("Moscow")
                        .street("Lenina")
                        .building(10)
                        .region("Central")
                        .build())
                .ticketInfo(TicketInfoDto.builder()
                        .price(BigDecimal.valueOf(500))
                        .currency("RUB")
                        .availability(true)
                        .build())
                .services(List.of(
                        ServiceDto.builder()
                                .name("Audio guide")
                                .description("Audio guide in English")
                                .serviceType(ServiceType.AUDIO_GUIDE)
                                .build()
                ))
                .build();

        // Создаем достопремечательность
        mockMvc.perform(post("/api/v1/attractions")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(attractionDto)));

        // Ищем достопремечательность по имени сервиса
        mockMvc.perform(get("/api/v1/attractions/service/name")
                        .param("serviceName", "Audio guide"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("Museum"))
                .andExpect(jsonPath("$[0].services[0].name").value("Audio guide"))
                .andExpect(jsonPath("$[0].services[0].serviceType").value("AUDIO_GUIDE"));
    }

    @Test
    void findAttractionByAddressCity_shouldReturnAttractions() throws Exception {
        // Создаем объект AttractionDto
        AttractionDto attractionDto = AttractionDto.builder()
                .name("Museum")
                .description("Historical museum")
                .attractionType(AttractionType.MUSEUM)
                .address(AddressDto.builder()
                        .city("Moscow")
                        .street("Lenina")
                        .building(10)
                        .region("Central")
                        .build())
                .ticketInfo(TicketInfoDto.builder()
                        .price(BigDecimal.valueOf(500))
                        .currency("RUB")
                        .availability(true)
                        .build())
                .services(List.of(
                        ServiceDto.builder()
                                .name("Audio guide")
                                .description("Audio guide in English")
                                .serviceType(ServiceType.AUDIO_GUIDE)
                                .build()
                ))
                .build();

        // Создаем достопремечательность
        mockMvc.perform(post("/api/v1/attractions")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(attractionDto)));

        // Ищем достопремечательность по городу
        mockMvc.perform(get("/api/v1/attractions/address/city")
                        .param("city", "Moscow"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("Museum"))
                .andExpect(jsonPath("$[0].address.city").value("Moscow"));
    }

    @Test
    void findAttractionByTicketPrice_shouldReturnAttractions() throws Exception {
        // Создаем объект AttractionDto
        AttractionDto attractionDto = AttractionDto.builder()
                .name("Museum")
                .description("Historical museum")
                .attractionType(AttractionType.MUSEUM)
                .address(AddressDto.builder()
                        .city("Moscow")
                        .street("Lenina")
                        .building(10)
                        .region("Central")
                        .build())
                .ticketInfo(TicketInfoDto.builder()
                        .price(BigDecimal.valueOf(500))
                        .currency("RUB")
                        .availability(true)
                        .build())
                .services(List.of(
                        ServiceDto.builder()
                                .name("Audio guide")
                                .description("Audio guide in English")
                                .serviceType(ServiceType.AUDIO_GUIDE)
                                .build()
                ))
                .build();

        // Создаем достопремечательность
        mockMvc.perform(post("/api/v1/attractions")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(attractionDto)));

        // Ищем достопремечательность по цене билета
        mockMvc.perform(get("/api/v1/attractions/ticket/price")
                        .param("ticketPrice", "500"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("Museum"))
                .andExpect(jsonPath("$[0].ticketInfo.price").value(500));
    }

    @Test
    void updateAttraction_shouldReturnUpdatedAttraction() throws Exception {
        // Создаем объект AttractionDto
        AttractionDto attractionDto = AttractionDto.builder()
                .name("Museum")
                .description("Historical museum")
                .attractionType(AttractionType.MUSEUM)
                .address(AddressDto.builder()
                        .city("Moscow")
                        .street("Lenina")
                        .building(10)
                        .region("Central")
                        .build())
                .ticketInfo(TicketInfoDto.builder()
                        .price(BigDecimal.valueOf(500))
                        .currency("RUB")
                        .availability(true)
                        .build())
                .services(List.of(
                        ServiceDto.builder()
                                .name("Audio guide")
                                .description("Audio guide in English")
                                .serviceType(ServiceType.AUDIO_GUIDE)
                                .build()
                ))
                .build();

        // Создаем достопремечательность
        String response = mockMvc.perform(post("/api/v1/attractions")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(attractionDto)))
                .andReturn()
                .getResponse()
                .getContentAsString();

        AttractionDto createdAttraction = objectMapper.readValue(response, AttractionDto.class);

        // Обновляем достопремечательность
        AttractionDto updatedAttractionDto = AttractionDto.builder()
                .name("Updated Museum")
                .description("Updated Historical museum")
                .attractionType(AttractionType.MUSEUM)
                .address(AddressDto.builder()
                        .city("Moscow")
                        .street("Lenina")
                        .building(10)
                        .region("Central")
                        .build())
                .ticketInfo(TicketInfoDto.builder()
                        .price(BigDecimal.valueOf(600))
                        .currency("RUB")
                        .availability(true)
                        .build())
                .services(List.of(
                        ServiceDto.builder()
                                .name("Updated Audio guide")
                                .description("Updated Audio guide in English")
                                .serviceType(ServiceType.AUDIO_GUIDE)
                                .build()
                ))
                .build();

        mockMvc.perform(put("/api/v1/attractions/{id}", createdAttraction.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updatedAttractionDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Updated Museum"))
                .andExpect(jsonPath("$.description").value("Updated Historical museum"))
                .andExpect(jsonPath("$.ticketInfo.price").value(600))
                .andExpect(jsonPath("$.services[0].name").value("Updated Audio guide"));
    }

    @Test
    void deleteAttraction_shouldReturnNoContent() throws Exception {
        // Создаем объект AttractionDto
        AttractionDto attractionDto = AttractionDto.builder()
                .name("Museum")
                .description("Historical museum")
                .attractionType(AttractionType.MUSEUM)
                .address(AddressDto.builder()
                        .city("Moscow")
                        .street("Lenina")
                        .building(10)
                        .region("Central")
                        .build())
                .ticketInfo(TicketInfoDto.builder()
                        .price(BigDecimal.valueOf(500))
                        .currency("RUB")
                        .availability(true)
                        .build())
                .services(List.of(
                        ServiceDto.builder()
                                .name("Audio guide")
                                .description("Audio guide in English")
                                .serviceType(ServiceType.AUDIO_GUIDE)
                                .build()
                ))
                .build();

        // Создаем достопремечательность
        String response = mockMvc.perform(post("/api/v1/attractions")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(attractionDto)))
                .andReturn()
                .getResponse()
                .getContentAsString();

        AttractionDto createdAttraction = objectMapper.readValue(response, AttractionDto.class);

        // Удаляем аттракцион
        mockMvc.perform(delete("/api/v1/attractions/{id}", createdAttraction.getId()))
                .andExpect(status().isNoContent());
    }

    @Test
    void deleteAllAttractions_shouldReturnNoContent() throws Exception {
        // Создаем несколько достопремечательностей
        AttractionDto attractionDto1 = AttractionDto.builder()
                .name("Museum")
                .description("Historical museum")
                .attractionType(AttractionType.MUSEUM)
                .address(AddressDto.builder()
                        .city("Moscow")
                        .street("Lenina")
                        .building(10)
                        .region("Central")
                        .build())
                .ticketInfo(TicketInfoDto.builder()
                        .price(BigDecimal.valueOf(500))
                        .currency("RUB")
                        .availability(true)
                        .build())
                .services(List.of(
                        ServiceDto.builder()
                                .name("Audio guide")
                                .description("Audio guide in English")
                                .serviceType(ServiceType.AUDIO_GUIDE)
                                .build()
                ))
                .build();

        AttractionDto attractionDto2 = AttractionDto.builder()
                .name("Park")
                .description("Central park")
                .attractionType(AttractionType.PARK)
                .address(AddressDto.builder()
                        .city("Saint Petersburg")
                        .street("Nevsky Prospect")
                        .building(20)
                        .region("Northwestern")
                        .build())
                .ticketInfo(TicketInfoDto.builder()
                        .price(BigDecimal.valueOf(300))
                        .currency("RUB")
                        .availability(true)
                        .build())
                .services(List.of(
                        ServiceDto.builder()
                                .name("Boat rental")
                                .description("Boat rental service")
                                .serviceType(ServiceType.BOAT_RENTAL)
                                .build()
                ))
                .build();

        mockMvc.perform(post("/api/v1/attractions")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(attractionDto1)));

        mockMvc.perform(post("/api/v1/attractions")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(attractionDto2)));

        // Удаляем все достопремечательности
        mockMvc.perform(delete("/api/v1/attractions/delete/all"))
                .andExpect(status().isNoContent());

        // Проверяем, что достопремечательности удалены
        mockMvc.perform(get("/api/v1/attractions"))
                .andExpect(status().isOk())
                .andExpect(content().json("[]"));
    }
}