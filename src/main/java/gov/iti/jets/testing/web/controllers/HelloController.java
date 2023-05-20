package gov.iti.jets.testing.web.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController {

    @GetMapping("/hello")
    public ResponseEntity<String> greeting() {
        return ResponseEntity.ok("hello, intake 43");
    }

}
