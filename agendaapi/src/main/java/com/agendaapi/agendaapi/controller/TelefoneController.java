package com.agendaapi.agendaapi.controller;

import com.agendaapi.agendaapi.dto.contatodto.TelefoneDto;
import com.agendaapi.agendaapi.model.Telefone;
import com.agendaapi.agendaapi.service.TelefoneService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/users")
public class TelefoneController {

    @Autowired
    private TelefoneService telefoneService;

    // Endpoint para criar um novo telefone
    @PostMapping("/createtelefone/{contatoId}")
    public ResponseEntity<Telefone> createTelefone(
            @RequestHeader("Authorization") String authorizationHeader, // Recebe o token no cabeçalho
            @RequestBody @Valid TelefoneDto telefoneDto,
            @PathVariable Long contatoId) {

        // Remover "Bearer " do token
        String token = authorizationHeader.replace("Bearer ", "");

        // Chama o serviço para criar o telefone com o token
        Telefone savedTelefone = telefoneService.createTelefone(token, telefoneDto, contatoId);

        return ResponseEntity.ok(savedTelefone);
    }

    // Endpoint para atualizar um telefone existente
    @PutMapping("/updatetelefone/{telefoneId}")
    public ResponseEntity<Telefone> updateTelefone(
            @RequestHeader("Authorization") String authorizationHeader, // Recebe o token no cabeçalho
            @PathVariable Long telefoneId,
            @RequestBody @Valid TelefoneDto telefoneDto) {

        // Remover "Bearer " do token
        String token = authorizationHeader.replace("Bearer ", "");

        // Chama o serviço para atualizar o telefone com o token
        Telefone updatedTelefone = telefoneService.updateTelefone(token, telefoneId, telefoneDto);

        return ResponseEntity.ok(updatedTelefone);
    }
}
