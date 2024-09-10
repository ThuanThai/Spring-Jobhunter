package vn.hoidanit.jobhunter.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import vn.hoidanit.jobhunter.utils.error.IdInvalidExeption;

@RestController
public class HelloController {

    @GetMapping("/")
    public String getHelloWorld() {
        return "hello jasper";
    }
}
