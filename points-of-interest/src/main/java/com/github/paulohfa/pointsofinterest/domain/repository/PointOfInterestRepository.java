package com.github.paulohfa.pointsofinterest.domain.repository;

import com.github.paulohfa.pointsofinterest.domain.model.PointOfInterest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PointOfInterestRepository extends JpaRepository<PointOfInterest, Long> {

    /**
     * Consulta os pontos de interesse que estao dentro do raio de alcance 'dmax'.
     * Utiliza o calculo da distancia Euclidiana no plano cartesiano:
     * (x_p - x_ref)^2 + (y_p - y_ref)^2 <= dmax^2
     */
    @Query("SELECT p FROM PointOfInterest p " +
           "WHERE ((p.x - :x) * (p.x - :x) + (p.y - :y) * (p.y - :y)) <= (:dmax * :dmax)")
    List<PointOfInterest> findNearby(
            @Param("x") int x,
            @Param("y") int y,
            @Param("dmax") int dmax
    );
}
