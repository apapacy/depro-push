/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.deprosystem.push.controller;


import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Controller
public class IndexController {

    //private final VisitsRepository visitsRepository;

    //public IndexController(VisitsRepository visitsRepository) {
    //    this.visitsRepository = visitsRepository;
    //}

    public IndexController() {
    }

    
    @GetMapping("/test")
    public ModelAndView index() {
        Map<String, String> model = new HashMap<>();
        model.put("name", "Alexey");

        //Visit visit = new Visit();
        //visit.description = String.format("Visited at %s", LocalDateTime.now());
        //visitsRepository.save(visit);

        return new ModelAndView("index", model);
    }
}