package com.example.demo.dto;

import java.util.List;

import lombok.Data;

@Data
public class UsuarioRegistroDto {
    private String nomusuario;
    private String email;
    private String password;
    private String nombres;
    private String apellidos;
    private List<Integer> rolesIds;
}
