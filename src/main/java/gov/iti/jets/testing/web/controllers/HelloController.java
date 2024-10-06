package gov.iti.jets.testing.web.controllers;

import gov.iti.jets.testing.service.HelloService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController {

    @Autowired
    private HelloService helloService;

    @GetMapping("/hello")
    public ResponseEntity<String> greeting() {
        String helloResponse = helloService.getHello();
        return ResponseEntity.ok(helloResponse);
    }

}
