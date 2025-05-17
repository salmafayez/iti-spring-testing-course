package gov.iti.jets.testing.web.controllers;

import gov.iti.jets.testing.service.RegistrationService;
import gov.iti.jets.testing.web.dtos.RegistrationDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RegistrationController {

    @Autowired
    private RegistrationService registrationService;

    //TODO
    @PostMapping("/event/register")
    public ResponseEntity<String> register(@RequestBody RegistrationDto registrationDto) {
        registrationService.register(registrationDto);
        return ResponseEntity.ok("success");
    }
}
