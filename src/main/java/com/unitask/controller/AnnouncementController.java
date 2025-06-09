package com.unitask.controller;


import com.unitask.dto.PageRequest;
import com.unitask.dto.annoucement.AnnouncementRequest;
import com.unitask.service.AnnouncementService;
import jakarta.mail.MessagingException;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@Validated
@RequestMapping(path = "announcement")
@AllArgsConstructor
public class AnnouncementController {

    private final AnnouncementService announcementService;

    @PostMapping("/create")
    public ResponseEntity<?> create(@RequestBody AnnouncementRequest announcementRequest) throws MessagingException {
        return ResponseEntity.ok().body(announcementService.create(announcementRequest));
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> read(@PathVariable Long id) {
        return ResponseEntity.ok().body(announcementService.read(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> read(@PathVariable Long id, @RequestBody AnnouncementRequest announcementRequest) {
        return ResponseEntity.ok().body(announcementService.update(id, announcementRequest));
    }

    @GetMapping("/list")
    public ResponseEntity<?> list(PageRequest pageRequest) {
        return ResponseEntity.ok().body(announcementService.list(pageRequest));
    }
}
