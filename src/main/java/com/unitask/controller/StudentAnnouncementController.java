package com.unitask.controller;

import com.unitask.dto.PageRequest;
import com.unitask.service.StudentAnnouncementService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Validated
@RequestMapping(path = "student-announcement")
@AllArgsConstructor
public class StudentAnnouncementController {

    private final StudentAnnouncementService studentAnnouncementService;

    @GetMapping("/{id}")
    public ResponseEntity<?> create(@PathVariable Long id) {
        return ResponseEntity.ok().body(studentAnnouncementService.read(id));
    }

    @GetMapping("/list")
    public ResponseEntity<?> list(PageRequest pageRequest) {
        return ResponseEntity.ok().body(studentAnnouncementService.list(pageRequest));
    }
}
