package com.agendaapi.agendaapi.controller;

import com.agendaapi.agendaapi.dto.contatodto.TelefoneDto;
import com.agendaapi.agendaapi.model.Telefone;
import com.agendaapi.agendaapi.model.Usuario;
import com.agendaapi.agendaapi.service.TelefoneService;
import com.agendaapi.agendaapi.service.UsuarioService;
import com.agendaapi.agendaapi.util.conversor.ConverterClass;
import com.agendaapi.agendaapi.vo.ContatoVO;
import com.agendaapi.agendaapi.vo.TelefoneVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;

import java.lang.reflect.InvocationTargetException;

@RestController
@RequestMapping("/v1/agenda/telefones")
public class TelefoneController {

    @Autowired
    private TelefoneService telefoneService;

    @Autowired
    private UsuarioService usuarioService;

    // Endpoint para criar um novo telefone
    @PostMapping("/{contatoId}")
    public ResponseEntity<TelefoneVO> createTelefone(
            @RequestHeader("Authorization") String authorizationHeader, // Recebe o token no cabeçalho
            @RequestBody @Valid TelefoneDto telefoneDto,
            @PathVariable Long contatoId) throws NoSuchFieldException, InvocationTargetException, IllegalAccessException, InstantiationException, NoSuchMethodException {

        Usuario userSignedIn = usuarioService.getContatoByToken(authorizationHeader);

        // Chama o serviço para criar o telefone com o token
        Telefone savedTelefone = telefoneService
                .createTelefone(userSignedIn, telefoneDto, contatoId);
        TelefoneVO telefoneVO = ConverterClass.convert(savedTelefone, TelefoneVO.class);

        // Retorna 201 (Created) com o objeto criado
        return ResponseEntity.status(HttpStatus.CREATED).body(telefoneVO);
    }

    // Endpoint para atualizar um telefone existente
    @PutMapping("/{telefoneId}")
    public ResponseEntity<TelefoneVO> updateTelefone(
            @RequestHeader("Authorization") String authorizationHeader, // Recebe o token no cabeçalho
            @PathVariable Long telefoneId,
            @RequestBody @Valid TelefoneDto telefoneDto) throws NoSuchFieldException, InvocationTargetException, IllegalAccessException, InstantiationException, NoSuchMethodException {

        Usuario userSignedIn = usuarioService.getContatoByToken(authorizationHeader);

        // Chama o serviço para atualizar o telefone com o token
        Telefone updatedTelefone = telefoneService.updateTelefone(userSignedIn, telefoneId, telefoneDto);

        TelefoneVO telefoneVO = ConverterClass.convert(updatedTelefone, TelefoneVO.class);

        // Retorna 201 (Created) com o objeto criado
        return ResponseEntity.status(HttpStatus.CREATED).body(telefoneVO);
    }


    @DeleteMapping("/{telefoneId}")
    public ResponseEntity<String> deleteTelefone(
            @RequestHeader("Authorization") String authorizationHeader, // Recebe o token no cabeçalho
            @PathVariable Long telefoneId) {

        Usuario userSignedIn = usuarioService.getContatoByToken(authorizationHeader);


        telefoneService.deleteTelefone(userSignedIn, telefoneId);

        return new ResponseEntity<>("Usuario deletado com sucesso!", HttpStatus.OK);
    }
    //todo: Endpoit get para baixar telefone do contato.
    //todo: Endpoit get para baixar lista de telefones do contato.
}
