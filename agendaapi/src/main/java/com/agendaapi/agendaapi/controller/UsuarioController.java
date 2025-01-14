package com.agendaapi.agendaapi.controller;

import com.agendaapi.agendaapi.dto.usuariodto.UsuarioDto;
import com.agendaapi.agendaapi.dto.usuariodto.LoginUserDto;
import com.agendaapi.agendaapi.dto.usuariodto.RecoveryJwtTokenDto;
import com.agendaapi.agendaapi.model.Usuario;
import com.agendaapi.agendaapi.service.UsuarioService;
import com.agendaapi.agendaapi.util.conversor.ConverterClass;
import com.agendaapi.agendaapi.vo.UsuarioVO;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.lang.reflect.InvocationTargetException;
import java.util.List;

@RestController
@RequestMapping("/v1/agenda")
public class UsuarioController {

    @Autowired
    private UsuarioService usuarioService;


    @PostMapping("/login")
    public ResponseEntity<?> authenticateUser(
            @Valid @RequestBody LoginUserDto loginUserDto, // Aplica validação no DTO
            BindingResult bindingResult) { // Recebe o resultado da validação
        if (bindingResult.hasErrors()) {
            // Retorna erro 400 com detalhes dos erros de validação
            List<String> errors = bindingResult.getAllErrors()
                    .stream()
                    .map(DefaultMessageSourceResolvable::getDefaultMessage)
                    .toList();
            return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
        }
        RecoveryJwtTokenDto token = usuarioService.authenticateUser(loginUserDto);
        return new ResponseEntity<>(token, HttpStatus.OK);
    }

    @PostMapping("/create")
    public ResponseEntity<UsuarioVO> createUser(@Valid @RequestBody UsuarioDto usuarioDto) throws IllegalAccessException, InstantiationException, InvocationTargetException, NoSuchMethodException, NoSuchFieldException {
        // Chama o serviço para criar o usuário
        Usuario usuario = usuarioService.createUser(usuarioDto);
        UsuarioVO usuarioVO = ConverterClass.convert(usuario, UsuarioVO.class);

        // Retorna 201 (Created) com o objeto criado
        return ResponseEntity.status(HttpStatus.CREATED).body(usuarioVO);
    }


    @PutMapping("/config/update")
    public ResponseEntity<UsuarioVO> updateUser(
            // Recebe o token no cabeçalho
            @RequestHeader("Authorization") String authorizationHeader,
            @Valid @RequestBody UsuarioDto usuarioDto) throws IllegalAccessException, InstantiationException, InvocationTargetException, NoSuchMethodException, NoSuchFieldException {

        Usuario userSignedIn = usuarioService.getContatoByToken(authorizationHeader);
        Usuario usuario = usuarioService.updateUser(usuarioDto, userSignedIn);

        UsuarioVO usuarioVO = ConverterClass.convert(usuario, UsuarioVO.class);
        return ResponseEntity.ok(usuarioVO);
    }


    @DeleteMapping("/config/delete")
    public ResponseEntity<?> deleteUser(
            // Recebe o token no cabeçalho
            @RequestHeader("Authorization") String authorizationHeader
    ) {

        Usuario userSignedIn = usuarioService.getContatoByToken(authorizationHeader);
        usuarioService.deleteUser(userSignedIn);
        return new ResponseEntity<>("Usuario deletado com sucesso!", HttpStatus.OK);
    }
}
