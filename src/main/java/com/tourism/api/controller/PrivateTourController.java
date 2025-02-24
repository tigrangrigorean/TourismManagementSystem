package com.tourism.api.controller;

import com.tourism.model.domain.Tour;
import com.tourism.service.TourService;
import org.springframework.web.bind.annotation.RestController;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/private/tour")
public class PrivateTourController {

    private final TourService tourService;

    @Autowired
    public PrivateTourController(TourService tourService) {
        this.tourService = tourService;
    }

    @GetMapping("/{id}")
    private ResponseEntity<Tour> getById(@PathVariable Long id) {
        return ResponseEntity.ok(tourService.getById(id)
                .orElseThrow(() -> new RuntimeException("Tour not found with id: " + id)));
    }

    @GetMapping("/")
    private ResponseEntity<List<Tour>> getAll() {
        return ResponseEntity.ok(tourService.getAll());
    }

    @PostMapping("/save")
    private ResponseEntity<Tour> save(@RequestBody Tour Tour) {
        return ResponseEntity.status(201).body(tourService.save(Tour));
    }

    @PostMapping("/update")
    private ResponseEntity<Tour> update(@RequestBody Tour Tour) {
        return ResponseEntity.ok(tourService.update(Tour));
    }

    @DeleteMapping("/remove")
    private ResponseEntity<String> remove(@RequestParam Long id) {
        tourService.delete(id);
        return ResponseEntity.ok().body("Tour with id " + id + " removed successfully");
    }
}