package com.unitask.controller;

import com.unitask.dto.PageRequest;
import com.unitask.dto.assessment.AssessmentRequest;
import com.unitask.service.AssessmentService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@Validated
@RequestMapping(path = "assessment")
@AllArgsConstructor
public class AssessmentController {

    @Autowired
    private final AssessmentService assessmentService;

    @GetMapping("/list")
    public ResponseEntity<?> getListing(PageRequest pageRequest) {
        return ResponseEntity.ok().body(assessmentService.getListing(pageRequest));
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getAssessment(@PathVariable("id") Long id) {
        return ResponseEntity.ok().body(assessmentService.read(id));
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<?> updateAssessment(@PathVariable("id") Long id, @RequestBody AssessmentRequest assessmentRequest) {
        assessmentService.update(id, assessmentRequest);
        return ResponseEntity.ok().body("OK");
    }

    @PostMapping("/update/{id}/file")
    public ResponseEntity<?> uploadFile(@PathVariable("id") Long id, @RequestBody MultipartFile multipartFile) {
        assessmentService.uploadFile(id, multipartFile);
        return ResponseEntity.ok().body("OK");
    }

    @DeleteMapping("/file/{id}")
    public ResponseEntity<?> deleteFile(@PathVariable("id") Long fileId) {
        assessmentService.deleteFile(fileId);
        return ResponseEntity.ok().body("OK");
    }
}
