package com.unitask.controller;

import com.amazonaws.Response;
import com.unitask.dto.PageRequest;
import com.unitask.service.StudentGroupService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@Validated
@RequestMapping(path = "student-group")
@AllArgsConstructor
public class StudentGroupController {

    @Autowired
    private StudentGroupService studentGroupService;

    @PostMapping("/join/{id}")
    public ResponseEntity<?> joinGroup(@PathVariable("id") Long id) {
        studentGroupService.joinGroup(id);
        return ResponseEntity.ok().body("OK");
    }

//    @PostMapping("/submit/{id}")
//    public ResponseEntity<?> submitGroup(@PathVariable("id") Long id, @RequestBody MultipartFile multipartFile) {
//        studentGroupService.submitGroup(id, multipartFile);
//        return ResponseEntity.ok().body("OK");
//    }

    @PostMapping("/leave/{id}")
    public ResponseEntity<?> leaveGroup(@PathVariable("id") Long id) {
        studentGroupService.leaveGroup(id);
        return ResponseEntity.ok().body("OK");
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getGroup(@RequestParam Long id) {
        return ResponseEntity.ok().body(studentGroupService.getGroup(id));
    }

    @GetMapping("/{assessmentId}/members")
    public ResponseEntity<?> getGroupMember(@PathVariable Long assessmentId) {
        return ResponseEntity.ok().body(studentGroupService.getGroupList(assessmentId));
    }

    @GetMapping("/list")
    public ResponseEntity<?> list(PageRequest pageRequest) {
        return ResponseEntity.ok().body(studentGroupService.getList(pageRequest));
    }

    @GetMapping("/public")
    public ResponseEntity<?> groupListing(PageRequest pageRequest) {
        return ResponseEntity.ok().body(studentGroupService.getGroupList(pageRequest));
    }

    @GetMapping("/assessment/list")
    public ResponseEntity<?> getAssessments( ) {
        return ResponseEntity.ok().body(studentGroupService.getAssessment());
    }

}
