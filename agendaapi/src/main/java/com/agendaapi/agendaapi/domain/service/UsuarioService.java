package com.agendaapi.agendaapi.domain.service;

import com.agendaapi.agendaapi.dto.usuariodto.UsuarioDto;
import com.agendaapi.agendaapi.dto.usuariodto.LoginUserDto;
import com.agendaapi.agendaapi.dto.usuariodto.RecoveryJwtTokenDto;
import com.agendaapi.agendaapi.domain.model.entity.usuario.Role;
import com.agendaapi.agendaapi.domain.model.entity.usuario.Usuario;
import com.agendaapi.agendaapi.domain.repository.RoleRepository;
import com.agendaapi.agendaapi.domain.repository.UsuarioRepository;
import com.agendaapi.agendaapi.security.JwtTokenService;
import com.agendaapi.agendaapi.config.SecurityConfiguration;
import com.agendaapi.agendaapi.security.UserDetailsImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.lang.reflect.InvocationTargetException;
import java.time.LocalDateTime;
import java.time.ZoneId;

@Service
public class UsuarioService {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private JwtTokenService jwtTokenService;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private SecurityConfiguration securityConfiguration;

    public Usuario getUsuarioById(Long userId) {
        return usuarioRepository.findById(userId).orElse(null);
    }

    public RecoveryJwtTokenDto authenticateUser(LoginUserDto loginUserDto) {

        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken =
                new UsernamePasswordAuthenticationToken(loginUserDto.getEmail(), loginUserDto.getPassword());


        Authentication authentication = authenticationManager.authenticate(usernamePasswordAuthenticationToken);


        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

        return new RecoveryJwtTokenDto(jwtTokenService.generateToken(userDetails));
    }

    public Usuario createUser(UsuarioDto usuarioDto) {

        if (usuarioDto ==null) {
            throw new IllegalArgumentException("Usuário, ID do contato ou Json não podem ser nulos");
        }

        Role role = roleRepository.findByName(usuarioDto.role())
                .orElseThrow(() -> new RuntimeException("Role não encontrada"));


        Usuario newUser = new Usuario(usuarioDto.nome(), usuarioDto.email(), securityConfiguration
                .passwordEncoder().encode(usuarioDto.password()), role
        );

        newUser.setCriadoEm(LocalDateTime.now(ZoneId.of("America/Sao_Paulo")));
        try{
            return usuarioRepository.save(newUser);
        } catch (RuntimeException e) {
            if (e.getMessage().contains("Duplicate entry") && e.getMessage().contains("for key 'usuario.email'")) {
                throw new RuntimeException("O e-mail fornecido já está em uso. Por favor, use um e-mail diferente.");
            }

            throw new RuntimeException("Ocorreu um erro ao tentar criar o usuário: " + e.getMessage());
        }


    }


    public Usuario updateUser(UsuarioDto updateUsuarioDto, Usuario userSignedIn) throws NoSuchFieldException, InvocationTargetException, IllegalAccessException, InstantiationException, NoSuchMethodException {

        Usuario existingUser = usuarioRepository.findByEmail(userSignedIn.getEmail())
                .orElseThrow(() -> new RuntimeException("Usuario não encontrado"));


        if (updateUsuarioDto.nome() != null) {
            existingUser.setNome(updateUsuarioDto.nome());
        }

        if (updateUsuarioDto.password() != null) {
            existingUser.setPassword(securityConfiguration.passwordEncoder().encode(updateUsuarioDto.password()));
        }
        if (updateUsuarioDto.role() != null) {
            Role role = roleRepository.findByName(updateUsuarioDto.role()).orElseThrow(() -> new RuntimeException("Role não encontrada"));
            existingUser.setRole(role);
        }

        if (updateUsuarioDto.email() != null) {
            existingUser.setEmail(updateUsuarioDto.email());
        }

        try {

            return usuarioRepository.save(existingUser);
        } catch (RuntimeException e) {
            throw new RuntimeException("Ocorreu um erro ao tentar atualizar o usuario, erro: " +e);
        }
    }
    
    public Usuario getContatoByToken(String token) {

        String tokenAbstract = token.replace("Bearer ", "");

        String email = jwtTokenService.getSubjectFromToken(tokenAbstract);



        return usuarioRepository.findByEmail(email).orElseThrow(() ->
                new RuntimeException("Usuário não encontrado"));

    }

    public void deleteUser(Usuario userSignedIn) {

        try {
            usuarioRepository.delete(userSignedIn);
        } catch (RuntimeException e) {
            throw new RuntimeException("Ocorreu um erro ao tentar deletar o usuario, erro: " +e);
        }


    }

    public Page<Usuario> getAllUsers(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return usuarioRepository.findAll(pageable);
    }


}