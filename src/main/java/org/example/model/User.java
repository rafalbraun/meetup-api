package org.example.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.annotations.Where;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

@Data
@Entity
@Table(name = "users")
@EqualsAndHashCode(callSuper = true)
public class User extends BaseEntity implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 50)
    private String username;

    @Column(unique = true, length = 100)
    private String email;

    @Column(nullable = false, length = 255)
    private String passwordHash;

    private String bio;

    private String avatarUrl;

    public User() {}

    public User(String username, String passwordHash) {
        this.username = username;
        this.passwordHash = passwordHash;
    }

    @ElementCollection(fetch = FetchType.EAGER)
    private List<String> roles = new ArrayList<>();

    // --- UserDetails methods ---
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return roles.stream()
                .map(role -> (GrantedAuthority) () -> "ROLE_" + role)
                .toList();
    }

    @Override
    public String getUsername() {
        return this.username;
    }

    public String getPassword() { return passwordHash; }

    public void setPassword(String passwordHash) { this.passwordHash = passwordHash; }

    @Override
    public boolean isAccountNonExpired() { return true; }

    @Override
    public boolean isAccountNonLocked() { return true; }

    @Override
    public boolean isCredentialsNonExpired() { return true; }

    @Override
    public boolean isEnabled() { return true; }

    @OneToMany(mappedBy = "organizer", cascade = CascadeType.ALL)
    @JsonBackReference
    private List<Meetup> organizedMeetups = new ArrayList<>();

    @ManyToMany(mappedBy = "attendees")
    @JsonBackReference
    private List<Meetup> joinedMeetups = new ArrayList<>();

//    @OneToMany(mappedBy = "owner")
//    @JsonBackReference
    @OneToMany(mappedBy = "owner", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonBackReference
    private List<Group> ownedGroups = new ArrayList<>();

    @ManyToMany(mappedBy = "members")
    @JsonBackReference
    private List<Group> groups = new ArrayList<>();

}

