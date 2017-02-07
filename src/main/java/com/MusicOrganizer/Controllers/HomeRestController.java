package com.MusicOrganizer.Controllers;

import com.MusicOrganizer.Greeting;
import com.fasterxml.jackson.databind.JsonSerializer;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.atomic.AtomicLong;

/**
 * Created by user on 2/2/2017.
 */
@RestController
public class HomeRestController {

    private static final String template = "Hello, %s!";
    private final AtomicLong counter = new AtomicLong();

    @RequestMapping("/greeting")
    public Greeting greeting(@RequestParam(name = "name", defaultValue = "World") String name) {

        return new Greeting(String.format(template, name), counter.getAndIncrement());
    }
}
