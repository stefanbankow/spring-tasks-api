package com.bankov.springtaskapi.repos;

import com.bankov.springtaskapi.models.Task;
import com.bankov.springtaskapi.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {
    List<Task> findAllByBy(User user);
    Optional<Task> findByIdAndBy(long id, User user);
}
