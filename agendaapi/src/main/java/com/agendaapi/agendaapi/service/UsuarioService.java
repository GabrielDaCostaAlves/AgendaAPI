package com.agendaapi.agendaapi.service;

import com.agendaapi.agendaapi.dto.usuariodto.UsuarioDto;
import com.agendaapi.agendaapi.dto.usuariodto.LoginUserDto;
import com.agendaapi.agendaapi.dto.usuariodto.RecoveryJwtTokenDto;
import com.agendaapi.agendaapi.model.Role;
import com.agendaapi.agendaapi.model.Usuario;
import com.agendaapi.agendaapi.repository.RoleRepository;
import com.agendaapi.agendaapi.repository.UsuarioRepository;
import com.agendaapi.agendaapi.security.JwtTokenService;
import com.agendaapi.agendaapi.security.SecurityConfiguration;
import com.agendaapi.agendaapi.security.UserDetailsImpl;
import com.agendaapi.agendaapi.util.conversor.ConverterClass;
import com.agendaapi.agendaapi.vo.LoginVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.lang.reflect.InvocationTargetException;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Objects;

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


    // Metodo responsável por autenticar um usuário e retornar um token JWT
    public RecoveryJwtTokenDto authenticateUser(LoginUserDto loginUserDto) {
        // Cria um objeto de autenticação com o email e a senha do usuário
        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken =
                new UsernamePasswordAuthenticationToken(loginUserDto.getEmail(), loginUserDto.getPassword());

        // Autentica o usuário com as credenciais fornecidas
        Authentication authentication = authenticationManager.authenticate(usernamePasswordAuthenticationToken);

        // Obtém o objeto UserDetails do usuário autenticado
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

        // Gera um token JWT para o usuário autenticado
        return new RecoveryJwtTokenDto(jwtTokenService.generateToken(userDetails));
    }

    public Usuario createUser(UsuarioDto usuarioDto) {
        // Verifica qual Role deve ser associada ao usuário
        if (usuarioDto ==null) {
            throw new IllegalArgumentException("Usuário, ID do contato ou Json não podem ser nulos");
        }

        Role role = roleRepository.findByName(usuarioDto.role())
                .orElseThrow(() -> new RuntimeException("Role não encontrada"));

        // Cria um novo usuário com os dados fornecidos e a role associada
        Usuario newUser = new Usuario(usuarioDto.nome(), usuarioDto.email(), securityConfiguration
                .passwordEncoder().encode(usuarioDto.password()), role // Aqui associamos a role com o ID correto
        );

        newUser.setCriadoEm(ZonedDateTime.now(ZoneId.of("America/Sao_Paulo")));

        // Salva o novo usuário no banco de dados
        try{
            return usuarioRepository.save(newUser);
        } catch (RuntimeException e) {
            if (e.getMessage().contains("Duplicate entry") && e.getMessage().contains("for key 'usuario.email'")) {
                throw new RuntimeException("O e-mail fornecido já está em uso. Por favor, use um e-mail diferente.");
            }
            // Caso não seja o erro de duplicação, lança o erro original
            throw new RuntimeException("Ocorreu um erro ao tentar criar o usuário: " + e.getMessage());
        }


    }


    public Usuario updateUser(UsuarioDto updateUsuarioDto, Usuario userSignedIn) throws NoSuchFieldException, InvocationTargetException, IllegalAccessException, InstantiationException, NoSuchMethodException {

        Usuario existingUser = usuarioRepository.findByEmail(userSignedIn.getEmail())
                .orElseThrow(() -> new RuntimeException("Usuario não encontrado"));

        // Atualiza as informações do usuário
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
            // Salva o novo usuário no banco de dados
            return usuarioRepository.save(existingUser);
        } catch (RuntimeException e) {
            throw new RuntimeException("Ocorreu um erro ao tentar atualizar o usuario, erro: " +e);
        }
    }
    
    public Usuario getContatoByToken(String token) {
        // Remover "Bearer " do token
        String tokenAbstract = token.replace("Bearer ", "");
        // Decodificar o token e extrair o email do usuário
        String email = jwtTokenService.getSubjectFromToken(tokenAbstract);


        // Buscar o usuário no banco de dados
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



}