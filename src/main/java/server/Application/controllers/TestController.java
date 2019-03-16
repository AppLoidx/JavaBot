package server.Application.controllers;

import org.springframework.web.bind.annotation.*;

/**
 * @author Arthur Kupriyanov
 */
@RestController
@RequestMapping("home")
public class TestController {

        @GetMapping("/{id}")
        public String get(@PathVariable("id") int id){
            return "This is first text to " + id;
        }

        @GetMapping
        public String request(@RequestParam(value="id",defaultValue = "0") int id){
            return "YOur id is " + id;
        }

}
