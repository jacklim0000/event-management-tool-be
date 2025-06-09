package com.unitask.entity;

import com.unitask.constant.Enum.GeneralStatus;
import com.unitask.entity.User.AppUser;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Builder
@Getter
@Setter
@ToString
@NoArgsConstructor
@Entity
@Table(name = "student_subject")
@AllArgsConstructor
public class StudentSubject {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn
    private AppUser user;

    @ManyToOne
    @JoinColumn
    private Subject subject;

    private LocalDate enrollmentDate;

    @Enumerated(EnumType.STRING)
    private GeneralStatus status;

}
