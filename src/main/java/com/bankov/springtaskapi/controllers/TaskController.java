package com.bankov.springtaskapi.controllers;

import com.bankov.springtaskapi.dtos.CreateTaskRequest;
import com.bankov.springtaskapi.models.Task;
import com.bankov.springtaskapi.models.User;
import com.bankov.springtaskapi.repos.TaskRepository;
import com.bankov.springtaskapi.repos.UserRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/tasks")
public class TaskController {
    private final UserRepository userRepository;
    private final TaskRepository taskRepository;

    public TaskController(TaskRepository taskRepository, UserRepository userRepository) {
        this.taskRepository = taskRepository;
        this.userRepository = userRepository;
    }

    @GetMapping
    public ResponseEntity<List<Task>> getUserTasks(Authentication auth) {

        User user = userRepository.findByUsername(auth.getName()).orElseThrow(() ->
                new UsernameNotFoundException("Invalid auth"));

        return ResponseEntity.ok(taskRepository.findAllByBy(user));

    }

    @PostMapping
    public ResponseEntity<Task> createTask(Authentication auth, @RequestBody CreateTaskRequest body
    ) {
        User user = userRepository.findByUsername(auth.getName()).orElseThrow(() ->
                new UsernameNotFoundException("Invalid auth"));
        String newTaskTitle = body.getTitle();
        String newTaskDescription = body.getDescription();
        boolean newTaskCompleted = body.isCompleted();
        Task newTask = taskRepository.save(Task.builder()
                .title(newTaskTitle)
                .description(newTaskDescription)
                .completed(newTaskCompleted).by(user).build());

        return ResponseEntity.ok(newTask);
    }
}
