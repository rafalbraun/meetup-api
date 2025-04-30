package org.example.repository;

import org.example.model.Group;
import org.example.model.User;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface GroupRepository extends JpaRepository<Group, Long> {

    List<Group> findByMembersContaining(User user);
    List<Group> findByOwner(User user);

//    @Query("SELECT g FROM Group g LEFT JOIN FETCH g.meetups WHERE g.id = :id")
//    Optional<Group> findByIdWithMeetups(@Param("id") Long id);

    @Query("SELECT g FROM Group g LEFT JOIN FETCH g.meetups WHERE g.id = :id")
    Optional<Group> findByIdWithMeetups(@Param("id") Long id);

    @Query("SELECT DISTINCT g FROM Group g LEFT JOIN FETCH g.meetups")
    List<Group> findAllWithMeetups();

}
