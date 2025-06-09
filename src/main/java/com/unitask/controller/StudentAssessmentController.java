package com.unitask.controller;

import com.unitask.dto.PageRequest;
import com.unitask.service.StudentAssessmentService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@Validated
@RequestMapping(path = "student-assessment")
@AllArgsConstructor
public class StudentAssessmentController {

    private final StudentAssessmentService studentAssessmentService;

    @GetMapping("/list")
    public ResponseEntity<?> getAssessmentListing(PageRequest pageRequest) {
        return ResponseEntity.ok().body(studentAssessmentService.getAssessmentListing(pageRequest));
    }

    @PostMapping("/submit/{id}")
    public ResponseEntity<?> submitAssessment(@PathVariable("id") Long id, @RequestBody MultipartFile multipartFile) {
        studentAssessmentService.submit(id, multipartFile);
        return ResponseEntity.ok().body("OK");
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getAssessment(@PathVariable("id") Long id) {
        return ResponseEntity.ok().body(studentAssessmentService.read(id));
    }
}
