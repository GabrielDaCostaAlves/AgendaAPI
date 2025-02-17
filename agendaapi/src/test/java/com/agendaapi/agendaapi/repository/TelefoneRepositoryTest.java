package com.agendaapi.agendaapi.repository;

import com.agendaapi.agendaapi.domain.model.entity.contato.Contato;
import com.agendaapi.agendaapi.domain.model.entity.usuario.Role;
import com.agendaapi.agendaapi.domain.model.entity.contato.Telefone;
import com.agendaapi.agendaapi.domain.model.entity.usuario.Usuario;
import com.agendaapi.agendaapi.domain.model.enums.RoleName;
import com.agendaapi.agendaapi.domain.repository.ContatoRepository;
import com.agendaapi.agendaapi.domain.repository.RoleRepository;
import com.agendaapi.agendaapi.domain.repository.TelefoneRepository;
import com.agendaapi.agendaapi.domain.repository.UsuarioRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class TelefoneRepositoryTest {

    @Autowired
    private TelefoneRepository telefoneRepository;

    @Autowired
    private ContatoRepository contatoRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private RoleRepository roleRepository;

    private Usuario usuario;
    private Contato contato;

    @BeforeEach
    void setUp() {
        Role role = roleRepository.findByName(RoleName.ROLE_CUSTOMER)
                .orElseGet(() -> roleRepository.save(new Role(RoleName.ROLE_CUSTOMER)));

        usuario = new Usuario();
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
    void shouldSavePhoneCorrectly() {
        Telefone telefone = new Telefone();
        telefone.setNumero("11987654321");
        telefone.setTipo("Celular");
        telefone.setContato(contato);

        Telefone telefoneSalvo = telefoneRepository.save(telefone);

        assertThat(telefoneSalvo).isNotNull();
        assertThat(telefoneSalvo.getId()).isNotNull();
        assertThat(telefoneSalvo.getNumero()).isEqualTo("11987654321");
        assertThat(telefoneSalvo.getContato().getId()).isEqualTo(contato.getId());
    }

    @Test
    void shouldFindPhoneById() {
        Telefone telefone = new Telefone();
        telefone.setNumero("11987654321");
        telefone.setTipo("Celular");
        telefone.setContato(contato);
        telefone = telefoneRepository.save(telefone);

        Optional<Telefone> telefoneBuscado = telefoneRepository.findById(telefone.getId());

        assertThat(telefoneBuscado).isPresent();
        assertThat(telefoneBuscado.get().getNumero()).isEqualTo("11987654321");
    }

    @Test
    void shouldFindPhonesOfAContactWithPagination() {
        Telefone telefone1 = new Telefone();
        telefone1.setNumero("11987654321");
        telefone1.setTipo("Celular");
        telefone1.setContato(contato);
        telefoneRepository.save(telefone1);

        Telefone telefone2 = new Telefone();
        telefone2.setNumero("11912345678");
        telefone2.setTipo("Residencial");
        telefone2.setContato(contato);
        telefoneRepository.save(telefone2);

        Pageable pageable = PageRequest.of(0, 10);
        Page<Telefone> telefones = telefoneRepository.findByContatoId(contato.getId(), pageable);

        assertThat(telefones.getContent()).hasSize(2);
    }

    @Test
    void shouldReturnEmptyPageIfContactHasNoPhones() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<Telefone> telefones = telefoneRepository.findByContatoId(contato.getId(), pageable);

        assertThat(telefones.getContent()).isEmpty();
    }

    @Test
    void shouldReturnOnlyCorrectSizeOfPhonesPerPage() {
        for (int i = 0; i < 15; i++) {
            Telefone telefone = new Telefone();
            telefone.setNumero("1198765432" + i);
            telefone.setTipo("Celular");
            telefone.setContato(contato);
            telefoneRepository.save(telefone);
        }

        Pageable pageable = PageRequest.of(0, 10);
        Page<Telefone> telefones = telefoneRepository.findByContatoId(contato.getId(), pageable);

        assertThat(telefones.getContent()).hasSize(10);
        assertThat(telefones.getTotalElements()).isEqualTo(15);
    }

    @Test
    void shouldDeletePhone() {
        Telefone telefone = new Telefone();
        telefone.setNumero("11987654321");
        telefone.setTipo("Celular");
        telefone.setContato(contato);
        telefone = telefoneRepository.save(telefone);

        telefoneRepository.delete(telefone);
        Optional<Telefone> telefoneExcluido = telefoneRepository.findById(telefone.getId());

        assertThat(telefoneExcluido).isEmpty();
    }
    @Test
    void shouldUpdatePhoneNumber() {
        Telefone telefone = new Telefone();
        telefone.setNumero("11999998888");
        telefone.setTipo("Celular");
        telefone.setContato(contato);
        telefone = telefoneRepository.save(telefone);
        assertThat(telefoneRepository.findById(telefone.getId()).get().getNumero()).isEqualTo("11999998888");
    }
}
