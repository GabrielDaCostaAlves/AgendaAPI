package com.agendaapi.agendaapi.controller;

import com.agendaapi.agendaapi.dto.contatodto.ContatoDto;
import com.agendaapi.agendaapi.model.Contato;
import com.agendaapi.agendaapi.service.ContatoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/users")
public class ContatoController {

    @Autowired
    private ContatoService contatoService;

    @PostMapping("/createcontato")
    public ResponseEntity<?> createContato(
            @RequestHeader("Authorization") String authorizationHeader, // Recebe o token no cabeçalho
            @RequestBody @Valid ContatoDto contatoDto) {

        // Remover "Bearer " do token
        String token = authorizationHeader.replace("Bearer ", "");

        // Chama o serviço para criar o contato com o token
        contatoService.createContato(token, contatoDto);

        return new ResponseEntity<>("Contato criado com sucesso!", HttpStatus.CREATED);
    }

    @PutMapping("/updatecontato/{contatoId}")
    public ResponseEntity<Contato> updateContato(
            @RequestHeader("Authorization") String authorizationHeader, // Recebe o token no cabeçalho
            @PathVariable Long contatoId,
            @RequestBody @Valid ContatoDto contatoDto) {

        // Remover "Bearer " do token
        String token = authorizationHeader.replace("Bearer ", "");

        // Chama o serviço para atualizar o contato com o token
        Contato updatedContato = contatoService.updateContato(token, contatoId, contatoDto);

        return ResponseEntity.ok(updatedContato);
    }

    @GetMapping("/contato/{contatoId}")
    public ResponseEntity<Contato> getContatoById(
            @RequestHeader("Authorization") String authorizationHeader, // Recebe o token no cabeçalho
            @PathVariable Long contatoId) {

        // Remover "Bearer " do token
        String token = authorizationHeader.replace("Bearer ", "");

        // Chama o serviço para obter o contato com o token
        Contato contato = contatoService.getContatoById(token, contatoId);

        return ResponseEntity.ok(contato);
    }

    @DeleteMapping("/deletecontato/{contatoId}")
    public ResponseEntity<Void> deleteContato(
            @RequestHeader("Authorization") String authorizationHeader, // Recebe o token no cabeçalho
            @PathVariable Long contatoId) {

        // Remover "Bearer " do token
        String token = authorizationHeader.replace("Bearer ", "");

        // Chama o serviço para deletar o contato com o token
        contatoService.deleteContato(token, contatoId);

        return ResponseEntity.noContent().build();
    }
}
