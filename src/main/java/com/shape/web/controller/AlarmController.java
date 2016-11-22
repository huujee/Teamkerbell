package com.shape.web.controller;

import com.shape.web.entity.Alarm;
import com.shape.web.entity.User;
import com.shape.web.service.AlarmService;
import com.shape.web.service.ProjectService;
import com.shape.web.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;

import javax.servlet.http.HttpSession;
import java.util.List;

/**
 * Created by hootting on 2016. 9. 13..
 */
@Slf4j
@RestController
public class AlarmController {


    @Autowired
    UserService userService;

    @Autowired
    AlarmService alarmService;

    @Autowired
    ProjectService projectService;


    /**
     * Get paged Timlines
     *
     * @param userIdx
     * @param page
     * @param count
     * @return List of Timelines
     */
    @RequestMapping(value = "/timeline/{userIdx}", method = RequestMethod.GET)
    public ResponseEntity timeline(@PathVariable("userIdx") Integer userIdx,
                                   @RequestParam(value = "page", defaultValue = "0") Integer page,
                                   @RequestParam(value = "count", defaultValue = "20") Integer count) {

        List lists = alarmService.getTimelines(userService.getUser(userIdx), page, count);
        log.info("page "+ page+ " "+count);
        if (lists.size() != 0)
            return ResponseEntity.ok(lists);
        else
            return ResponseEntity.badRequest().body("No more Tineline");
    }

    @RequestMapping(value = "/alarm/{userId}", method = RequestMethod.GET)
    public List alarms(@PathVariable("userIdx") Integer userIdx,
                       @RequestParam(value = "page", defaultValue = "0") Integer page,
                       @RequestParam(value = "size", defaultValue = "10") Integer size) {
        return alarmService.getAlarms(userService.getUser(userIdx));
    }


    /**
     * Accept the request
     *
     * @param alarmIdx
     * @param type
     */
    @RequestMapping(value = "/acceptRequest", method = RequestMethod.GET)
    public void acceptRequest(@RequestParam("alarmIdx") Integer alarmIdx, @RequestParam("type") Integer type) {
        Alarm alarm = alarmService.getAlarm(alarmIdx);
        alarm.setIsshow(false);
        if (type == 1)
            alarm.getUser().addProject(alarm.getProject());
        alarmService.save(alarm);
        projectService.save(alarm.getUser(), alarm.getProject());
    }

}
