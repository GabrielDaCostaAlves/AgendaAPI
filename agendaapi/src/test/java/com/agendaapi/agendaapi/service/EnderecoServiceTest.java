package com.agendaapi.agendaapi.service;

import com.agendaapi.agendaapi.domain.service.EnderecoService;
import com.agendaapi.agendaapi.dto.contatodto.EnderecoDto;
import com.agendaapi.agendaapi.domain.model.entity.contato.Contato;
import com.agendaapi.agendaapi.domain.model.entity.contato.Endereco;
import com.agendaapi.agendaapi.domain.model.entity.usuario.Usuario;
import com.agendaapi.agendaapi.domain.repository.ContatoRepository;
import com.agendaapi.agendaapi.domain.repository.EnderecoRepository;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EnderecoServiceTest {

    @InjectMocks
    private EnderecoService enderecoService;

    @Mock
    private EnderecoRepository enderecoRepository;

    @Mock
    private ContatoRepository contatoRepository;

    private Usuario usuario;
    private Contato contato;
    private Endereco endereco;
    private EnderecoDto enderecoDto;

    @BeforeEach
    void setUp() {
        usuario = new Usuario();
        usuario.setEmail("usuario@email.com");

        contato = new Contato();
        contato.setId(1L);
        contato.setUsuario(usuario);

        endereco = new Endereco();
        endereco.setId(1L);
        endereco.setContato(contato);
        endereco.setLogradouro("Rua Teste");

        enderecoDto = new EnderecoDto("Rua Nova", "100", "Apto 1", "Bairro X", "Cidade Y", "Estado Z", "12345-678");
    }

    @Test
    void deveCriarEnderecoComSucesso() {
        when(contatoRepository.findById(1L)).thenReturn(Optional.of(contato));
        when(enderecoRepository.save(any(Endereco.class))).thenReturn(endereco);

        Endereco enderecoCriado = enderecoService.createEndereco(usuario, enderecoDto, 1L);

        assertNotNull(enderecoCriado);
        verify(enderecoRepository).save(any(Endereco.class));
    }

    @Test
    void deveLancarExcecaoAoCriarEnderecoComContatoInexistente() {
        when(contatoRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> enderecoService.createEndereco(usuario, enderecoDto, 1L));
    }

    @Test
    void deveAtualizarEnderecoComSucesso() {
        when(enderecoRepository.findById(1L)).thenReturn(Optional.of(endereco));
        when(enderecoRepository.save(any(Endereco.class))).thenReturn(endereco);

        Endereco enderecoAtualizado = enderecoService.updateEndereco(usuario, 1L, enderecoDto);

        assertNotNull(enderecoAtualizado);
        assertEquals("Rua Nova", enderecoAtualizado.getLogradouro());
        verify(enderecoRepository).save(any(Endereco.class));
    }

    @Test
    void deveLancarExcecaoAoAtualizarEnderecoInexistente() {
        when(enderecoRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> enderecoService.updateEndereco(usuario, 1L, enderecoDto));
    }

    @Test
    void deveDeletarEnderecoComSucesso() {
        when(enderecoRepository.findById(1L)).thenReturn(Optional.of(endereco));
        doNothing().when(enderecoRepository).delete(endereco);

        assertDoesNotThrow(() -> enderecoService.deleteEndereco(usuario, 1L));
        verify(enderecoRepository).delete(endereco);
    }

    @Test
    void deveLancarExcecaoAoDeletarEnderecoInexistente() {
        when(enderecoRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> enderecoService.deleteEndereco(usuario, 1L));
    }

    @Test
    void deveRetornarEnderecoPorId() {
        when(enderecoRepository.findById(1L)).thenReturn(Optional.of(endereco));

        Endereco enderecoRetornado = enderecoService.getEnderecoById(usuario, 1L);

        assertNotNull(enderecoRetornado);
        assertEquals(1L, enderecoRetornado.getId());
    }

    @Test
    void deveLancarExcecaoQuandoEnderecoNaoEncontrado() {
        when(enderecoRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> enderecoService.getEnderecoById(usuario, 1L));
    }

    @Test
    void deveRetornarEnderecosPorContato() {
        Page<Endereco> page = new PageImpl<>(List.of(endereco));
        when(contatoRepository.findById(1L)).thenReturn(Optional.of(contato));
        when(enderecoRepository.findByContatoId(eq(1L), any(Pageable.class))).thenReturn(page);

        Page<Endereco> enderecosRetornados = enderecoService.getEnderecosByContato(1L, usuario, 0, 5);

        assertFalse(enderecosRetornados.isEmpty());
        verify(enderecoRepository).findByContatoId(eq(1L), any(Pageable.class));
    }
}
