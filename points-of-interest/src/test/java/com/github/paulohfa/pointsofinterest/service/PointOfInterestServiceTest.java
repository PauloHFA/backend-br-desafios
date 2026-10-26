package com.github.paulohfa.pointsofinterest.service;

import com.github.paulohfa.pointsofinterest.domain.model.PointOfInterest;
import com.github.paulohfa.pointsofinterest.domain.repository.PointOfInterestRepository;
import com.github.paulohfa.pointsofinterest.dto.PointOfInterestResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

class PointOfInterestServiceTest {

    private PointOfInterestRepository repository;
    private PointOfInterestService service;

    @BeforeEach
    void setUp() {
        repository = Mockito.mock(PointOfInterestRepository.class);
        service = new PointOfInterestService(repository);
    }

    @Test
    @DisplayName("Deve calcular a distancia Euclidiana corretamente")
    void shouldCalculateEuclideanDistanceCorrectly() {
        // Ponto 1: (0, 0), Ponto 2: (3, 4) -> Triangulo retangulo 3-4-5
        double distance = PointOfInterestService.calculateEuclideanDistance(0, 0, 3, 4);
        assertEquals(5.0, distance, 0.0001);
    }

    @Test
    @DisplayName("Deve retornar pontos proximos ordenados por menor distancia")
    void shouldReturnNearbySortedByDistance() {
        PointOfInterest p1 = new PointOfInterest(1L, "Ponto A", 20, 15); // dist = 5
        PointOfInterest p2 = new PointOfInterest(2L, "Ponto B", 20, 12); // dist = 2

        when(repository.findNearby(20, 10, 10)).thenReturn(List.of(p1, p2));

        List<PointOfInterestResponse> results = service.findNearby(20, 10, 10);

        assertEquals(2, results.size());
        assertEquals("Ponto B", results.get(0).name());
        assertEquals(2.0, results.get(0).distance());
        assertEquals("Ponto A", results.get(1).name());
        assertEquals(5.0, results.get(1).distance());
    }
}
