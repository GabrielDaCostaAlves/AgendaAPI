package com.agendaapi.agendaapi.controller;

import com.agendaapi.agendaapi.assembler.UsuarioAssembler;
import com.agendaapi.agendaapi.dto.usuariodto.UsuarioDto;
import com.agendaapi.agendaapi.dto.usuariodto.LoginUserDto;
import com.agendaapi.agendaapi.dto.usuariodto.RecoveryJwtTokenDto;
import com.agendaapi.agendaapi.domain.model.entity.usuario.Usuario;
import com.agendaapi.agendaapi.domain.service.UsuarioService;
import com.agendaapi.agendaapi.util.conversor.ConverterClass;
import com.agendaapi.agendaapi.vo.UsuarioVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.lang.reflect.InvocationTargetException;

@RestController
@RequestMapping("/v1/agenda")
@Tag(name = "Usuário", description = "Gerenciamento de usuários")
public class UsuarioController {

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private UsuarioAssembler usuarioAssembler;

    @Operation(
            summary = "Autenticar usuário",
            description = "Realiza a autenticação de um usuário e retorna um token JWT.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Autenticação realizada com sucesso",
                            content = @Content(
                                    mediaType = "application/json",
                                    examples = @ExampleObject(value = "{ \"token\": \"eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiJ1c2VyMTIzIiwiZXhwIjoxNjc1ODgwMDAwfQ.abc123\" }")
                            )
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Dados inválidos fornecidos na requisição",
                            content = @Content(mediaType = "application/json")
                    )
            }
    )
    @PostMapping(value = "/login",
            consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE},
            produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public ResponseEntity<?> authenticateUser(
            @Valid @RequestBody LoginUserDto loginUserDto) {

        RecoveryJwtTokenDto token = usuarioService.authenticateUser(loginUserDto);
        return new ResponseEntity<>(token, HttpStatus.OK);
    }


    @Operation(
            summary = "Criar usuário",
            description = "Recebe os dados de um usuário e o cadastra no sistema.",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Dados necessários para criar um novo usuário",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = UsuarioVO.class),
                            examples = @ExampleObject(
                                    name = "Exemplo de entrada",
                                    value = """
                                            {
                                                "nome": "Usuario de teste",
                                                "email": "teste.user@email.com",
                                                "role": "ROLE_CUSTOMER"
                                            }
                                            """
                            )
                    )
            ),
            responses = {
                    @ApiResponse(
                            responseCode = "201",
                            description = "Usuário criado com sucesso",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = UsuarioVO.class),
                                    examples = @ExampleObject(
                                            name = "Exemplo de resposta",
                                            value = """
                                                    {
                                                        "nome": "Usuario de teste",
                                                        "email": "teste.user@email.com",
                                                        "criadoEm": "2025-01-24T15:30:00Z",
                                                        "role": "ROLE_CUSTOMER"
                                                    }
                                                    """
                                    )
                            )
                    )
            }
    )
    @PostMapping(value = "/create",
            consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE},
            produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public ResponseEntity<UsuarioVO> createUser(@Valid @RequestBody UsuarioDto usuarioDto) throws IllegalAccessException, InstantiationException, InvocationTargetException, NoSuchMethodException, NoSuchFieldException {
        Usuario usuario = usuarioService.createUser(usuarioDto);
        UsuarioVO usuarioVO = ConverterClass.convert(usuario, UsuarioVO.class);

        return ResponseEntity.status(201).body(usuarioVO);
    }

    @Operation(
            summary = "Atualizar usuário",
            description = "Atualiza os dados do usuário autenticado com base no token de autenticação e nos dados fornecidos no corpo da requisição.",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Dados para atualizar o usuário",
                    required = true,
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = UsuarioDto.class),
                            examples = @ExampleObject(
                                    name = "Exemplo de entrada",
                                    value = """
                                            {
                                                "nome": "Usuario de teste",
                                                "email": "teste.atualizado@email.com",
                                                "criadoEm": "2025-01-24T15:30:00Z",
                                                "role": "ROLE_CUSTOMER"
                                            }
                                            """
                            )
                    )
            ),
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Usuário atualizado com sucesso",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = UsuarioVO.class),
                                    examples = @ExampleObject(
                                            name = "Exemplo de resposta",
                                            value = """
                                                    {
                                                        "nome": "Usuario de teste",
                                                        "email": "teste.atualizado@email.com",
                                                        "criadoEm": "2025-01-24T15:30:00Z",
                                                        "role": "ROLE_CUSTOMER"
                                                    }
                                                    """
                                    )
                            )
                    )
            }
    )
    @PutMapping(value = "/config/update",
            consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE},
            produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public ResponseEntity<UsuarioVO> updateUser(
            @RequestHeader("Authorization") String authorizationHeader,
            @Valid @RequestBody UsuarioDto usuarioDto) throws IllegalAccessException, InstantiationException, InvocationTargetException, NoSuchMethodException, NoSuchFieldException {

        Usuario userSignedIn = usuarioService.getContatoByToken(authorizationHeader);
        Usuario usuario = usuarioService.updateUser(usuarioDto, userSignedIn);

        UsuarioVO usuarioVO = ConverterClass.convert(usuario, UsuarioVO.class);
        return ResponseEntity.ok(usuarioVO);
    }

    @Operation(
            summary = "Deletar usuário",
            description = "Deleta o usuário autenticado com base no token de autenticação fornecido no cabeçalho.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Usuário deletado com sucesso",
                            content = @Content(
                                    mediaType = "application/json",
                                    examples = @ExampleObject(
                                            name = "Exemplo de resposta",
                                            value = """
                                                    {
                                                        "message": "Usuário deletado com sucesso!"
                                                    }
                                                    """
                                    )
                            )
                    )
            }
    )
    @DeleteMapping("/config/delete")
    public ResponseEntity<?> deleteUser(
            @RequestHeader("Authorization") String authorizationHeader
    ) {

        Usuario userSignedIn = usuarioService.getContatoByToken(authorizationHeader);
        usuarioService.deleteUser(userSignedIn);
        return new ResponseEntity<>("Usuario deletado com sucesso!", HttpStatus.OK);
    }

    @Operation(
            summary = "Listar usuários com paginação",
            description = "Retorna uma lista de usuários paginada. Somente usuários com a role 'ROLE_ADMINISTRATOR' podem acessar este endpoint.",
            parameters = {
                    @Parameter(name = "page", description = "Número da página a ser exibida (padrão 0)", example = "0"),
                    @Parameter(name = "size", description = "Quantidade de itens por página (padrão 10)", example = "10")
            },
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Retorna uma lista de usuários paginada. Somente usuários com a role 'ROLE_ADMINISTRATOR' podem acessar este endpoint.",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = Page.class),
                                    examples = @ExampleObject(
                                            name = "Exemplo de resposta",
                                            value = """
                                                    {
                                                        "content": [
                                                            {
                                                                "nome": "Usuario 1",
                                                                "email": "usuario1@email.com",
                                                                "criadoEm": "2025-01-24T15:30:00Z",
                                                                "role": "ROLE_ADMINISTRATOR"
                                                            },
                                                            {
                                                                "nome": "Usuario 2",
                                                                "email": "usuario2@email.com",
                                                                "criadoEm": "2025-01-25T16:00:00Z",
                                                                "role": "ROLE_CUSTOMER"
                                                            }
                                                        ],
                                                        "pageable": {
                                                            "pageNumber": 0,
                                                            "pageSize": 10,
                                                            "offset": 0
                                                        },
                                                        "totalElements": 2,
                                                        "totalPages": 1
                                                    }
                                                    """
                                    )
                            )
                    )
            }
    )
    @GetMapping(value = "/usuarios", produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public ResponseEntity<Page<Usuario>> getAllUsuarios(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        Page<Usuario> usuarios = usuarioService.getAllUsers(page, size);
        return ResponseEntity.ok(usuarios);
    }


    @GetMapping(value = "/{userId}",produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public EntityModel<UsuarioVO> getUsuario(@PathVariable Long userId) {
        Usuario usuario = usuarioService.getUsuarioById(userId);

        if (usuario == null) {
            throw new RuntimeException("Ocorreu um erro ao tentar localizar o usuario");
        }

        return usuarioAssembler.toModel(usuario);
    }

}