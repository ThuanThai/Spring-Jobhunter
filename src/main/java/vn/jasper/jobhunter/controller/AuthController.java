package vn.jasper.jobhunter.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import vn.jasper.jobhunter.domain.User;
import vn.jasper.jobhunter.domain.dto.LoginDTO;
import vn.jasper.jobhunter.domain.dto.ResLoginDTO;
import vn.jasper.jobhunter.service.UserService;
import vn.jasper.jobhunter.utils.SecurityUtil;

import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.Optional;

@RestController
public class AuthController {

    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final SecurityUtil sercurityUtil;
    private final UserService userService;

    public AuthController(AuthenticationManagerBuilder authenticationManagerBuilder, SecurityUtil sercurityUtil, UserService userService) {
        this.authenticationManagerBuilder = authenticationManagerBuilder;
        this.sercurityUtil = sercurityUtil;
        this.userService = userService;
    }

    @PostMapping("/login")
    public ResponseEntity<ResLoginDTO> login(@Valid @RequestBody LoginDTO logginDto)
            throws MethodArgumentNotValidException {
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                logginDto.getUsername(), logginDto.getPassword());

        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);

        // create Access Token after login successfully
        String accessToken = this.sercurityUtil.creatToken(authentication);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        ResLoginDTO res = new ResLoginDTO();

        //get information of user
        Optional<User> currentUserDB = userService.handleFetchUserByUsername(logginDto.getUsername());
        if (currentUserDB.isPresent()) {
            ResLoginDTO.UserLogin userLogin = new ResLoginDTO.UserLogin(
                    currentUserDB.get().getId(),
                    currentUserDB.get().getEmail(),
                    currentUserDB.get().getName()
            );
            res.setUser(userLogin);
        }
        res.setAccessToken(accessToken);
        return ResponseEntity.ok().body(res);
    }
}
