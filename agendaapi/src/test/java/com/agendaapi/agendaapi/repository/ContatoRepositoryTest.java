package com.agendaapi.agendaapi.repository;

import com.agendaapi.agendaapi.domain.model.entity.contato.Contato;
import com.agendaapi.agendaapi.domain.model.entity.usuario.Role;
import com.agendaapi.agendaapi.domain.model.entity.usuario.Usuario;
import com.agendaapi.agendaapi.domain.model.enums.RoleName;
import com.agendaapi.agendaapi.domain.repository.ContatoRepository;
import com.agendaapi.agendaapi.domain.repository.RoleRepository;
import com.agendaapi.agendaapi.domain.repository.UsuarioRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@Transactional
public class ContatoRepositoryTest {

    @Autowired
    private ContatoRepository contatoRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private RoleRepository roleRepository;

    private Contato contato;

    @BeforeEach
    void setUp() {
        Role role = roleRepository.findByName(RoleName.ROLE_CUSTOMER)
                .orElseGet(() -> roleRepository.save(new Role(RoleName.ROLE_CUSTOMER)));

        Usuario usuario = new Usuario();
        usuario.setNome("Gabriel");
        usuario.setEmail("gabriel@teste.com");
        usuario.setPassword("senha123");
        usuario.setCriadoEm(LocalDateTime.now());
        usuario.setRole(role);
        usuario = usuarioRepository.save(usuario);

        contato = new Contato();
        contato.setNome("Contato Teste");
        contato.setDataNascimento(LocalDate.of(1990, 1, 1));
        contato.setUsuario(usuario);
        contato = contatoRepository.save(contato);
    }

    @Test
    void shouldSaveAndFindContatoById() {
        Optional<Contato> foundContato = contatoRepository.findById(contato.getId());
        assertThat(foundContato).isPresent();
        assertThat(foundContato.get().getNome()).isEqualTo(contato.getNome());
    }

    @Test
    void shouldUpdateContato() {
        contato.setNome("Nome Atualizado");
        contatoRepository.save(contato);

        Optional<Contato> updatedContato = contatoRepository.findById(contato.getId());
        assertThat(updatedContato).isPresent();
        assertThat(updatedContato.get().getNome()).isEqualTo("Nome Atualizado");
    }

    @Test
    void shouldDeleteContato() {
        contatoRepository.delete(contato);
        Optional<Contato> deletedContato = contatoRepository.findById(contato.getId());
        assertThat(deletedContato).isEmpty();
    }

    @Test
    void shouldReturnEmptyWhenContatoDoesNotExist() {
        Optional<Contato> notFoundContato = contatoRepository.findById(999L);
        assertThat(notFoundContato).isEmpty();
    }
}
