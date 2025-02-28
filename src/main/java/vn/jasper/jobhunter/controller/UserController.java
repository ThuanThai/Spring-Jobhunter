package vn.jasper.jobhunter.controller;

import com.turkraft.springfilter.boot.Filter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.web.bind.annotation.*;

import vn.jasper.jobhunter.annotation.ApiMessage;
import vn.jasper.jobhunter.domain.User;
import vn.jasper.jobhunter.domain.dto.ResultPaginationDTO;
import vn.jasper.jobhunter.service.UserService;
import vn.jasper.jobhunter.utils.error.IdInvalidExeption;

import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

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

    @ApiMessage("Fetch user by ID")
    @GetMapping("/user/{id}")
    public ResponseEntity<User> fetchUserById(@PathVariable("id") long id) {
        Optional<User> optionalUser = userService.handleFetchUserById(id);
        return optionalUser.map(user -> ResponseEntity.ok().body(user)).orElse(null);
    }

    @GetMapping("/user")
    @ApiMessage("Fetch all users")
    public ResponseEntity<ResultPaginationDTO> fetchAllUser(@Filter Specification<User> spec, Pageable pageable) {
        return ResponseEntity.ok().body(userService.fetchAllUser(spec, pageable));
    }

    @PutMapping("/user")
    public ResponseEntity<User> putMethodName(@RequestBody User entity) {
        return ResponseEntity.ok().body(userService.handleUpdateUser(entity));
    }

}
