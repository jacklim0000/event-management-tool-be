package com.unitask.controller;

import com.amazonaws.Response;
import com.unitask.dto.PageRequest;
import com.unitask.dto.group.GroupRequest;
import com.unitask.service.GroupService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@Validated
@RequestMapping(path = "group")
@AllArgsConstructor
public class GroupController {

    @Autowired
    private GroupService groupService;

    @PostMapping("/create")
    public ResponseEntity<?> createGroup(@RequestBody GroupRequest groupRequest) {
        groupService.createGroup(groupRequest);
        return ResponseEntity.ok().body("OK");
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<?> updateGroup(@PathVariable("id") Long id, @RequestBody GroupRequest groupRequest) {
        groupService.updateGroup(id, groupRequest);
        return ResponseEntity.ok().body("OK");
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getGroup(@PathVariable("id") Long id) {
        return ResponseEntity.ok().body(groupService.getGroup(id));
    }

    @GetMapping("/list")
    public ResponseEntity<?> getAssessmentListing(PageRequest pageRequest) {
        return ResponseEntity.ok().body(groupService.getList(pageRequest));
    }

    @GetMapping("/student")
    public ResponseEntity<?> getStudentListing() {
        return ResponseEntity.ok().body(groupService.getStudentListing());
    }

    @GetMapping("/studentAssignmentDropdown/{id}")
    public ResponseEntity<?> getStudentAssignmentDropdown(@PathVariable("id")Long id) {
        return ResponseEntity.ok().body(groupService.getStudentAssignmentDropdown(id));
    }

}
