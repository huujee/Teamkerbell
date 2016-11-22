package com.shape.web.controller;

import com.shape.web.entity.Alarm;
import com.shape.web.entity.Project;
import com.shape.web.entity.Schedule;
import com.shape.web.entity.User;
import com.shape.web.service.ProjectService;
import com.shape.web.service.ScheduleService;
import com.shape.web.service.UserService;
import com.shape.web.util.AlarmUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

/**
 * Created by seongahjo on 2016. 2. 7..
 */

/**
 * Handles requests for the calendar.
 */
@Slf4j
@RestController
public class CalendarController {

    @Autowired
    ProjectService projectService;
    @Autowired
    ScheduleService scheduleService;
    @Autowired
    UserService userService;

    /**
     * get Schedules
     * @param userIdx
     * @param page
     * @param size
     * @return List of schedules
     */
    @RequestMapping(value="/schedule/{userIdx}/user",method=RequestMethod.GET)
    public ResponseEntity getSchedules(@PathVariable("userIdx")Integer userIdx,
                                       @RequestParam(value = "page",defaultValue = "0") Integer page,
                                       @RequestParam(value="size",defaultValue = "10") Integer size){
        List lists=scheduleService.getSchedules(userService.getUser(userIdx));
        if(lists.size()!=0)
            return ResponseEntity.ok(lists);
        return ResponseEntity.badRequest().body("Empty list");
    }

    /**
     * Make Schedule
     * @param projectIdx
     * @param schedule
     */

    @RequestMapping(value = "/schedule", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.OK)
    public void makeSchedule(@RequestParam("projectIdx") Integer projectIdx, @ModelAttribute("schedule") Schedule schedule) {
        Project project = projectService.getProject(projectIdx);
        schedule.setProject(project);
        scheduleService.save(schedule);
        Alarm alarm = new Alarm(1, null, null, new Date());
        alarm.setProject(project);
        List<User> lu = userService.getUsersByProject(project);
        AlarmUtil.postAlarm(lu,alarm,true);
        log.info("[ROOM " + projectIdx + "] Make Schedule ");

    }

    /**
     * Update Schedule
     * @param schedule
     */
    @RequestMapping(value = "/schedule", method = RequestMethod.PUT)
    public void updateSchedule(@RequestBody Schedule schedule) {
        Schedule s = scheduleService.getSchedule(schedule.getScheduleidx());
        if (schedule.getStartdate() != null)
            s.setStartdate(schedule.getStartdate());
        if (schedule.getEnddate() != null)
            s.setEnddate(schedule.getEnddate());
        if (schedule.getState() != null)
            s.setState(schedule.getState());
        List<User> lu = userService.getUsersByProject(s.getProject());
        lu.forEach(u -> scheduleService.clear(u));
        scheduleService.save(s);
        log.info("modifying schedule");
    }

}
