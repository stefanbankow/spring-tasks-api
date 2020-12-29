package com.bankov.springtaskapi.dtos;

import lombok.Data;

import javax.validation.constraints.NotEmpty;

@Data
public class TaskRequest {
    @NotEmpty
    private String title;

    private String description;
    private Boolean completed;
}
