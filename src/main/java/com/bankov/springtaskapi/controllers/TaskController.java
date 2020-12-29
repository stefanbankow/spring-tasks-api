package com.bankov.springtaskapi.controllers;

import com.bankov.springtaskapi.dtos.TaskRequest;
import com.bankov.springtaskapi.models.Task;
import com.bankov.springtaskapi.models.User;
import com.bankov.springtaskapi.repos.TaskRepository;
import com.bankov.springtaskapi.repos.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

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

    @GetMapping("/{id}")
    public ResponseEntity<Task> getOneTask(Authentication auth, @PathVariable(value = "id") long taskId) {
        User user = userRepository.findByUsername(auth.getName()).orElseThrow(() ->
                new UsernameNotFoundException("Invalid auth"));

        Task task = taskRepository.findByIdAndBy(taskId, user).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND, "Invalid task id"));

        return ResponseEntity.ok(task);

    }

    @PostMapping
    public ResponseEntity<Task> createTask(Authentication auth, @RequestBody TaskRequest body
    ) {
        User user = userRepository.findByUsername(auth.getName()).orElseThrow(() ->
                new UsernameNotFoundException("Invalid auth"));
        String newTaskTitle = body.getTitle();
        String newTaskDescription = body.getDescription();
        boolean newTaskCompleted = body.getCompleted();
        Task newTask = taskRepository.save(Task.builder()
                .title(newTaskTitle)
                .description(newTaskDescription)
                .completed(newTaskCompleted).by(user).build());

        return ResponseEntity.ok(newTask);
    }
    @PatchMapping("/{id}")
    public ResponseEntity<Task> updateTask(Authentication auth,
                                           @RequestBody TaskRequest body,
                                           @PathVariable(value = "id") long taskId) {
        User user = userRepository.findByUsername(auth.getName()).orElseThrow(() ->
                new UsernameNotFoundException("Invalid auth"));
        Task task = taskRepository.findByIdAndBy(taskId, user).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND, "Invalid post id"));
        String newTitle = body.getTitle();
        String newDescription = body.getDescription();
        Boolean newCompleted = body.getCompleted();
        System.out.println("title" + newTitle);
        System.out.println("description" + newDescription);
        System.out.println("completed" + newCompleted);
        if(newTitle != null) {
            task.setTitle(body.getTitle());
        }
        if(newDescription != null) {
            task.setDescription(newDescription);
        }
        if(newCompleted != null && newCompleted != task.isCompleted()) {
            task.setCompleted(newCompleted);
        }
        taskRepository.save(task);
        return ResponseEntity.ok(task);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity deleteTask(Authentication auth,
                                                       @PathVariable(value = "id") long taskId) {
        User user = userRepository.findByUsername(auth.getName()).orElseThrow(() ->
                new UsernameNotFoundException("Invalid auth"));
        Task task = taskRepository.findById(taskId).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND, "Invalid post id"));

        if(task.getBy() == user || user.getAuthorities().contains("ROLE_ADMIN")) {
            taskRepository.delete(task);
        }

        System.out.println(user.getAuthorities());
        return ResponseEntity.noContent().build();
    }

}
