package org.example.repository;

import org.example.model.Group;
import org.example.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface GroupRepository extends JpaRepository<Group, Long> {

    List<Group> findByMembersContaining(User user);
    List<Group> findByOwner(User user);

}
