package com.unitask.dto;

import lombok.Data;

import java.time.LocalDate;

@Data
public class GroupMemberListDto {

    private Long id;
    private String name;
    private String score;

}
