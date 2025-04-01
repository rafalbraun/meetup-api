package org.example.service;

import org.example.dto.LocationDto;
import org.example.exceptions.ResourceNotFoundException;
import org.example.mapper.LocationMapper;
import org.example.model.Location;
import org.example.repository.LocationRepository;
import org.springframework.stereotype.Service;

import static org.example.exceptions.ExceptionMessages.LOCATION_NOT_FOUND;

@Service
public class LocationService {

    private final LocationRepository locationRepository;

    public LocationService(LocationRepository locationRepository) {
        this.locationRepository = locationRepository;
    }

    public Location getLocationById(Long id) throws ResourceNotFoundException {
        return locationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(LOCATION_NOT_FOUND /*+ id*/));
    }

    public LocationDto createLocation(LocationDto dto) {
        Location location = LocationMapper.toEntity(dto);
        location = locationRepository.save(location);
        return LocationMapper.toDto(location);
    }
}
