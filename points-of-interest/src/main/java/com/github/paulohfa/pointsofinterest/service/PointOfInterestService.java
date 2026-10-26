package com.github.paulohfa.pointsofinterest.service;

import com.github.paulohfa.pointsofinterest.domain.model.PointOfInterest;
import com.github.paulohfa.pointsofinterest.domain.repository.PointOfInterestRepository;
import com.github.paulohfa.pointsofinterest.dto.PointOfInterestRequest;
import com.github.paulohfa.pointsofinterest.dto.PointOfInterestResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Comparator;
import java.util.List;

@Service
public class PointOfInterestService {

    private final PointOfInterestRepository repository;

    public PointOfInterestService(PointOfInterestRepository repository) {
        this.repository = repository;
    }

    @Transactional
    public PointOfInterestResponse create(PointOfInterestRequest request) {
        PointOfInterest poi = new PointOfInterest(request.name(), request.x(), request.y());
        PointOfInterest saved = repository.save(poi);
        return PointOfInterestResponse.fromEntity(saved);
    }

    @Transactional(readOnly = true)
    public Page<PointOfInterestResponse> findAll(Pageable pageable) {
        return repository.findAll(pageable).map(PointOfInterestResponse::fromEntity);
    }

    @Transactional(readOnly = true)
    public List<PointOfInterestResponse> findNearby(int refX, int refY, int dmax) {
        List<PointOfInterest> rawResults = repository.findNearby(refX, refY, dmax);

        return rawResults.stream()
                .map(poi -> {
                    double distance = calculateEuclideanDistance(refX, refY, poi.getX(), poi.getY());
                    return PointOfInterestResponse.fromEntityWithDistance(poi, distance);
                })
                .sorted(Comparator.comparing(PointOfInterestResponse::distance))
                .toList();
    }

    public static double calculateEuclideanDistance(int x1, int y1, int x2, int y2) {
        int dx = x1 - x2;
        int dy = y1 - y2;
        return Math.sqrt((dx * dx) + (dy * dy));
    }
}
