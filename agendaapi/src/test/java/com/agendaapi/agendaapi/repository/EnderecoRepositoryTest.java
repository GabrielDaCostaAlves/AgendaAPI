package com.agendaapi.agendaapi.repository;

import com.agendaapi.agendaapi.domain.model.entity.contato.Contato;
import com.agendaapi.agendaapi.domain.model.entity.contato.Endereco;
import com.agendaapi.agendaapi.domain.model.entity.usuario.Role;
import com.agendaapi.agendaapi.domain.model.entity.usuario.Usuario;
import com.agendaapi.agendaapi.domain.model.enums.RoleName;
import com.agendaapi.agendaapi.domain.repository.ContatoRepository;
import com.agendaapi.agendaapi.domain.repository.EnderecoRepository;
import com.agendaapi.agendaapi.domain.repository.RoleRepository;
import com.agendaapi.agendaapi.domain.repository.UsuarioRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.time.LocalDate;
import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class EnderecoRepositoryTest {

    @Autowired
    private EnderecoRepository enderecoRepository;

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
    void shouldSaveAddress() {
        Endereco endereco = new Endereco();
        endereco.setLogradouro("Rua Teste");
        endereco.setNumero("123");
        endereco.setBairro("Centro");
        endereco.setCidade("Cidade X");
        endereco.setEstado("SP");
        endereco.setCep("12345-678");
        endereco.setContato(contato);

        Endereco savedAddress = enderecoRepository.save(endereco);

        assertThat(savedAddress).isNotNull();
        assertThat(savedAddress.getId()).isNotNull();
        assertThat(savedAddress.getContato()).isEqualTo(contato);
    }

    @Test
    void shouldFindAddressById() {
        Endereco endereco = new Endereco();
        endereco.setLogradouro("Rua Teste");
        endereco.setNumero("123");
        endereco.setBairro("Centro");
        endereco.setCidade("Cidade X");
        endereco.setEstado("SP");
        endereco.setCep("12345-678");
        endereco.setContato(contato);
        endereco = enderecoRepository.save(endereco);

        Endereco foundAddress = enderecoRepository.findById(endereco.getId()).orElse(null);

        assertThat(foundAddress).isNotNull();
        assertThat(foundAddress.getId()).isEqualTo(endereco.getId());
    }

    @Test
    void shouldFindAddressesByContact() {
        Endereco endereco1 = new Endereco();
        endereco1.setLogradouro("Rua 1");
        endereco1.setNumero("100");
        endereco1.setBairro("Bairro A");
        endereco1.setCidade("Cidade Y");
        endereco1.setEstado("RJ");
        endereco1.setCep("54321-678");
        endereco1.setContato(contato);
        enderecoRepository.save(endereco1);

        Endereco endereco2 = new Endereco();
        endereco2.setLogradouro("Rua 2");
        endereco2.setNumero("200");
        endereco2.setBairro("Bairro B");
        endereco2.setCidade("Cidade Z");
        endereco2.setEstado("SP");
        endereco2.setCep("98765-432");
        endereco2.setContato(contato);
        enderecoRepository.save(endereco2);

        Page<Endereco> addresses = enderecoRepository.findByContatoId(contato.getId(), PageRequest.of(0, 10));

        assertThat(addresses).isNotEmpty();
        assertThat(addresses.getTotalElements()).isEqualTo(2);
    }

    @Test
    void shouldDeleteAddress() {
        Endereco endereco = new Endereco();
        endereco.setLogradouro("Rua Teste");
        endereco.setNumero("123");
        endereco.setBairro("Centro");
        endereco.setCidade("Cidade X");
        endereco.setEstado("SP");
        endereco.setCep("12345-678");
        endereco.setContato(contato);
        endereco = enderecoRepository.save(endereco);

        enderecoRepository.delete(endereco);

        assertThat(enderecoRepository.findById(endereco.getId())).isEmpty();
    }
}
