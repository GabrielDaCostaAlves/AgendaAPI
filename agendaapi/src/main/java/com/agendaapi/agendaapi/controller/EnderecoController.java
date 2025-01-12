package com.agendaapi.agendaapi.controller;

import com.agendaapi.agendaapi.dto.contatodto.EnderecoDto;
import com.agendaapi.agendaapi.model.Endereco;
import com.agendaapi.agendaapi.model.Usuario;
import com.agendaapi.agendaapi.service.EnderecoService;
import com.agendaapi.agendaapi.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/v1/agenda/enderecos")
public class EnderecoController {

    @Autowired
    private EnderecoService enderecoService;

    @Autowired
    private UsuarioService usuarioService;


    // Endpoint para criar um novo endereço
    @PostMapping("/{contatoId}")
    public ResponseEntity<Endereco> createEndereco(
            @RequestHeader("Authorization") String authorizationHeader, // Recebe o token no cabeçalho
            @RequestBody @Valid EnderecoDto enderecoDto,
            @PathVariable Long contatoId) {

        Usuario userSignedIn = usuarioService.getContatoByToken(authorizationHeader);

        // Chama o serviço para criar o endereço com o token
        Endereco savedEndereco = enderecoService.createEndereco(userSignedIn, enderecoDto, contatoId);

        return ResponseEntity.ok(savedEndereco);
    }

    // Endpoint para atualizar um endereço existente
    @PutMapping("/{enderecoId}")
    public ResponseEntity<Endereco> updateEndereco(
            @RequestHeader("Authorization") String authorizationHeader, // Recebe o token no cabeçalho
            @PathVariable Long enderecoId,
            @RequestBody @Valid EnderecoDto enderecoDto) {

        Usuario userSignedIn = usuarioService.getContatoByToken(authorizationHeader);

        // Chama o serviço para atualizar o endereço com o token
        Endereco updatedEndereco = enderecoService.updateEndereco(userSignedIn, enderecoId, enderecoDto);

        return ResponseEntity.ok(updatedEndereco);
    }

    @DeleteMapping("/{enderecoId}")
    public ResponseEntity<String> deleteEndereco(
            @RequestHeader("Authorization") String authorizationHeader, // Recebe o token no cabeçalho
            @PathVariable Long enderecoId) {

        String response;
        Usuario userSignedIn = usuarioService.getContatoByToken(authorizationHeader);

        // Chama o serviço para atualizar o telefone com o token
        boolean result = enderecoService.deleteEndereco(userSignedIn, enderecoId);
        if (result){
            response = "Telefone deletado com sucesso!";
        }else {
            response = "Erro ao tentar deletar!";
        }

        return ResponseEntity.ok(response);
    }
    //todo: Criar endpoit get para endereço do contato.
    //todo: Criar endpoit get para listar endereços do contato.
}
