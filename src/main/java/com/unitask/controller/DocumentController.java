package com.unitask.controller;

import com.unitask.dto.DocumentPageRequest;
import com.unitask.service.DocumentService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Validated
@RequestMapping(path = "document")
public class DocumentController {

    @Autowired
    private DocumentService documentService;

    @GetMapping("/list")
    public ResponseEntity<?> getDocumentList(DocumentPageRequest documentPageRequest) {
        return ResponseEntity.ok().body(documentService.getListing(documentPageRequest));
    }

    @GetMapping("/download")
    public void downloadDocument(String path, HttpServletResponse response) {
        documentService.downloadFile(path, response);
    }


}
