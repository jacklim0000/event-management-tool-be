package com.unitask.entity.assessment;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@NoArgsConstructor
@Entity
@Table(name = "assessment_marking_rubric")
public class AssessmentMarkingRubric {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column
    private String criteria;
    @Column
    private String weightage;

    @ManyToOne
    @JoinColumn
    private Assessment assessment;

}
