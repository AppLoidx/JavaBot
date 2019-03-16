package server.Application.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Arthur Kupriyanov
 */
@RestController
@RequestMapping
public class MainController {
    @GetMapping
    public String get(){
        return "Hello, Arthur! I am on heroku!!!";
    }

}
