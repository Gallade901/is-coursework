package main.iscourseworkback.present.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/main")
public class PresentController {
    @GetMapping("/")
    public ResponseEntity<String> gg () {
        return ResponseEntity.ok("Welcome");
    }

}
