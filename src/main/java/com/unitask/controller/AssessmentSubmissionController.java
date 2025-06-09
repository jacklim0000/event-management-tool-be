package com.unitask.controller;

import com.unitask.dto.AssessmentSubmissionPageRequest;
import com.unitask.dto.PageRequest;
import com.unitask.dto.assessment.AssessmentGradeRequest;
import com.unitask.service.AssessmentSubmissionService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@Validated
@RequestMapping(path = "assessment-submission")
@AllArgsConstructor
public class AssessmentSubmissionController {

    private final AssessmentSubmissionService assessmentSubmissionService;

    @GetMapping("/list")
    public ResponseEntity<?> getListing(AssessmentSubmissionPageRequest pageRequest) {
        return ResponseEntity.ok().body(assessmentSubmissionService.getListing(pageRequest));
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getListing(@PathVariable Long id) {
        return ResponseEntity.ok().body(assessmentSubmissionService.read(id));
    }

    @PutMapping("/grade/{id}")
    public ResponseEntity<?> resubmit(@PathVariable Long id, @RequestBody AssessmentGradeRequest request) {
        return ResponseEntity.ok().body(assessmentSubmissionService.grade(id, request));
    }

    @PutMapping("/resubmit/{id}")
    public ResponseEntity<?> resubmit(@PathVariable Long id) {
        return ResponseEntity.ok().body(assessmentSubmissionService.resubmit(id));
    }
}
