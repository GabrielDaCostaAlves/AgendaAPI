package com.agendaapi.agendaapi.service;

import com.agendaapi.agendaapi.domain.service.UsuarioService;
import com.agendaapi.agendaapi.dto.usuariodto.UsuarioDto;
import com.agendaapi.agendaapi.domain.model.entity.usuario.Role;
import com.agendaapi.agendaapi.domain.model.entity.usuario.Usuario;
import com.agendaapi.agendaapi.domain.model.enums.RoleName;
import com.agendaapi.agendaapi.domain.repository.RoleRepository;
import com.agendaapi.agendaapi.domain.repository.UsuarioRepository;
import com.agendaapi.agendaapi.config.SecurityConfiguration;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.lang.reflect.InvocationTargetException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UsuarioServiceTest {

    @InjectMocks
    private UsuarioService usuarioService;

    @Mock
    private UsuarioRepository usuarioRepository;

    @Mock
    private RoleRepository roleRepository;

    @Mock
    private SecurityConfiguration securityConfiguration;

    @Mock
    private PasswordEncoder passwordEncoder;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        when(securityConfiguration.passwordEncoder()).thenReturn(passwordEncoder);
    }

    @Test
    void criarUsuarioTest() {

        Role role = new Role();
        role.setName(RoleName.ROLE_CUSTOMER);

        UsuarioDto usuarioDto = new UsuarioDto("Nome", "email@teste.com", "password", RoleName.ROLE_CUSTOMER);
        when(roleRepository.findByName(usuarioDto.role())).thenReturn(Optional.of(role));
        when(usuarioRepository.save(any(Usuario.class))).thenReturn(new Usuario());
        when(passwordEncoder.encode(anyString())).thenReturn("encodedPassword");


        Usuario createdUser = usuarioService.createUser(usuarioDto);


        assertNotNull(createdUser);
        verify(usuarioRepository, times(1)).save(any(Usuario.class));
        verify(passwordEncoder, times(1)).encode("password");
    }

    @Test
    void retornarUsuarioPorIdTest() {

        Long userId = 1L;
        Usuario usuario = new Usuario();
        when(usuarioRepository.findById(userId)).thenReturn(Optional.of(usuario));


        Usuario foundUser = usuarioService.getUsuarioById(userId);


        assertNotNull(foundUser);
    }

    @Test
    void atualizarUsuarioTest() throws NoSuchFieldException, InvocationTargetException, IllegalAccessException, InstantiationException, NoSuchMethodException {

        Usuario usuarioExistente = new Usuario();
        usuarioExistente.setEmail("email@teste.com");
        UsuarioDto updateUsuarioDto = new UsuarioDto("Updated Nome", "updatedEmail@teste.com", "newPassword", RoleName.ROLE_ADMINISTRATOR);

        when(usuarioRepository.findByEmail(usuarioExistente.getEmail())).thenReturn(Optional.of(usuarioExistente));
        when(roleRepository.findByName(updateUsuarioDto.role())).thenReturn(Optional.of(new Role(RoleName.ROLE_ADMINISTRATOR)));
        when(usuarioRepository.save(any(Usuario.class))).thenReturn(usuarioExistente);
        when(passwordEncoder.encode(anyString())).thenReturn("encodedPassword");


        Usuario updatedUser = usuarioService.updateUser(updateUsuarioDto, usuarioExistente);


        assertNotNull(updatedUser);
        assertEquals("Updated Nome", updatedUser.getNome());
        verify(passwordEncoder, times(1)).encode("newPassword");
    }

    @Test
    void deletarUsuarioTest() {

        Usuario usuario = new Usuario();
        doNothing().when(usuarioRepository).delete(usuario);


        usuarioService.deleteUser(usuario);


        verify(usuarioRepository, times(1)).delete(usuario);
    }
}
