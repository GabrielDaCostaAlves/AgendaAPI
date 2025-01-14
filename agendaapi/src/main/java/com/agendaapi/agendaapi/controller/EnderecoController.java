package com.agendaapi.agendaapi.controller;

import com.agendaapi.agendaapi.dto.contatodto.EnderecoDto;
import com.agendaapi.agendaapi.model.Endereco;
import com.agendaapi.agendaapi.model.Usuario;
import com.agendaapi.agendaapi.service.EnderecoService;
import com.agendaapi.agendaapi.service.UsuarioService;
import com.agendaapi.agendaapi.util.conversor.ConverterClass;
import com.agendaapi.agendaapi.vo.EnderecoVO;
import com.agendaapi.agendaapi.vo.TelefoneVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;

import java.lang.reflect.InvocationTargetException;

@RestController
@RequestMapping("/v1/agenda/enderecos")
public class EnderecoController {

    @Autowired
    private EnderecoService enderecoService;

    @Autowired
    private UsuarioService usuarioService;


    // Endpoint para criar um novo endereço
    @PostMapping("/{contatoId}")
    public ResponseEntity<EnderecoVO> createEndereco(
            @RequestHeader("Authorization") String authorizationHeader, // Recebe o token no cabeçalho
            @RequestBody @Valid EnderecoDto enderecoDto,
            @PathVariable Long contatoId) throws NoSuchFieldException, InvocationTargetException, IllegalAccessException, InstantiationException, NoSuchMethodException {

        Usuario userSignedIn = usuarioService.getContatoByToken(authorizationHeader);

        // Chama o serviço para criar o endereço com o token
        Endereco savedEndereco = enderecoService.createEndereco(userSignedIn, enderecoDto, contatoId);
        EnderecoVO enderecoVO = ConverterClass.convert(savedEndereco, EnderecoVO.class);

        // Retorna 201 (Created) com o objeto criado
        return ResponseEntity.status(HttpStatus.CREATED).body(enderecoVO);
    }

    // Endpoint para atualizar um endereço existente
    @PutMapping("/{enderecoId}")
    public ResponseEntity<EnderecoVO> updateEndereco(
            @RequestHeader("Authorization") String authorizationHeader, // Recebe o token no cabeçalho
            @PathVariable Long enderecoId,
            @RequestBody @Valid EnderecoDto enderecoDto) throws NoSuchFieldException, InvocationTargetException, IllegalAccessException, InstantiationException, NoSuchMethodException {

        Usuario userSignedIn = usuarioService.getContatoByToken(authorizationHeader);

        // Chama o serviço para atualizar o endereço com o token
        Endereco updatedEndereco = enderecoService.updateEndereco(userSignedIn, enderecoId, enderecoDto);
        EnderecoVO enderecoVO = ConverterClass.convert(updatedEndereco, EnderecoVO.class);

        // Retorna 201 (Created) com o objeto criado
        return ResponseEntity.status(HttpStatus.CREATED).body(enderecoVO);
    }

    @DeleteMapping("/{enderecoId}")
    public ResponseEntity<String> deleteEndereco(
            @RequestHeader("Authorization") String authorizationHeader, // Recebe o token no cabeçalho
            @PathVariable Long enderecoId) {

        String response;
        Usuario userSignedIn = usuarioService.getContatoByToken(authorizationHeader);

        // Chama o serviço para atualizar o telefone com o token
        enderecoService.deleteEndereco(userSignedIn, enderecoId);


        return new ResponseEntity<>("Usuario deletado com sucesso!", HttpStatus.OK);
    }
    //todo: Criar endpoit get para endereço do contato.
    //todo: Criar endpoit get para listar endereços do contato.
}
