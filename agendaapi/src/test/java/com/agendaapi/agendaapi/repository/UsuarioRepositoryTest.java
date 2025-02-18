package com.agendaapi.agendaapi.repository;

import com.agendaapi.agendaapi.domain.model.entity.usuario.Role;
import com.agendaapi.agendaapi.domain.model.entity.usuario.Usuario;
import com.agendaapi.agendaapi.domain.model.enums.RoleName;
import com.agendaapi.agendaapi.domain.repository.RoleRepository;
import com.agendaapi.agendaapi.domain.repository.UsuarioRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DataJpaTest
@ActiveProfiles("test")
public class UsuarioRepositoryTest {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private RoleRepository roleRepository;

    private Role role;
    private Usuario usuario;

    @BeforeEach
    void setUp() {
        role = new Role();
        role.setName(RoleName.ROLE_CUSTOMER);
        role = roleRepository.save(role);

        usuario = new Usuario();
        usuario.setNome("Gabriel");
        usuario.setEmail("gabriel@teste.com");
        usuario.setPassword("senha123");
        usuario.setRole(role);
    }

    @Test
    void salvarUsuarioNoBancoDeDadosTest() {
        Usuario savedUsuario = usuarioRepository.save(usuario);
        assertThat(savedUsuario).isNotNull();
        assertThat(savedUsuario.getId()).isGreaterThan(0);
        assertThat(savedUsuario.getEmail()).isEqualTo("gabriel@teste.com");
    }

    @Test
    void buscarUsuarioPorEmailTest() {
        usuarioRepository.save(usuario);
        Usuario foundUsuario = usuarioRepository.findByEmail("gabriel@teste.com").orElse(null);
        assertThat(foundUsuario).isNotNull();
    }

    @Test
    void deveSalvarUsuarioComEmailDuplicadoTest() {
        usuarioRepository.save(usuario);
        Usuario outroUsuario = new Usuario();
        outroUsuario.setNome("João");
        outroUsuario.setEmail("gabriel@teste.com");
        outroUsuario.setPassword("outrasenha123");
        outroUsuario.setRole(role);
        assertThatThrownBy(() -> usuarioRepository.save(outroUsuario))
                .isInstanceOf(DataIntegrityViolationException.class);
    }

    @Test
    void excluirUsuarioTest() {
        Usuario savedUsuario = usuarioRepository.save(usuario);
        usuarioRepository.deleteById(savedUsuario.getId());
        assertThat(usuarioRepository.findById(savedUsuario.getId())).isEmpty();
    }

    @Test
    void atualizarUsuarioTest() {
        Usuario savedUsuario = usuarioRepository.save(usuario);
        savedUsuario.setNome("Gabriel Updated");
        Usuario updatedUsuario = usuarioRepository.save(savedUsuario);
        assertThat(updatedUsuario.getNome()).isEqualTo("Gabriel Updated");
    }
}