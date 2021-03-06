package com.shape.web.repository;

import com.shape.web.entity.Project;
import com.shape.web.entity.Todolist;
import com.shape.web.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Created by seongahjo on 2016. 7. 2..
 */
public interface TodolistRepository extends JpaRepository<Todolist,Integer> {
    List<Todolist> findDistinctByUserAndProject_Processed(User user,boolean isprocessed);
    List<Todolist> findByProject(Project project);
}
