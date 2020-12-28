package com.bankov.springtaskapi.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "tasks")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Task implements Serializable {
    @Id
    @GeneratedValue
    private long id;

    private String title;
    private String description;
    private boolean completed;
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User by;
}
