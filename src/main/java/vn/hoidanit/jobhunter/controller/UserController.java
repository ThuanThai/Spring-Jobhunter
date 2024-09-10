package vn.hoidanit.jobhunter.controller;

import org.springframework.web.bind.annotation.RestController;

import vn.hoidanit.jobhunter.domain.User;
import vn.hoidanit.jobhunter.service.UserService;
import vn.hoidanit.jobhunter.utils.error.IdInvalidExeption;

import java.util.List;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;

@RestController
public class UserController {

    private UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/user")
    public ResponseEntity<User> createNewUser(@RequestBody User user) {
        User createdUser = userService.handleCreateUser(user);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdUser);
    }

    @DeleteMapping("/user/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable("id") long id) throws IdInvalidExeption {
        if (id > 1500)
            throw new IdInvalidExeption("id is greater than 1500");
        userService.handleDeleteUser(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/user/{id}")
    public ResponseEntity<User> fetchUserById(@PathVariable("id") long id) {
        Optional<User> optionalUser = userService.handleFetchUserById(id);
        if (optionalUser.isPresent()) {
            return ResponseEntity.ok().body(optionalUser.get());
        } else {
            return null;
        }
    }

    @GetMapping("/user")
    public ResponseEntity<List<User>> fetchAllUser() {
        return ResponseEntity.ok().body(userService.fetchAllUser());
    }

    @PutMapping("/user")
    public ResponseEntity<User> putMethodName(@RequestBody User entity) {

        return ResponseEntity.ok().body(userService.handleUpdateUser(entity));
    }

}
