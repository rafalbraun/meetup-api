package org.example.controllers;

import org.example.dto.LocationDto;
import org.example.mapper.LocationMapper;
import org.example.repository.LocationRepository;
import org.example.service.LocationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

import static org.example.Constants.GET_LOCATION;
import static org.example.Constants.GET_LOCATIONS;

@RestController
public class LocationController {

    private final LocationRepository locationRepository;
    private final LocationService locationService;

    public LocationController(LocationRepository locationRepository, LocationService locationService) {
        this.locationRepository = locationRepository;
        this.locationService = locationService;
    }

    @GetMapping(GET_LOCATIONS)
    public ResponseEntity<List<LocationDto>> getLocations() {
        List<LocationDto> locations = locationRepository.findAll().stream().map(LocationMapper::toDto).collect(Collectors.toList());
        return ResponseEntity.ok(locations);
    }

    @PostMapping(GET_LOCATION)
    public ResponseEntity<LocationDto> createLocation(@RequestBody LocationDto request) {
        LocationDto saved = locationService.createLocation(request);
        URI locationUri = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(saved.getId())
                .toUri();
        return ResponseEntity.created(locationUri).body(saved);
    }

    //------------------------------------------------------

}
