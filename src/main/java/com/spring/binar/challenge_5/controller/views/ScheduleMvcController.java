package com.spring.binar.challenge_5.controller.views;

import com.spring.binar.challenge_5.dto.ScheduleResponseDTO;
import com.spring.binar.challenge_5.service.ScheduleService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping(value = "/web-public/schedule")
public class ScheduleMvcController {

    private final ScheduleService scheduleService;

    @GetMapping("/list")
    public String findAll(Model model){
        List<ScheduleResponseDTO> schedules;
        schedules = scheduleService.findAll();

        model.addAttribute("schedules", schedules);

        return "schedule/schedule-list";
    }

    @GetMapping("/details/{scheduleId}")
    public String showDetail(@PathVariable("scheduleId") Integer scheduleId, Model model){

        var schedule = scheduleService.findById(scheduleId);

        model.addAttribute("schedule", schedule);

        return "schedule/schedule-details";
    }

    @GetMapping("/update/{scheduleId}")
    public String updateSchedule(@PathVariable("scheduleId") Integer scheduleId, Model model){

        var schedule = scheduleService.findById(scheduleId);

        model.addAttribute("schedule", schedule);

        return "schedule/schedule-form";
    }

    @GetMapping("/delete/{scheduleId}")
    public String deleteSchedule(@PathVariable("scheduleId") Integer scheduleId, Model model){

        scheduleService.delete(scheduleId);

        return "redirect:/web-public/schedule/list";
    }




}
