package org.example.repository;

import org.example.model.Location;
import org.example.model.Meetup;
import org.example.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface MeetupRepository extends JpaRepository<Meetup, Long> {
    List<Meetup> findByLocation(Location location);
    List<Meetup> findByLocationId(Long locationId);

//    @Query("SELECT m FROM Meetup m WHERE m.deletedAt IS NULL")
//    List<Meetup> findAllActive();

    List<Meetup> findByAttendeesContaining(User user);
    List<Meetup> findByOrganizer(User user);

}
