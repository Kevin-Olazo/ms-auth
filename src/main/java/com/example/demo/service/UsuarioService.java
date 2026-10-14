package com.example.demo.service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.demo.dto.UsuarioRegistroDto;
import com.example.demo.model.Rol;
import com.example.demo.model.Usuario;
import com.example.demo.repository.RolRepository;
import com.example.demo.repository.UsuarioRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;
    private final RolRepository rolRepository;

    public Usuario getUsuarioByNomusuario(String nomusuario) {
        return usuarioRepository.findByNomusuario(nomusuario);
    }

    public List<Rol> listarRoles() {
        return rolRepository.findAll();
    }

    @Transactional
    public Usuario crearUsuarioConRoles(UsuarioRegistroDto dto) {
        if (dto.getNomusuario() == null || dto.getNomusuario().isBlank()
                || dto.getPassword() == null || dto.getPassword().isBlank()) {
            throw new RuntimeException("El usuario y el password son obligatorios.");
        }
        if (usuarioRepository.existsByNomusuario(dto.getNomusuario())) {
            throw new RuntimeException("El nombre de usuario ya está en uso.");
        }
        if (usuarioRepository.existsByEmail(dto.getEmail())) {
            throw new RuntimeException("El correo electrónico ya está en uso.");
        }
        Set<Rol> rolesAsignados = new HashSet<>();
        if (dto.getRolesIds() != null && !dto.getRolesIds().isEmpty()) {
            List<Rol> rolesEncontrados = rolRepository.findAllById(dto.getRolesIds());
            if (rolesEncontrados.size() != dto.getRolesIds().size()) {
                throw new RuntimeException("Uno o más roles proporcionados no existen en la base de datos.");
            }
            rolesAsignados.addAll(rolesEncontrados);
        }
        Usuario nuevoUsuario = new Usuario();
        nuevoUsuario.setNomusuario(dto.getNomusuario());
        nuevoUsuario.setEmail(dto.getEmail());
        nuevoUsuario.setPassword(passwordEncoder.encode(dto.getPassword()));
        nuevoUsuario.setNombres(dto.getNombres());
        nuevoUsuario.setApellidos(dto.getApellidos());
        nuevoUsuario.setActivo(true);
        nuevoUsuario.setRoles(rolesAsignados);
        return usuarioRepository.save(nuevoUsuario);
    }
}
