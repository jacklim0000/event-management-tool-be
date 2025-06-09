package com.unitask.entity;

import com.unitask.entity.User.AppUser;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@NoArgsConstructor
@Entity
@Table(name = "announcement")
@AllArgsConstructor
public class Announcement {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String title;

    @Column(columnDefinition = "text")
    private String description;

    @ManyToOne
    @JoinColumn
    private Subject subject;

    @ManyToOne
    @JoinColumn
    private AppUser owner;

    @Column
    private LocalDateTime postedDate = LocalDateTime.now();

}
