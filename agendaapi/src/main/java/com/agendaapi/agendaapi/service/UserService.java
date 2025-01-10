package com.agendaapi.agendaapi.service;

import com.agendaapi.agendaapi.dto.UpdateEmailDto;
import com.agendaapi.agendaapi.dto.UserDto;
import com.agendaapi.agendaapi.dto.LoginUserDto;
import com.agendaapi.agendaapi.dto.RecoveryJwtTokenDto;
import com.agendaapi.agendaapi.model.Role;
import com.agendaapi.agendaapi.model.Usuario;
import com.agendaapi.agendaapi.repository.RoleRepository;
import com.agendaapi.agendaapi.repository.UsuarioRepository;
import com.agendaapi.agendaapi.security.JwtTokenService;
import com.agendaapi.agendaapi.security.SecurityConfiguration;
import com.agendaapi.agendaapi.security.UserDetailsImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.time.ZoneId;
import java.time.ZonedDateTime;

@Service
public class UserService {

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
                new UsernamePasswordAuthenticationToken(loginUserDto.email(), loginUserDto.password());

        // Autentica o usuário com as credenciais fornecidas
        Authentication authentication = authenticationManager.authenticate(usernamePasswordAuthenticationToken);

        // Obtém o objeto UserDetails do usuário autenticado
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

        // Gera um token JWT para o usuário autenticado
        return new RecoveryJwtTokenDto(jwtTokenService.generateToken(userDetails));
    }

    public void createUser(UserDto userDto) {
        // Verifica qual Role deve ser associada ao usuário

        Role role = roleRepository.findByName(userDto.role())
                .orElseThrow(() -> new RuntimeException("Role não encontrada"));

        // Cria um novo usuário com os dados fornecidos e a role associada
        Usuario newUser = new Usuario(
                userDto.nome(),
                userDto.email(),
                securityConfiguration.passwordEncoder().encode(userDto.password()),
                role // Aqui associamos a role com o ID correto
        );

        newUser.setCriadoEm(ZonedDateTime.now(ZoneId.of("America/Sao_Paulo")));

        // Salva o novo usuário no banco de dados
        usuarioRepository.save(newUser);
    }


    public void updateUser(UserDto updateUserDto){


        Usuario existingUser = usuarioRepository.findByEmail(updateUserDto.email())
                .orElseThrow(()-> new RuntimeException("Usuario não encontrado"));



        // Atualize as informações do usuário
        if (updateUserDto.nome() != null) {
            existingUser.setNome(updateUserDto.nome());
        }
        if (updateUserDto.email() != null) {
            existingUser.setEmail(updateUserDto.email());
        }
        if (updateUserDto.password() != null) {
            existingUser.setPassword(securityConfiguration.passwordEncoder().encode(updateUserDto.password()));
        }
        if (updateUserDto.role() != null) {
            Role role = roleRepository.findByName(updateUserDto.role())
                    .orElseThrow(() -> new RuntimeException("Role não encontrada"));
            existingUser.setRole(role);
        }

        // Salva o novo usuário no banco de dados
        usuarioRepository.save(existingUser);
    }


    public RecoveryJwtTokenDto updateEmail(UpdateEmailDto updateEmailDto) {

        // Busca o usuário pelo e-mail atual
        Usuario existingUser = usuarioRepository.findByEmail(updateEmailDto.email())
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

        // Atualiza o e-mail, se for fornecido
        if (updateEmailDto.newemail() != null) {
            existingUser.setEmail(updateEmailDto.newemail());
        }

        // Salva o usuário com o novo e-mail
        usuarioRepository.save(existingUser);

        // Cria um UserDetailsImpl atualizado com o novo e-mail
        UserDetailsImpl userDetails = new UserDetailsImpl(existingUser);

        // Gera um novo token JWT com o e-mail atualizado
        String newJwtToken = jwtTokenService.generateToken(userDetails);

        // Retorna o novo token no DTO
        return new RecoveryJwtTokenDto(newJwtToken);
    }
}