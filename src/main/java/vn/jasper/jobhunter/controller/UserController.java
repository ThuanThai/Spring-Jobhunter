package vn.jasper.jobhunter.controller;

import com.turkraft.springfilter.boot.Filter;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.web.bind.annotation.*;

import vn.jasper.jobhunter.annotation.ApiMessage;
import vn.jasper.jobhunter.domain.User;
import vn.jasper.jobhunter.domain.dto.ResCreateUserDTO;
import vn.jasper.jobhunter.domain.dto.ResUpdateUserDTO;
import vn.jasper.jobhunter.domain.dto.ResUserDTO;
import vn.jasper.jobhunter.domain.dto.ResultPaginationDTO;
import vn.jasper.jobhunter.service.UserService;
import vn.jasper.jobhunter.utils.error.IdInvalidException;

import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@RestController
@RequestMapping("/api/v1")
public class UserController {

    private UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/user")
    @ApiMessage("Create new user")
    public ResponseEntity<ResCreateUserDTO> createNewUser(@RequestBody User user) throws IdInvalidException {
       boolean emailExisted = userService.isEmailExisted(user.getEmail());
        if (emailExisted) {
            throw new IdInvalidException(
                    "Email " + user.getEmail() + "is existed, please user another email");
        }
        User createdUser = userService.handleCreateUser(user);
        return ResponseEntity.status(HttpStatus.CREATED).body(userService.convertToResCreateUserDTO(createdUser));
    }

    @DeleteMapping("/user/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable("id") long id) throws IdInvalidException {
        Optional<User> currentUser = this.userService.handleFetchUserById(id);
        if (currentUser.isPresent()) {
            throw new IdInvalidException("User with id" + id + "is not existed");
        }
        this.userService.handleDeleteUser(id);
        return ResponseEntity.noContent().build();
    }

    @ApiMessage("Fetch user by ID")
    @GetMapping("/user/{id}")
    public ResponseEntity<ResUserDTO> fetchUserById(@PathVariable("id") long id) throws IdInvalidException {
        Optional<User> optionalUser = userService.handleFetchUserById(id);
       if (optionalUser.isPresent()) {
           throw new IdInvalidException("User with id" + id + "is not existed");
       }
       return ResponseEntity.status(HttpStatus.OK).body(userService.convertToResUserDTO(optionalUser.get()));
    }

    @GetMapping("/user")
    @ApiMessage("Fetch all users")
    public ResponseEntity<ResultPaginationDTO> fetchAllUser(@Filter Specification<User> spec, Pageable pageable) {
        return ResponseEntity.ok().body(userService.fetchAllUser(spec, pageable));
    }

    @PutMapping("/user")
    public ResponseEntity<ResUpdateUserDTO> updateUser(@RequestBody User user) throws IdInvalidException {
        User updatedUser = userService.handleUpdateUser(user);
        if (updatedUser == null) {
            throw new IdInvalidException("User with id" + user.getId() + "is not existed");
        }
        return ResponseEntity.ok().body(userService.convertToResUpdateUserDTO(updatedUser));
    }
}
