package com.unitask.controller;

import com.unitask.dto.PageRequest;
import com.unitask.dto.subject.SubjectRequest;
import com.unitask.service.SubjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@Validated
@RequestMapping(path = "subject")
public class SubjectController {

    @Autowired
    SubjectService subjectService;

    @PostMapping("/create")
    public ResponseEntity<?> createSubject(@RequestBody SubjectRequest subjectRequest) {
        subjectService.create(subjectRequest);
        return ResponseEntity.ok().body("OK");
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<?> updateSubject(@PathVariable("id") Long subjectId, @RequestBody SubjectRequest subjectRequest) {
        subjectService.updateSubject(subjectId, subjectRequest);
        return ResponseEntity.ok().body("OK");
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getSubject(@PathVariable("id") Long subjectId) {
        return ResponseEntity.ok().body(subjectService.getSubject(subjectId));
    }

    @GetMapping("/list")
    public ResponseEntity<?> subjectListing(PageRequest pageRequest, @RequestParam(required = false) Long subjectId) {
        return ResponseEntity.ok().body(subjectService.getListing(pageRequest,subjectId));
    }
}
