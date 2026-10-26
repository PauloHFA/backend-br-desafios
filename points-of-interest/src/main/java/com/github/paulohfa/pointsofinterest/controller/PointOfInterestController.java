package com.github.paulohfa.pointsofinterest.controller;

import com.github.paulohfa.pointsofinterest.dto.PointOfInterestRequest;
import com.github.paulohfa.pointsofinterest.dto.PointOfInterestResponse;
import com.github.paulohfa.pointsofinterest.service.PointOfInterestService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/points-of-interest")
public class PointOfInterestController {

    private final PointOfInterestService service;

    public PointOfInterestController(PointOfInterestService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<PointOfInterestResponse> create(@RequestBody @Valid PointOfInterestRequest request) {
        PointOfInterestResponse response = service.create(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping
    public ResponseEntity<Page<PointOfInterestResponse>> findAll(@PageableDefault(size = 20) Pageable pageable) {
        return ResponseEntity.ok(service.findAll(pageable));
    }

    @GetMapping("/nearby")
    public ResponseEntity<List<PointOfInterestResponse>> findNearby(
            @RequestParam int x,
            @RequestParam int y,
            @RequestParam int dmax
    ) {
        if (dmax < 0) {
            throw new IllegalArgumentException("A distancia maxima (dmax) nao pode ser negativa.");
        }
        return ResponseEntity.ok(service.findNearby(x, y, dmax));
    }
}
