package com.dmdev.taskhome2.controller.unit;

import com.dmdev.taskhome2.dto.AttractionDto;
import com.dmdev.taskhome2.entity.Attraction;
import com.dmdev.taskhome2.entity.AttractionType;
import com.dmdev.taskhome2.exception.AttractionNotFoundException;
import com.dmdev.taskhome2.mappers.AttractionMapper;
import com.dmdev.taskhome2.repository.AttractionRepository;
import com.dmdev.taskhome2.service.AttractionService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AttractionServiceTest {

    @Mock
    private AttractionRepository attractionRepository;

    @Mock
    private AttractionMapper attractionMapper;

    @InjectMocks
    private AttractionService attractionService;

    @Test
    void create_shouldReturnCreatedAttractionDto() {
        // Arrange
        UUID id = UUID.randomUUID();
        AttractionDto attractionDto = AttractionDto.builder()
                .name("Eiffel Tower")
                .description("Iconic tower in Paris")
                .attractionType(AttractionType.LANDMARK)
                .build();

        Attraction attraction = Attraction.builder()
                .id(id)
                .name("Eiffel Tower")
                .description("Iconic tower in Paris")
                .type(AttractionType.LANDMARK)
                .build();

        when(attractionMapper.toEntity(attractionDto)).thenReturn(attraction);
        when(attractionRepository.save(attraction)).thenReturn(attraction);
        when(attractionMapper.toDto(attraction)).thenReturn(attractionDto);

        // Act
        AttractionDto result = attractionService.create(attractionDto);

        // Assert
        assertNotNull(result);
        assertEquals("Eiffel Tower", result.getName());
        assertEquals("Iconic tower in Paris", result.getDescription());
        assertEquals(AttractionType.LANDMARK, result.getAttractionType());

        verify(attractionRepository, times(1)).save(attraction);
        verify(attractionMapper, times(1)).toDto(attraction);
    }

    @Test
    void getAttractionById_shouldReturnAttractionDto() {
        // Arrange
        UUID id = UUID.randomUUID();
        Attraction attraction = Attraction.builder()
                .id(id)
                .name("Statue of Liberty")
                .description("Iconic statue in New York")
                .type(AttractionType.LANDMARK)
                .build();

        AttractionDto attractionDto = AttractionDto.builder()
                .id(id)
                .name("Statue of Liberty")
                .description("Iconic statue in New York")
                .attractionType(AttractionType.LANDMARK)
                .build();

        when(attractionRepository.findById(id)).thenReturn(Optional.of(attraction));
        when(attractionMapper.toDto(attraction)).thenReturn(attractionDto);

        // Act
        AttractionDto result = attractionService.getAttractionById(id);

        // Assert
        assertNotNull(result);
        assertEquals(id, result.getId());
        assertEquals("Statue of Liberty", result.getName());
        assertEquals("Iconic statue in New York", result.getDescription());
        assertEquals(AttractionType.LANDMARK, result.getAttractionType());

        verify(attractionRepository, times(1)).findById(id);
        verify(attractionMapper, times(1)).toDto(attraction);
    }

    @Test
    void getAttractionById_shouldThrowAttractionNotFoundException() {
        // Arrange
        UUID id = UUID.randomUUID();
        when(attractionRepository.findById(id)).thenReturn(Optional.empty());

        // Act & Assert
        AttractionNotFoundException exception = assertThrows(AttractionNotFoundException.class, () -> attractionService.getAttractionById(id));
        assertEquals("No attractions found by id", exception.getMessage());

        verify(attractionRepository, times(1)).findById(id);
        verify(attractionMapper, never()).toDto(any());
    }

    @Test
    void updateAttraction_shouldReturnUpdatedAttractionDto() {
        // Arrange
        UUID id = UUID.randomUUID();
        AttractionDto attractionDto = AttractionDto.builder()
                .name("Updated Name")
                .description("Updated Description")
                .attractionType(AttractionType.MUSEUM)
                .build();

        Attraction existingAttraction = Attraction.builder()
                .id(id)
                .name("Old Name")
                .description("Old Description")
                .type(AttractionType.LANDMARK)
                .build();

        Attraction updatedAttraction = Attraction.builder()
                .id(id)
                .name("Updated Name")
                .description("Updated Description")
                .type(AttractionType.MUSEUM)
                .build();

        when(attractionRepository.findById(id)).thenReturn(Optional.of(existingAttraction));
        when(attractionMapper.toEntity(attractionDto)).thenReturn(updatedAttraction);
        when(attractionRepository.save(updatedAttraction)).thenReturn(updatedAttraction);
        when(attractionMapper.toDto(updatedAttraction)).thenReturn(attractionDto);

        // Act
        AttractionDto result = attractionService.updateAttraction(id, attractionDto);

        // Assert
        assertNotNull(result);
        assertEquals("Updated Name", result.getName());
        assertEquals("Updated Description", result.getDescription());
        assertEquals(AttractionType.MUSEUM, result.getAttractionType());

        verify(attractionRepository, times(1)).findById(id);
        verify(attractionRepository, times(1)).save(updatedAttraction);
        verify(attractionMapper, times(1)).toDto(updatedAttraction);
    }

    @Test
    void deleteAttraction_shouldReturnTrueIfAttractionExists() {
        // Arrange
        UUID id = UUID.randomUUID();
        Attraction attraction = Attraction.builder()
                .id(id)
                .name("Eiffel Tower")
                .description("Iconic tower in Paris")
                .type(AttractionType.LANDMARK)
                .build();

        when(attractionRepository.findById(id)).thenReturn(Optional.of(attraction));

        // Act
        boolean result = attractionService.deleteAttraction(id);

        // Assert
        assertTrue(result);
        verify(attractionRepository, times(1)).findById(id);
        verify(attractionRepository, times(1)).delete(attraction);
    }

    @Test
    void deleteAttraction_shouldReturnFalseIfAttractionDoesNotExist() {
        // Arrange
        UUID id = UUID.randomUUID();
        when(attractionRepository.findById(id)).thenReturn(Optional.empty());

        // Act
        boolean result = attractionService.deleteAttraction(id);

        // Assert
        assertFalse(result);
        verify(attractionRepository, times(1)).findById(id);
        verify(attractionRepository, never()).delete(any());
    }

    @Test
    void deleteAllAttractions_shouldDeleteAllAttractions() {
        // Arrange
        doNothing().when(attractionRepository).deleteAll();

        // Act
        attractionService.deleteAllAttractions();

        // Assert
        verify(attractionRepository, times(1)).deleteAll();
    }
}
