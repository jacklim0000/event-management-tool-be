package com.unitask.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.unitask.constant.Enum.GeneralStatus;
import com.unitask.entity.User.AppUser;
import com.unitask.entity.assessment.Assessment;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Getter
@Setter
@ToString
@NoArgsConstructor
@Entity
@Table(name = "subject")
@AllArgsConstructor
public class Subject {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "name")
    private String name;
    @Column(name = "code")
    private String code;
    @Column(name = "course")
    private String course;
    @Column(name = "creditHour")
    private Integer creditHour;
    @Column(name = "description", columnDefinition = "TEXT")
    private String description;
    @Column(name = "learningOutcome", columnDefinition = "TEXT")
    private String learningOutcome;
    @Column(name = "lecturerName")
    private String lecturerName;
    @Column(name = "lecturerContact")
    private String lecturerContact;
    @Column(name = "lecturerEmail")
    private String lecturerEmail;
    @Column(name = "lecturerOffice")
    private String lecturerOffice;
    @Column(name = "status")
    @Enumerated(value = EnumType.STRING)
    private GeneralStatus status;
    @Column
    private String color;
    @ManyToOne
    @JoinColumn
    private AppUser owner;
    @JsonManagedReference
    @OneToMany(mappedBy = "subject", orphanRemoval = true)
    @ToString.Exclude
    private List<Assessment> assessment;

}
