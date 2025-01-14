package com.agendaapi.agendaapi.controller;

import com.agendaapi.agendaapi.dto.contatodto.ContatoDto;
import com.agendaapi.agendaapi.model.Contato;
import com.agendaapi.agendaapi.model.Usuario;
import com.agendaapi.agendaapi.service.ContatoService;
import com.agendaapi.agendaapi.service.UsuarioService;
import com.agendaapi.agendaapi.util.conversor.ConverterClass;
import com.agendaapi.agendaapi.vo.ContatoVO;
import com.agendaapi.agendaapi.vo.UsuarioVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;

import java.lang.reflect.InvocationTargetException;

@RestController
@RequestMapping("/v1/agenda/contatos")
public class ContatoController {

    @Autowired
    private ContatoService contatoService;


    @Autowired
    private UsuarioService usuarioService;

    @PostMapping("/createcontato")
    public ResponseEntity<ContatoVO> createContato(
            @RequestHeader("Authorization") String authorizationHeader, // Recebe o token no cabeçalho
            @RequestBody @Valid ContatoDto contatoDto) throws NoSuchFieldException, InvocationTargetException, IllegalAccessException, InstantiationException, NoSuchMethodException {

        Usuario userSignedIn = usuarioService.getContatoByToken(authorizationHeader);

        // Chama o serviço para criar o contato com o token
        Contato contato = contatoService.createContato(userSignedIn, contatoDto);
        ContatoVO contatoVO = ConverterClass.convert(contato, ContatoVO.class);

        // Retorna 201 (Created) com o objeto criado
        return ResponseEntity.status(HttpStatus.CREATED).body(contatoVO);
    }

    @PutMapping("/{contatoId}")
    public ResponseEntity<ContatoVO> updateContato(
            @RequestHeader("Authorization") String authorizationHeader, // Recebe o token no cabeçalho
            @PathVariable Long contatoId,
            @RequestBody @Valid ContatoDto contatoDto) throws NoSuchFieldException, InvocationTargetException, IllegalAccessException, InstantiationException, NoSuchMethodException {

        Usuario userSignedIn = usuarioService.getContatoByToken(authorizationHeader);

        // Chama o serviço para atualizar o contato com o token
        Contato updatedContato = contatoService.updateContato(userSignedIn, contatoId, contatoDto);
        ContatoVO contatoVO = ConverterClass.convert(updatedContato, ContatoVO.class);

        // Retorna 201 (Created) com o objeto criado
        return ResponseEntity.status(HttpStatus.CREATED).body(contatoVO);
    }

    @GetMapping("/{contatoId}")
    public ResponseEntity<ContatoVO> getContatoById(
            @RequestHeader("Authorization") String authorizationHeader, // Recebe o token no cabeçalho
            @PathVariable Long contatoId) throws NoSuchFieldException, InvocationTargetException, IllegalAccessException, InstantiationException, NoSuchMethodException {

        Usuario userSignedIn = usuarioService.getContatoByToken(authorizationHeader);
        // Chama o serviço para obter o contato com o token
        Contato contato = contatoService.getContatoById(userSignedIn, contatoId);
        ContatoVO contatoVO = ConverterClass.convert(contato, ContatoVO.class);

        // Retorna 201 (Created) com o objeto criado
        return ResponseEntity.status(HttpStatus.CREATED).body(contatoVO);
    }

    @DeleteMapping("/{contatoId}")
    public ResponseEntity<String> deleteContato(
            @RequestHeader("Authorization") String authorizationHeader, // Recebe o token no cabeçalho
            @PathVariable Long contatoId) {

        Usuario userSignedIn = usuarioService.getContatoByToken(authorizationHeader);
        // Chama o serviço para deletar o contato com o token
        contatoService.deleteContato(userSignedIn, contatoId);

        return new ResponseEntity<>("Usuario deletado com sucesso!", HttpStatus.OK);
    }

    //todo: Endpoint para listar contatos do usuario.
}
