package org.luotian.open.configuration.controller;

import com.alibaba.fastjson.JSON;
import org.luotian.open.configuration.service.HelloService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import java.util.HashMap;
import java.util.Map;

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

   @ResponseBody
   @RequestMapping("/world")
   public String world() {
       Map<String, String> worldMap = new HashMap<String, String>();
       worldMap.put("key1","value1");
       worldMap.put("key2", "value2");
       return JSON.toJSONString(worldMap);
   }

}