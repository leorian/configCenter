package org.luotian.open.configuration.controller;

import org.luotian.open.configuration.domain.Greeting;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.atomic.AtomicLong;

/**
 * Created by xiezhonggui on 17-1-15.
 */
@RestController
public class GreetingController {
    private static final String template="Hello, %s";
    private final AtomicLong counter = new AtomicLong();

    @RequestMapping("/greeting")
    public Greeting greeting(@RequestParam(value = "name", defaultValue = "World") String name) {
        return new Greeting(counter.getAndIncrement(), String.format(template, name));
    }
}
