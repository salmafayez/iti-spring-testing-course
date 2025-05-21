package gov.iti.jets.testing.web.controllers;

import gov.iti.jets.testing.domain.User;
import gov.iti.jets.testing.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
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

    //TODO
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

    @DeleteMapping("/private/users/{id}")
    public ResponseEntity<String> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return ResponseEntity.ok("deleted successfully");
    }

//    @PostMapping("/register")
//    public ResponseEntity<String> register(@RequestBody User user, MultipartFile image) {
//        userService.register(user, image);
//        return ResponseEntity.ok("added photo");
//    }

}
