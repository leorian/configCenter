package org.luotian.open.configuration.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;


@RequestMapping(value="/test")
@Controller
public class TestController {  
    @RequestMapping(value="/index")  
    public String index(Model model) {
        String name = "tester";  
        model.addAttribute("name", name);  
        return "test/index";  
    }  
}  