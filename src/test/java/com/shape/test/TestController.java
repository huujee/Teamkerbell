package com.shape.test;

import com.shape.web.configuration.SpringConfig;
import com.shape.web.entity.Todolist;
import com.shape.web.repository.FileDBRepository;
import com.shape.web.repository.ProjectRepository;
import com.shape.web.repository.TodolistRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.io.IOException;
import java.util.Date;

/**
 * Created by seongahjo on 2016. 7. 16..
 */
@Slf4j
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {SpringConfig.class})
@ComponentScan("com.shape.web.repository")
public class TestController {
    @Autowired
    TodolistRepository todolistRepository;

    @Test
    public void test() throws IOException {
        Todolist to1= new Todolist("test1",new Date(),new Date());
        Todolist to2=new Todolist("test2",new Date(),new Date());
        Todolist to3=new Todolist("test3",new Date(),new Date());
        to1.addsubTodolist(to2);
        to1.addsubTodolist(to3);
        todolistRepository.saveAndFlush(to1);
        todolistRepository.saveAndFlush(to2);
        todolistRepository.saveAndFlush(to3);
        log.info(to1.toString());



    }
}
