package com.agendaapi.agendaapi.service;

import com.agendaapi.agendaapi.domain.service.TelefoneService;
import com.agendaapi.agendaapi.dto.contatodto.TelefoneDto;
import com.agendaapi.agendaapi.domain.model.entity.contato.Contato;
import com.agendaapi.agendaapi.domain.model.entity.contato.Telefone;
import com.agendaapi.agendaapi.domain.model.entity.usuario.Usuario;
import com.agendaapi.agendaapi.domain.repository.TelefoneRepository;
import com.agendaapi.agendaapi.domain.repository.ContatoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TelefoneServiceTest {

    @InjectMocks
    private TelefoneService telefoneService;

    @Mock
    private TelefoneRepository telefoneRepository;

    @Mock
    private ContatoRepository contatoRepository;

    private Usuario usuario;
    private Contato contato;
    private Telefone telefone;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        usuario = new Usuario();
        usuario.setEmail("teste@teste.com");

        contato = new Contato();
        contato.setId(1L);
        contato.setUsuario(usuario);

        telefone = new Telefone();
        telefone.setId(1L);
        telefone.setNumero("999999999");
        telefone.setContato(contato);
    }

    @Test
    void testCreateTelefone() {
        TelefoneDto telefoneDto = new TelefoneDto("999999999", "MOBILE");
        when(contatoRepository.findById(1L)).thenReturn(Optional.of(contato));
        when(telefoneRepository.save(any(Telefone.class))).thenReturn(telefone);

        Telefone createdTelefone = telefoneService.createTelefone(usuario, telefoneDto, 1L);

        assertNotNull(createdTelefone);
        assertEquals("999999999", createdTelefone.getNumero());
        verify(telefoneRepository, times(1)).save(any(Telefone.class));
    }

    @Test
    void testUpdateTelefone() {
        TelefoneDto telefoneDto = new TelefoneDto("888888888", "HOME");
        when(telefoneRepository.findById(1L)).thenReturn(Optional.of(telefone));
        when(telefoneRepository.save(any(Telefone.class))).thenReturn(telefone);

        Telefone updatedTelefone = telefoneService.updateTelefone(usuario, 1L, telefoneDto);

        assertNotNull(updatedTelefone);
        assertEquals("888888888", updatedTelefone.getNumero());
        verify(telefoneRepository, times(1)).save(any(Telefone.class));
    }

    @Test
    void testDeleteTelefone() {
        when(telefoneRepository.findById(1L)).thenReturn(Optional.of(telefone));
        doNothing().when(telefoneRepository).delete(telefone);

        telefoneService.deleteTelefone(usuario, 1L);

        verify(telefoneRepository, times(1)).delete(telefone);
    }

    @Test
    void testGetTelefoneById() {
        when(telefoneRepository.findById(1L)).thenReturn(Optional.of(telefone));

        Telefone foundTelefone = telefoneService.getTelefoneById(usuario, 1L);

        assertNotNull(foundTelefone);
        assertEquals("999999999", foundTelefone.getNumero());
        verify(telefoneRepository, times(1)).findById(1L);
    }

    @Test
    void testGetTelefonesByUsuario() {
        Page<Telefone> telefonesPage = new PageImpl<>(List.of(telefone));
        when(contatoRepository.findById(1L)).thenReturn(Optional.of(contato));
        when(telefoneRepository.findByContatoId(eq(1L), any(PageRequest.class))).thenReturn(telefonesPage);

        Page<Telefone> result = telefoneService.getTelefonesByUsuario(1L, usuario, 0, 10);

        assertNotNull(result);
        assertFalse(result.getContent().isEmpty());
        verify(telefoneRepository, times(1)).findByContatoId(eq(1L), any(PageRequest.class));
    }
}
