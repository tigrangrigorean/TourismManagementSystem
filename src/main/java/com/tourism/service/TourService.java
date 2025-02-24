package com.tourism.service;

import com.tourism.api.security.SecurityConfig;
import com.tourism.model.domain.Tour;
import com.tourism.repository.TourRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
public class TourService {

    private final TourRepository tourRepository;

    @Autowired
    public TourService(TourRepository tourRepository) {
        this.tourRepository = tourRepository;
    }


    public Tour save(Tour tour) {
        return tourRepository.save(tour);
    }


    public Optional<Tour> getById(Long id) {
        return tourRepository.findById(id);
    }

    public List<Tour> getAll() {
        return tourRepository.findAll();
    }

    public Tour update(Tour Tour) {
        return tourRepository.save(Tour);
    }

    public void delete(Long id) {
        tourRepository.deleteById(id);
    }

}
