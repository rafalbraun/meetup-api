package org.example.dto;

import jakarta.persistence.Column;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.model.Meetup;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

@Data
@NoArgsConstructor
public class LocationDto {

    private Long id;
    private String city;
    private String address;
    private String name;
    private Double latitude;
    private Double longitude;
    private List<MeetupDto> meetups = new LinkedList<>();

}
