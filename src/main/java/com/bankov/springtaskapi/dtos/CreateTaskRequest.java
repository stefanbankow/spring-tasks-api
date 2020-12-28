package com.bankov.springtaskapi.dtos;

import lombok.Data;

import javax.validation.constraints.NotEmpty;

@Data
public class CreateTaskRequest {
    @NotEmpty
    private String title;

    private String description;
    private boolean completed;
}
