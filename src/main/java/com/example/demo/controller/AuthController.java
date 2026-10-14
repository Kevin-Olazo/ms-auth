package com.example.demo.controller;

import java.time.LocalDate;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.dto.ErrorMessage;
import com.example.demo.dto.GenericResponse;
import com.example.demo.dto.Login;
import com.example.demo.dto.UsuarioJwt;
import com.example.demo.model.Rol;
import com.example.demo.model.Usuario;
import com.example.demo.security.JwtService;
import com.example.demo.service.DetalleUsuarioService;
import com.example.demo.service.UsuarioService;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {

    private final DetalleUsuarioService detalleUsuarioService;
    private final JwtService jwtService;
    private final AuthenticationManager authManager;
    private final UsuarioService usuarioService;

    //localhost:8090/api/v1/auth/login
    @PostMapping("/login")
    public ResponseEntity<GenericResponse<UsuarioJwt>> login(@RequestBody Login login) {
        GenericResponse<UsuarioJwt> response;
        try {
            authManager.authenticate(new UsernamePasswordAuthenticationToken(
                    login.getUsername(), login.getPassword()));
            Usuario usuario = usuarioService.getUsuarioByNomusuario(login.getUsername());
            String token = jwtService.generarToken(usuario,
                    detalleUsuarioService.getAuthorities(usuario.getRoles()));
            response = GenericResponse.<UsuarioJwt>builder()
                    .response(UsuarioJwt.builder()
                            .idusuario(usuario.getIdusuario())
                            .nomusuario(usuario.getNomusuario())
                            .roles(usuario.getRoles().stream().map(Rol::getNomrol).toList())
                            .token(token).build())
                    .build();
            return ResponseEntity.ok(response);
        } catch (AuthenticationException e) {
            response = GenericResponse.<UsuarioJwt>builder()
                    .error(ErrorMessage.builder()
                            .statusCode(HttpStatus.UNAUTHORIZED.value())
                            .dateError(LocalDate.now())
                            .message("Usuario y/o password incorrecto")
                            .build())
                    .build();
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
        }
    }
}
