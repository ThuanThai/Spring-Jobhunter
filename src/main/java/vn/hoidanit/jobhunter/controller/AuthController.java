package vn.hoidanit.jobhunter.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import vn.hoidanit.jobhunter.domain.DTO.LoginDTO;
import vn.hoidanit.jobhunter.domain.DTO.LoginResDTO;
import vn.hoidanit.jobhunter.utils.SecurityUtil;

import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
public class AuthController {

    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final SecurityUtil sercurityUtil;

    public AuthController(AuthenticationManagerBuilder authenticationManagerBuilder, SecurityUtil sercurityUtil) {
        this.authenticationManagerBuilder = authenticationManagerBuilder;
        this.sercurityUtil = sercurityUtil;
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResDTO> login(@Valid @RequestBody LoginDTO logginDto)
            throws MethodArgumentNotValidException {
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                logginDto.getUsername(), logginDto.getPassword());

        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);

        // create Access Token after login successfully
        String accessToken = this.sercurityUtil.creatToken(authentication);
        LoginResDTO res = new LoginResDTO();
        res.setAccessToken(accessToken);

        return ResponseEntity.ok().body(res);
    }
}
