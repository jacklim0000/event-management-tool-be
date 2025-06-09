package com.unitask.entity.assessment;

import com.unitask.entity.Group;
import com.unitask.entity.OssFile;
import com.unitask.entity.StudentAssessment;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Builder
@Getter
@Setter
@ToString
@NoArgsConstructor
@Entity
@Table(name = "assessment_submission")
@AllArgsConstructor
public class AssessmentSubmission extends OssFile {


    @ManyToOne
    @JoinColumn(nullable = false)
    private Assessment assessment;

    //optional if group
    @ManyToOne
    @JoinColumn
    private Group group;

    //optional if individual
    @ManyToOne
    @JoinColumn
    private StudentAssessment studentAssessment;

    private LocalDateTime submissionDate;

    private Boolean resubmit;
}
