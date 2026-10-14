package com.example.demo.dto;

import java.util.List;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class UsuarioJwt {
    private Integer idusuario;
    private String nomusuario;
    private List<String> roles;
    private String token;
}
