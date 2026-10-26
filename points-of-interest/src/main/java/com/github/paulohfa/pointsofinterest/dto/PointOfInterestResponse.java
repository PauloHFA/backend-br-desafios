package com.github.paulohfa.pointsofinterest.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.github.paulohfa.pointsofinterest.domain.model.PointOfInterest;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record PointOfInterestResponse(
        Long id,
        String name,
        Integer x,
        Integer y,
        Double distance
) {
    public static PointOfInterestResponse fromEntity(PointOfInterest poi) {
        return new PointOfInterestResponse(poi.getId(), poi.getName(), poi.getX(), poi.getY(), null);
    }

    public static PointOfInterestResponse fromEntityWithDistance(PointOfInterest poi, double distance) {
        // Arredonda para 2 casas decimais
        double rounded = Math.round(distance * 100.0) / 100.0;
        return new PointOfInterestResponse(poi.getId(), poi.getName(), poi.getX(), poi.getY(), rounded);
    }
}
