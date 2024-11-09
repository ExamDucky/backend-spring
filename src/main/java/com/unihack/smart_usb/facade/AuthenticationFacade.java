package com.unihack.smart_usb.facade;

import com.unihack.smart_usb.api.dto.LoginRequestDTO;
import com.unihack.smart_usb.api.dto.LoginResponseDTO;
import com.unihack.smart_usb.api.dto.RegisterRequestDTO;
import com.unihack.smart_usb.api.dto.RegisterResponseDTO;
import com.unihack.smart_usb.exception.auth.ProfessorAlreadyExistsException;
import com.unihack.smart_usb.persistance.model.Professor;
import com.unihack.smart_usb.service.implementations.JwtService;
import com.unihack.smart_usb.service.implementations.ProfessorService;
import jakarta.transaction.Transactional;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthenticationFacade {

    private final ProfessorService professorService;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;


    @Transactional
    public RegisterResponseDTO register(@NotNull RegisterRequestDTO request) {
        Optional<Professor> existingProfessor = professorService.getProfessorByEmail(request.getEmail());

        if (existingProfessor.isPresent()) {
            throw new ProfessorAlreadyExistsException("User already exists");
        } else {
            var newProfessor = Professor.builder()
                    .firstName(request.getFirstName())
                    .lastName(request.getLastName())
                    .email(request.getEmail())
                    .password(passwordEncoder.encode(request.getPassword()))
                    .build();
            Professor savedProfessor = professorService.saveProfessor(newProfessor);
            var jwtToken = jwtService.generateToken(savedProfessor);
            return RegisterResponseDTO.builder()
                    .token(jwtToken)
                    .build();
        }
    }

    public LoginResponseDTO authenticate(LoginRequestDTO request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );
        var user = professorService.getProfessorByEmail(request.getEmail())
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
        var jwtToken = jwtService.generateToken(user);
        return LoginResponseDTO.builder()
                .token(jwtToken)
                .build();
    }

}
