package gov.iti.jets.testing.web.controllers;

import gov.iti.jets.testing.domain.User;
import gov.iti.jets.testing.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.util.List;

@RestController
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("/users")
    public ResponseEntity<List<User>> getUsers() {
        return ResponseEntity.ok(userService.getAll());
    }

    @GetMapping("/users/{id}")
    public ResponseEntity<User> getUser(@PathVariable Long id) {
        return ResponseEntity.ok(userService.getById(id));
    }

    @PostMapping("/users")
    public ResponseEntity<String> addUser(@RequestBody User user) {
        User addUser = userService.addUser(user);
        return ResponseEntity.created(ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
                .buildAndExpand(user.getId()).toUri())
                .body("added successfully");
    }

    @PostMapping("/users/{id}/photo")
    public ResponseEntity<String> addUserPhoto(@PathVariable Long id, MultipartFile image) {
        userService.addUserPhoto(id, image);
        return ResponseEntity.ok("added photo");
    }

}
