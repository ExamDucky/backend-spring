package com.unihack.smart_usb.api.controller;


import com.unihack.smart_usb.api.dto.LoginRequestDTO;
import com.unihack.smart_usb.api.dto.LoginResponseDTO;
import com.unihack.smart_usb.api.dto.RegisterRequestDTO;
import com.unihack.smart_usb.api.dto.RegisterResponseDTO;
import com.unihack.smart_usb.facade.AuthenticationFacade;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@Slf4j
public class AuthController {

    private final AuthenticationFacade authenticationFacade;

    @Operation(summary = "Register professor to system", description = "Method for creating new professor and pass token in response")
    @PostMapping("/register")
    public ResponseEntity<RegisterResponseDTO> register(
            @RequestBody RegisterRequestDTO request
    ){
        return ResponseEntity.ok(authenticationFacade.register(request));
    }

    @Operation(summary = "Login professor", description = "Method for login professor that pass token in response")
    @PostMapping("/login")
    public ResponseEntity<LoginResponseDTO> login(
            @RequestBody LoginRequestDTO request
    ){
        return ResponseEntity.ok(authenticationFacade.authenticate(request));
    }
}
