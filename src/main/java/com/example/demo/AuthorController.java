package com.example.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Map;

@Controller
public class AuthorController {


    @Autowired
    AuthorService movieService;

    @RequestMapping(value = "/authors", method = RequestMethod.GET)
    public String showMovies(@RequestParam(value = "orderby",required = false, defaultValue = "name") String order,
                             @RequestParam(value = "name",required = false,defaultValue = "") String name,
                             @RequestParam(value = "path") String path,
                             Model model){
        Map<String, Integer> authors = movieService.showAllNames(order, name, path);
        model.addAttribute("authors", authors);
        return "authors";
    }
}
