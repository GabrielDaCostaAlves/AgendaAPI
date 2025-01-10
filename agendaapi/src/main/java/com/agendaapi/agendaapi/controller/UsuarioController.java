package com.agendaapi.agendaapi.controller;

import com.agendaapi.agendaapi.dto.usuariodto.UpdateEmailDto;
import com.agendaapi.agendaapi.dto.usuariodto.UserDto;
import com.agendaapi.agendaapi.dto.usuariodto.LoginUserDto;
import com.agendaapi.agendaapi.dto.usuariodto.RecoveryJwtTokenDto;
import com.agendaapi.agendaapi.service.UserService;
import jakarta.validation.Valid; // Importa a anotação @Valid
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
public class UsuarioController {

    @Autowired
    private UserService userService;

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
        RecoveryJwtTokenDto token = userService.authenticateUser(loginUserDto);
        return new ResponseEntity<>(token, HttpStatus.OK);
    }

    @PostMapping("/create")
    public ResponseEntity<?> createUser(
            @Valid @RequestBody UserDto userDto, // Aplica validação no DTO
            BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            // Retorna erro 400 com detalhes dos erros de validação
            List<String> errors = bindingResult.getAllErrors()
                    .stream()
                    .map(error -> error.getDefaultMessage())
                    .toList();
            return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
        }
        userService.createUser(userDto);
        return new ResponseEntity<>("Usuario criado com sucesso!", HttpStatus.CREATED);
    }

    @PutMapping("/config/update")
    public ResponseEntity<?> updateUser(
            @Valid @RequestBody UserDto userDto, // Aplica validação no DTO
            BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            // Retorna erro 400 com detalhes dos erros de validação
            List<String> errors = bindingResult.getAllErrors()
                    .stream()
                    .map(error -> error.getDefaultMessage())
                    .toList();
            return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
        }
        userService.updateUser(userDto);
        return new ResponseEntity<>("Usuario alterado com sucesso!", HttpStatus.OK);
    }

    @PutMapping("/config/update/email")
    public ResponseEntity<?> updateEmail(
            @Valid @RequestBody UpdateEmailDto updateEmailDto, // Aplica validação no DTO
            BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            // Retorna erro 400 com detalhes dos erros de validação
            List<String> errors = bindingResult.getAllErrors()
                    .stream()
                    .map(error -> error.getDefaultMessage())
                    .toList();
            return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
        }
        RecoveryJwtTokenDto newToken = userService.updateEmail(updateEmailDto);
        return new ResponseEntity<>(newToken, HttpStatus.OK);
    }

    @GetMapping("/test/customer")
    public ResponseEntity<String> getCustomerAuthenticationTest() {
        return new ResponseEntity<>("Cliente autenticado com sucesso.", HttpStatus.OK);
    }

    @GetMapping("/test/administrator")
    public ResponseEntity<String> getAdminAuthenticationTest() {
        return new ResponseEntity<>("Administrador autenticado com sucesso.", HttpStatus.OK);
    }
}
