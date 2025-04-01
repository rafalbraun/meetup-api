package org.example.mapper;

import org.example.dto.LocationDto;
import org.example.model.Location;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
public class LocationMapper {
    public static LocationDto toDto(Location location) {
        if (location == null) {
            return null;
        }
        LocationDto dto = new LocationDto();
        dto.setId(location.getId());
        dto.setAddress(location.getAddress());
        dto.setCity(location.getCity());
        return dto;
    }

    public static Location toEntity(LocationDto dto) {
        if (dto == null) {
            return null;
        }
        Location location = new Location();
        location.setId(dto.getId());
        location.setAddress(dto.getAddress());
        location.setCity(dto.getCity());
        return location;
    }
}