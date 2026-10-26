package com.github.paulohfa.pointsofinterest.config;

import com.github.paulohfa.pointsofinterest.domain.model.PointOfInterest;
import com.github.paulohfa.pointsofinterest.domain.repository.PointOfInterestRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class DataInitializer {

    @Bean
    public CommandLineRunner initDatabase(PointOfInterestRepository repository) {
        return args -> {
            if (repository.count() == 0) {
                repository.saveAll(List.of(
                        new PointOfInterest("Lanchonete", 27, 12),
                        new PointOfInterest("Posto", 31, 18),
                        new PointOfInterest("Joalheria", 15, 12),
                        new PointOfInterest("Floricultura", 19, 21),
                        new PointOfInterest("Pub", 12, 8),
                        new PointOfInterest("Supermercado", 23, 6),
                        new PointOfInterest("Churrascaria", 28, 2)
                ));
            }
        };
    }
}
