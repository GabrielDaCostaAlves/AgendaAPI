package com.agendaapi.agendaapi.controller;

import com.agendaapi.agendaapi.dto.contatodto.ContatoDto;
import com.agendaapi.agendaapi.model.Contato;
import com.agendaapi.agendaapi.model.Usuario;
import com.agendaapi.agendaapi.service.ContatoService;
import com.agendaapi.agendaapi.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/v1/agenda/contatos")
public class ContatoController {

    @Autowired
    private ContatoService contatoService;


    @Autowired
    private UsuarioService usuarioService;

    @PostMapping("/createcontato")
    public ResponseEntity<?> createContato(
            @RequestHeader("Authorization") String authorizationHeader, // Recebe o token no cabeçalho
            @RequestBody @Valid ContatoDto contatoDto) {

        Usuario userSignedIn = usuarioService.getContatoByToken(authorizationHeader);

        // Chama o serviço para criar o contato com o token
        contatoService.createContato(userSignedIn, contatoDto);

        return new ResponseEntity<>("Contato criado com sucesso!", HttpStatus.CREATED);
    }

    @PutMapping("/{contatoId}")
    public ResponseEntity<Contato> updateContato(
            @RequestHeader("Authorization") String authorizationHeader, // Recebe o token no cabeçalho
            @PathVariable Long contatoId,
            @RequestBody @Valid ContatoDto contatoDto) {

        Usuario userSignedIn = usuarioService.getContatoByToken(authorizationHeader);

        // Chama o serviço para atualizar o contato com o token
        Contato updatedContato = contatoService.updateContato(userSignedIn, contatoId, contatoDto);

        return ResponseEntity.ok(updatedContato);
    }

    @GetMapping("/{contatoId}")
    public ResponseEntity<Contato> getContatoById(
            @RequestHeader("Authorization") String authorizationHeader, // Recebe o token no cabeçalho
            @PathVariable Long contatoId) {

        Usuario userSignedIn = usuarioService.getContatoByToken(authorizationHeader);
        // Chama o serviço para obter o contato com o token
        Contato contato = contatoService.getContatoById(userSignedIn, contatoId);

        return ResponseEntity.ok(contato);
    }

    @DeleteMapping("/{contatoId}")
    public ResponseEntity<Void> deleteContato(
            @RequestHeader("Authorization") String authorizationHeader, // Recebe o token no cabeçalho
            @PathVariable Long contatoId) {

        Usuario userSignedIn = usuarioService.getContatoByToken(authorizationHeader);
        // Chama o serviço para deletar o contato com o token
        contatoService.deleteContato(userSignedIn, contatoId);

        return ResponseEntity.noContent().build();
    }

    //todo: Endpoint para listar contatos do usuario.
}
