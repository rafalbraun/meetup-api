package org.example.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.annotations.Where;

@Data
@Entity
@Table(name = "comments")
@EqualsAndHashCode(callSuper = true)
public class Comment extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "meetup_id", nullable = false)
    private Meetup meetup;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User author;

    @Lob
    @Column(nullable = false)
    private String content;

}
