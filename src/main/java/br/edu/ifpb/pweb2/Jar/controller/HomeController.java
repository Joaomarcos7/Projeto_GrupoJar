package br.edu.ifpb.pweb2.Jar.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class HomeController {

    @GetMapping("/")
    public ModelAndView home(ModelAndView model) {
        model.setViewName("home");
        return model;
    }
}
