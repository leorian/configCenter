package org.luotian.open.configuration.controller;

import org.luotian.open.configuration.service.HelloService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
  
@Controller
public class HelloController {

    @Autowired
    private HelloService helloService;

   @RequestMapping("/hello")
   public ModelAndView hello(){
     ModelAndView mv =new ModelAndView();
     mv.addObject("spring", "spring mvc");
     mv.addObject("message", helloService.test("管理员"));
     mv.setViewName("hello");
     return mv;
   }

}