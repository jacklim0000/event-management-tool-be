package com.unitask.entity.assessment;

import com.unitask.entity.OssFile;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@NoArgsConstructor
@Entity
@Table(name = "assessment_file")
public class AssessmentFile extends OssFile {

    @ManyToOne
    @JoinColumn(nullable = false)
    private Assessment assessment;

}
