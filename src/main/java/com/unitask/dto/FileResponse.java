package com.unitask.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class FileResponse {
    private Long id;
    private String name;
    private String url;
    private LocalDateTime createdDate;
}
