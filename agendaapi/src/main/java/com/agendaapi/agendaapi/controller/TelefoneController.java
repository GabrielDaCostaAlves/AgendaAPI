package com.agendaapi.agendaapi.controller;

import com.agendaapi.agendaapi.dto.contatodto.TelefoneDto;
import com.agendaapi.agendaapi.domain.model.entity.contato.Telefone;
import com.agendaapi.agendaapi.domain.model.entity.usuario.Usuario;
import com.agendaapi.agendaapi.domain.service.TelefoneService;
import com.agendaapi.agendaapi.domain.service.UsuarioService;
import com.agendaapi.agendaapi.util.conversor.ConverterClass;
import com.agendaapi.agendaapi.vo.TelefoneVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;

import java.lang.reflect.InvocationTargetException;

@Tag(name = "Telefone", description = "Gerenciamento de telefones")
@RestController
@RequestMapping("/v1/agenda/telefones")
public class TelefoneController {

    @Autowired
    private TelefoneService telefoneService;

    @Autowired
    private UsuarioService usuarioService;


    @Operation(
            summary = "Criar um novo telefone",
            description = "Recebe os dados de um novo telefone e realiza a criação no sistema.",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Dados necessários para criar um novo telefone",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = TelefoneDto.class),
                            examples = @ExampleObject(
                                    name = "O número de telefone deve ser válido (formato: (11) 91234-5678 ou 11912345678)",
                                    value = """
                                    {
                                        "numero": "011999998888",
                                        "tipo": "Celular"
                                    }
                                    """
                            )
                    )
            ),
            responses = {
                    @ApiResponse(
                            responseCode = "201",
                            description = "Telefone criado com sucesso",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = TelefoneVO.class),
                                    examples = @ExampleObject(
                                            name = "Exemplo de resposta",
                                            value = """
                                            {
                                                "numero": "011999998888",
                                                "tipo": "Celular"
                                            }
                                            """
                                    )
                            )
                    )
            }
    )
    @PostMapping(value = "/{contatoId}",
            consumes =  {MediaType.APPLICATION_JSON_VALUE,MediaType.APPLICATION_XML_VALUE},
            produces = {MediaType.APPLICATION_JSON_VALUE,MediaType.APPLICATION_XML_VALUE})
    public ResponseEntity<TelefoneVO> createTelefone(
            @RequestHeader("Authorization") String authorizationHeader,
            @RequestBody @Valid TelefoneDto telefoneDto,
            @PathVariable Long contatoId) throws NoSuchFieldException, InvocationTargetException, IllegalAccessException, InstantiationException, NoSuchMethodException {

        Usuario userSignedIn = usuarioService.getContatoByToken(authorizationHeader);


        Telefone savedTelefone = telefoneService
                .createTelefone(userSignedIn, telefoneDto, contatoId);
        TelefoneVO telefoneVO = ConverterClass.convert(savedTelefone, TelefoneVO.class);

        return ResponseEntity.status(HttpStatus.CREATED).body(telefoneVO);
    }

    @Operation(
            summary = "Atualizar um telefone",
            description = "Recebe os dados atualizados de um telefone existente e realiza a atualização no sistema.",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Dados necessários para atualizar o telefone",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = TelefoneDto.class),
                            examples = @ExampleObject(
                                    name = "O número de telefone deve ser válido (formato: (11) 91234-5678 ou 11912345678)",
                                    value = """
                                    {
                                        "numero": "011988887777",
                                        "tipo": "Comercial"
                                    }
                                    """
                            )
                    )
            ),
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Telefone atualizado com sucesso",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = TelefoneVO.class),
                                    examples = @ExampleObject(
                                            name = "Exemplo de resposta",
                                            value = """
                                            {
                                                "numero": "011988887777",
                                                "tipo": "Comercial"
                                            }
                                            """
                                    )
                            )
                    )
            }
    )
    @PutMapping(value = "/{telefoneId}",
            consumes =  {MediaType.APPLICATION_JSON_VALUE,MediaType.APPLICATION_XML_VALUE},
            produces = {MediaType.APPLICATION_JSON_VALUE,MediaType.APPLICATION_XML_VALUE})
    public ResponseEntity<TelefoneVO> updateTelefone(
            @RequestHeader("Authorization") String authorizationHeader,
            @PathVariable Long telefoneId,
            @RequestBody @Valid TelefoneDto telefoneDto) throws NoSuchFieldException, InvocationTargetException, IllegalAccessException, InstantiationException, NoSuchMethodException {

        Usuario userSignedIn = usuarioService.getContatoByToken(authorizationHeader);


        Telefone updatedTelefone = telefoneService.updateTelefone(userSignedIn, telefoneId, telefoneDto);

        TelefoneVO telefoneVO = ConverterClass.convert(updatedTelefone, TelefoneVO.class);


        return ResponseEntity.status(HttpStatus.CREATED).body(telefoneVO);
    }

    @Operation(
            summary = "Buscar telefone por ID",
            description = "Retorna um telefone específico associado ao usuário autenticado, com base no ID fornecido.",
            parameters = {
                    @io.swagger.v3.oas.annotations.Parameter(
                            name = "Authorization",
                            description = "Token JWT do usuário autenticado. Deve ser enviado no cabeçalho da requisição.",
                            in = io.swagger.v3.oas.annotations.enums.ParameterIn.HEADER,
                            required = true,
                            schema = @Schema(type = "string")
                    ),
                    @io.swagger.v3.oas.annotations.Parameter(
                            name = "telefoneId",
                            description = "ID do telefone a ser retornado.",
                            in = io.swagger.v3.oas.annotations.enums.ParameterIn.PATH,
                            required = true,
                            schema = @Schema(type = "integer")
                    )
            },
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Telefone localizado com sucesso",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = TelefoneVO.class),
                                    examples = @ExampleObject(
                                            name = "Exemplo de resposta",
                                            value = """
                                                    {
                                                        "numero": "011988887777",
                                                        "tipo": "Comercial"
                                                    }
                                                    """
                                    )
                            )
                    )
            }
    )
    @GetMapping(value = "/{telefoneId}",
            produces = {MediaType.APPLICATION_JSON_VALUE,MediaType.APPLICATION_XML_VALUE})
    public ResponseEntity<TelefoneVO> getTelefoneById(
            @RequestHeader("Authorization") String authorizationHeader,
            @PathVariable Long telefoneId) throws NoSuchFieldException, InvocationTargetException, IllegalAccessException, InstantiationException, NoSuchMethodException {

        Usuario userSignedIn = usuarioService.getContatoByToken(authorizationHeader);


        Telefone telefone = telefoneService.getTelefoneById(userSignedIn, telefoneId);


        TelefoneVO telefoneVO = ConverterClass.convert(telefone, TelefoneVO.class);


        return ResponseEntity.ok(telefoneVO);
    }

    @Operation(
            summary = "Deletar um telefone",
            description = "Deleta um telefone existente do sistema, associando-o ao usuário autenticado.",
            parameters = {
                    @io.swagger.v3.oas.annotations.Parameter(
                            name = "Authorization",
                            description = "Token JWT do usuário autenticado. Deve ser enviado no cabeçalho da requisição.",
                            in = io.swagger.v3.oas.annotations.enums.ParameterIn.HEADER,
                            required = true,
                            schema = @Schema(type = "string")
                    ),
                    @io.swagger.v3.oas.annotations.Parameter(
                            name = "telefoneId",
                            description = "ID do telefone a ser deletado.",
                            in = io.swagger.v3.oas.annotations.enums.ParameterIn.PATH,
                            required = true,
                            schema = @Schema(type = "integer")
                    )
            },
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Telefone deletado com sucesso",
                            content = @Content(mediaType = "application/json")
                    )
            }
    )
    @DeleteMapping("/{telefoneId}")
    public ResponseEntity<String> deleteTelefone(
            @RequestHeader("Authorization") String authorizationHeader,
            @PathVariable Long telefoneId) {

        Usuario userSignedIn = usuarioService.getContatoByToken(authorizationHeader);


        telefoneService.deleteTelefone(userSignedIn, telefoneId);

        return new ResponseEntity<>("Usuario deletado com sucesso!", HttpStatus.OK);
    }


    @Operation(
            summary = "Buscar telefones por contato",
            description = "Retorna uma lista paginada de telefones associados a um contato específico do usuário autenticado.",
            parameters = {
                    @Parameter(
                            name = "Authorization",
                            description = "Token JWT do usuário autenticado. Deve ser enviado no cabeçalho da requisição.",
                            in = ParameterIn.HEADER,
                            required = true,
                            schema = @Schema(type = "string")
                    ),
                    @Parameter(
                            name = "contatoId",
                            description = "ID do contato para buscar os telefones associados.",
                            in = ParameterIn.PATH,
                            required = true,
                            schema = @Schema(type = "integer")
                    ),
                    @Parameter(
                            name = "page",
                            description = "Número da página para a paginação (default: 0).",
                            in = ParameterIn.QUERY,
                            required = false,
                            schema = @Schema(type = "integer", defaultValue = "0")
                    ),
                    @Parameter(
                            name = "size",
                            description = "Número de itens por página (default: 10).",
                            in = ParameterIn.QUERY,
                            required = false,
                            schema = @Schema(type = "integer", defaultValue = "10")
                    )
            },
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Telefones encontrados com sucesso",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = TelefoneVO.class),
                                    examples = @ExampleObject(
                                            name = "Exemplo de resposta",
                                            value = """
                                                    [
                                                        {
                                                            "numero": "011988887777",
                                                            "tipo": "Comercial"
                                                        },
                                                        {
                                                            "numero": "011999988888",
                                                            "tipo": "Residencial"
                                                        }
                                                    ]
                                                    """
                                    )
                            )
                    )
            }
    )
    @GetMapping(value = "/{contatoId}",
            produces = {MediaType.APPLICATION_JSON_VALUE,MediaType.APPLICATION_XML_VALUE})
    public Page<TelefoneVO> getTelefonesByContato(
            @RequestHeader("Authorization") String authorizationHeader,
            @PathVariable Long contatoId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {

        Usuario userSignedIn = usuarioService.getContatoByToken(authorizationHeader);

        Page<Telefone> telefones = telefoneService.getTelefonesByUsuario(contatoId, userSignedIn, page, size);

        return telefones.map(telefone -> {
            try {
                return ConverterClass.convert(telefone, TelefoneVO.class);
            } catch (Exception e) {
                throw new RuntimeException("Erro ao converter Telefone para TelefoneVO", e);
            }
        });
    }


}
