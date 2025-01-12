package com.agendaapi.agendaapi.controller;

import com.agendaapi.agendaapi.dto.contatodto.TelefoneDto;
import com.agendaapi.agendaapi.model.Telefone;
import com.agendaapi.agendaapi.model.Usuario;
import com.agendaapi.agendaapi.service.TelefoneService;
import com.agendaapi.agendaapi.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/v1/agenda/telefones")
public class TelefoneController {

    @Autowired
    private TelefoneService telefoneService;

    @Autowired
    private UsuarioService usuarioService;

    // Endpoint para criar um novo telefone
    @PostMapping("/{contatoId}")
    public ResponseEntity<Telefone> createTelefone(
            @RequestHeader("Authorization") String authorizationHeader, // Recebe o token no cabeçalho
            @RequestBody @Valid TelefoneDto telefoneDto,
            @PathVariable Long contatoId) {

        Usuario userSignedIn = usuarioService.getContatoByToken(authorizationHeader);

        // Chama o serviço para criar o telefone com o token
        Telefone savedTelefone = telefoneService
                .createTelefone(userSignedIn, telefoneDto, contatoId);

        return ResponseEntity.ok(savedTelefone);
    }

    // Endpoint para atualizar um telefone existente
    @PutMapping("/{telefoneId}")
    public ResponseEntity<Telefone> updateTelefone(
            @RequestHeader("Authorization") String authorizationHeader, // Recebe o token no cabeçalho
            @PathVariable Long telefoneId,
            @RequestBody @Valid TelefoneDto telefoneDto) {

        Usuario userSignedIn = usuarioService.getContatoByToken(authorizationHeader);

        // Chama o serviço para atualizar o telefone com o token
        Telefone updatedTelefone = telefoneService.updateTelefone(userSignedIn, telefoneId, telefoneDto);

        return ResponseEntity.ok(updatedTelefone);
    }


    @DeleteMapping("/{telefoneId}")
    public ResponseEntity<String> deleteTelefone(
            @RequestHeader("Authorization") String authorizationHeader, // Recebe o token no cabeçalho
            @PathVariable Long telefoneId) {

        String response;
        Usuario userSignedIn = usuarioService.getContatoByToken(authorizationHeader);

        // Chama o serviço para atualizar o telefone com o token
        boolean result = telefoneService.deleteTelefone(userSignedIn, telefoneId);
        if (result) {
            response = "Telefone deletado com sucesso!";
        } else {
            response = "Erro ao tentar deletar!";
        }

        return ResponseEntity.ok(response);
    }
    //todo: Endpoit get para baixar telefone do contato.
    //todo: Endpoit get para baixar lista de telefones do contato.
}
