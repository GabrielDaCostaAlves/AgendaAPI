package com.agendaapi.agendaapi.controller;

import com.agendaapi.agendaapi.dto.contatodto.EnderecoDto;
import com.agendaapi.agendaapi.model.Endereco;
import com.agendaapi.agendaapi.service.EnderecoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/users")
public class EnderecoController {

    @Autowired
    private EnderecoService enderecoService;

    // Endpoint para criar um novo endereço
    @PostMapping("/createendereco/{contatoId}")
    public ResponseEntity<Endereco> createEndereco(
            @RequestHeader("Authorization") String authorizationHeader, // Recebe o token no cabeçalho
            @RequestBody @Valid EnderecoDto enderecoDto,
            @PathVariable Long contatoId) {

        // Remover "Bearer " do token
        String token = authorizationHeader.replace("Bearer ", "");

        // Chama o serviço para criar o endereço com o token
        Endereco savedEndereco = enderecoService.createEndereco(token, enderecoDto, contatoId);

        return ResponseEntity.ok(savedEndereco);
    }

    // Endpoint para atualizar um endereço existente
    @PutMapping("/updateendereco/{enderecoId}")
    public ResponseEntity<Endereco> updateEndereco(
            @RequestHeader("Authorization") String authorizationHeader, // Recebe o token no cabeçalho
            @PathVariable Long enderecoId,
            @RequestBody @Valid EnderecoDto enderecoDto) {

        // Remover "Bearer " do token
        String token = authorizationHeader.replace("Bearer ", "");

        // Chama o serviço para atualizar o endereço com o token
        Endereco updatedEndereco = enderecoService.updateEndereco(token, enderecoId, enderecoDto);

        return ResponseEntity.ok(updatedEndereco);
    }
}
