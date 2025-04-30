package org.example.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Where;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@Entity
@Table(name = "groups")
@EqualsAndHashCode(callSuper = true)
public class Group extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long id;

    @Column(nullable = false, unique = true, length = 50)
    public String name;

    @OneToMany(mappedBy = "group", fetch = FetchType.EAGER)
    @JsonManagedReference
    private List<Meetup> meetups = new ArrayList<>();

    @ManyToOne
    @JoinColumn(name = "owner_id", nullable = false)
    private User owner;

    @ManyToMany
    @JoinTable(name = "group_members",
            joinColumns = @JoinColumn(name = "group_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id"))
    @JsonManagedReference
    private List<User> members = new ArrayList<>();

    public Group(String name, User owner) {
        this.name = name;
        this.owner = owner;
    }

    public List<Meetup> getMeetups() {
        return meetups;
    }

}
