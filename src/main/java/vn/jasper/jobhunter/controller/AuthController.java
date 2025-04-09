package vn.jasper.jobhunter.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import vn.jasper.jobhunter.annotation.ApiMessage;
import vn.jasper.jobhunter.domain.User;
import vn.jasper.jobhunter.domain.dto.LoginDTO;
import vn.jasper.jobhunter.domain.dto.ResLoginDTO;
import vn.jasper.jobhunter.service.UserService;
import vn.jasper.jobhunter.utils.SecurityUtil;

import org.springframework.web.bind.MethodArgumentNotValidException;
import vn.jasper.jobhunter.utils.error.IdInvalidException;

import java.util.Optional;

@RestController
@RequestMapping("/api/v1")
//@RequestMapping(name = "/api/v1")
public class AuthController {

    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final SecurityUtil securityUtil;
    private final UserService userService;

    @Value("${jobhunter.jwt.refresh-token-validity-in-seconds}")
    private long refreshTokenExpiration;

    public AuthController(AuthenticationManagerBuilder authenticationManagerBuilder, SecurityUtil sercurityUtil, UserService userService) {
        this.authenticationManagerBuilder = authenticationManagerBuilder;
        this.securityUtil = sercurityUtil;
        this.userService = userService;
    }

    @PostMapping("/auth/login")
    public ResponseEntity<ResLoginDTO> login(@Valid @RequestBody LoginDTO loginDto)
            throws MethodArgumentNotValidException {
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                loginDto.getUsername(), loginDto.getPassword());

        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);

        // create Access Token after login successfully
        SecurityContextHolder.getContext().setAuthentication(authentication);
        ResLoginDTO res = new ResLoginDTO();

        //get information of user
    Optional<User> currentUserDB = userService.handleFetchUserByUsername(loginDto.getUsername());
        if (currentUserDB.isPresent()) {
            ResLoginDTO.UserLogin userLogin = new ResLoginDTO.UserLogin(
                    currentUserDB.get().getId(),
                    currentUserDB.get().getEmail(),
                    currentUserDB.get().getName()
            );
            res.setUser(userLogin);
        }

        String accessToken = this.securityUtil.createAccessToken(authentication, res.getUser());
        res.setAccessToken(accessToken);

        String refreshToken = this.securityUtil.createRefreshToken(loginDto.getUsername(), res);
        this.userService.updateUserToken(refreshToken, loginDto.getUsername());
        //set cookies
        ResponseCookie resCookies = ResponseCookie.from("refresh_token", refreshToken)
                .httpOnly(true)
                .secure(true)
                .path("/")
                .maxAge(refreshTokenExpiration)
                .build();

        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, resCookies.toString())
                .body(res);
    }

    @GetMapping("/auth/account")
    @ApiMessage("fetch account")
    public ResponseEntity<ResLoginDTO.UserLogin> getAccount() {
        String email = SecurityUtil.getCurrentUserLogin().isPresent()
                ? SecurityUtil.getCurrentUserLogin().get()
                : "";

        Optional<User> optionalUser = this.userService.handleFetchUserByUsername(email);
        ResLoginDTO.UserLogin userLogin = new ResLoginDTO.UserLogin();


        if (optionalUser.isPresent()) {
            User currentUserDB = optionalUser.get();
            userLogin.setId(currentUserDB.getId());
            userLogin.setEmail(currentUserDB.getEmail());
            userLogin.setName(currentUserDB.getName());
        }

        return ResponseEntity.ok().body(userLogin);
    }

    @GetMapping("/auth/refresh")
    @ApiMessage("Get User by refresh token")
    public ResponseEntity<ResLoginDTO> getRefreshToken(
            @CookieValue(name = "refresh_token", defaultValue = "abc") String refresh_token) throws IdInvalidException {
        Jwt decodedToken = this.securityUtil.checkValidRefreshToken(refresh_token);
        String email = decodedToken.getSubject();

        User currentUserDB = this.userService.getUserByRefreshTokenAndEmail(refresh_token, email);
        if (currentUserDB == null) {
            throw new IdInvalidException("Invalid Refresh Token");
        }

        // issue new token/set refresh token as cookies
        ResLoginDTO res = new ResLoginDTO();
        ResLoginDTO.UserLogin userLogin = new ResLoginDTO.UserLogin(
                currentUserDB.getId(),
                currentUserDB.getEmail(),
                currentUserDB.getName());

        res.setUser(userLogin);
        // create access token
        String access_token = this.securityUtil.createAccessToken(email, res.getUser());
        res.setAccessToken(access_token);

        // create refresh token
        String new_refresh_token = this.securityUtil.createRefreshToken(email, res);

        // update user
        this.userService.updateUserToken(new_refresh_token, email);

        // set cookies
        ResponseCookie resCookies = ResponseCookie
                .from("refresh_token", new_refresh_token)
                .httpOnly(true)
                .secure(true)
                .path("/")
                .maxAge(refreshTokenExpiration)
                .build();

        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, resCookies.toString())
                .body(res);
    }

    @GetMapping("/auth/logout")
    @ApiMessage("logout")
    public ResponseEntity<Void> logout() {
        String email = SecurityUtil.getCurrentUserLogin().isPresent()
                ? SecurityUtil.getCurrentUserLogin().get()
                : "";
        Optional<User> optionalUser = this.userService.handleFetchUserByUsername(email);
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            this.userService.updateUserToken(null, user.getEmail());
        }

        //set cookies
        ResponseCookie deleteSpringCookie = ResponseCookie.from("refresh_token")
                .httpOnly(true)
                .secure(true)
                .path("/")
                .maxAge(0)
                .build();

        return ResponseEntity
                .ok()
                .header(HttpHeaders.SET_COOKIE, deleteSpringCookie.toString())
                .build();
    }
}
