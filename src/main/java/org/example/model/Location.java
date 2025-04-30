package org.example.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.annotations.Where;

import java.util.ArrayList;
import java.util.List;

@Data
@Entity
@Table(name = "locations")
@EqualsAndHashCode(callSuper = true)
public class Location extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = true, length = 100)
    private String name;

    @Column(nullable = false, length = 255)
    private String address;

    @Column(nullable = false, length = 100)
    private String city;

    private Double latitude;

    private Double longitude;

    @OneToMany(mappedBy = "location")
    @JsonBackReference
    private List<Meetup> meetups = new ArrayList<>();

    public Location() {}

    public Location(String city, String address) {
        this.city = city;
        this.address = address;
    }

}
