package com.evsyukoov.project.controller;

import com.evsyukoov.project.jwt.JwtUtils;
import com.evsyukoov.project.model.rest.request.AuthRequestParams;
import com.evsyukoov.project.service.UserService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class AuthController {

    UserService userService;

    AuthenticationManager authenticationManager;

    @Autowired
    public void setAuthenticationManager(AuthenticationManager authenticationManager) {
        this.authenticationManager = authenticationManager;
    }

    @Autowired
    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/service/bank/v1/auth")
    @ApiOperation(value = "Получение токена для работы с API", response = String.class)
    public ResponseEntity<?> authUser(@RequestBody AuthRequestParams authReq) {
        Authentication authentication = authenticationManager
                .authenticate(new UsernamePasswordAuthenticationToken(
                        authReq.getName(),
                        authReq.getPass()));

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = JwtUtils.generateJwtToken(authReq.getName());
        return ResponseEntity.ok(jwt);
    }
}
