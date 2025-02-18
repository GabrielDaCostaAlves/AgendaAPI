package com.agendaapi.agendaapi.service;

import com.agendaapi.agendaapi.domain.service.ContatoService;
import com.agendaapi.agendaapi.dto.contatodto.ContatoDto;
import com.agendaapi.agendaapi.dto.contatodto.ContatoUpdateDto;
import com.agendaapi.agendaapi.domain.model.entity.contato.Contato;
import com.agendaapi.agendaapi.domain.model.entity.usuario.Usuario;
import com.agendaapi.agendaapi.domain.repository.ContatoRepository;
import com.agendaapi.agendaapi.vo.ContatoVO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ContatoServiceTest {

    @InjectMocks
    private ContatoService contatoService;

    @Mock
    private ContatoRepository contatoRepository;

    @Mock
    private Usuario userSignedIn;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        when(userSignedIn.getEmail()).thenReturn("user@teste.com");
    }

    @Test
    void criarContatoTest() {

        ContatoDto contatoDto = new ContatoDto("Contato Teste", LocalDate.of(1990, 1, 1), null, null);
        Contato contatoMock = new Contato();
        contatoMock.setUsuario(userSignedIn);

        when(contatoRepository.save(any(Contato.class))).thenReturn(contatoMock);


        Contato createdContato = contatoService.createContato(userSignedIn, contatoDto);


        assertNotNull(createdContato);
        assertEquals("user@teste.com", createdContato.getUsuario().getEmail());
        verify(contatoRepository, times(1)).save(any(Contato.class));
    }

    @Test
    void atualizarContatoTest() {

        Long contatoId = 1L;
        Contato existingContato = new Contato();
        existingContato.setId(contatoId);
        existingContato.setNome("Contato Antigo");
        existingContato.setUsuario(userSignedIn);

        ContatoUpdateDto updateDto = new ContatoUpdateDto("Contato Atualizado", LocalDate.of(1990, 2, 2));

        when(contatoRepository.findById(contatoId)).thenReturn(Optional.of(existingContato));
        when(contatoRepository.save(any(Contato.class))).thenReturn(existingContato);


        Contato updatedContato = contatoService.updateContato(userSignedIn, contatoId, updateDto);


        assertNotNull(updatedContato);
        assertEquals("Contato Atualizado", updatedContato.getNome());
        assertNotNull(updatedContato.getUsuario());
        verify(contatoRepository, times(1)).save(any(Contato.class));
    }

    @Test
    void obterContatoPorIdTest() {

        Long contatoId = 1L;
        Contato contato = new Contato();
        contato.setId(contatoId);
        contato.setUsuario(userSignedIn);

        when(contatoRepository.findById(contatoId)).thenReturn(Optional.of(contato));


        Contato foundContato = contatoService.getContatoById(userSignedIn, contatoId);


        assertNotNull(foundContato);
        assertNotNull(foundContato.getUsuario());
        verify(contatoRepository, times(1)).findById(contatoId);
    }

    @Test
    void excluirContatoTest() {

        Long contatoId = 1L;
        Contato contato = new Contato();
        contato.setId(contatoId);
        contato.setUsuario(userSignedIn);

        when(contatoRepository.findById(contatoId)).thenReturn(Optional.of(contato));
        doNothing().when(contatoRepository).delete(contato);


        contatoService.deleteContato(userSignedIn, contatoId);


        verify(contatoRepository, times(1)).delete(contato);
    }

    @Test
    void obterTodosContatosTest() {

        int page = 0;
        int size = 10;
        PageRequest pageable = PageRequest.of(page, size);
        Page<ContatoVO> contatosPage = new PageImpl<>(List.of(new ContatoVO()));

        when(contatoRepository.findAllByUsuarioEmail(eq(userSignedIn.getEmail()), eq(pageable)))
                .thenReturn(contatosPage);


        Page<ContatoVO> result = contatoService.getAllContatos(userSignedIn, page, size);


        assertNotNull(result);
        assertFalse(result.getContent().isEmpty());
    }
}
