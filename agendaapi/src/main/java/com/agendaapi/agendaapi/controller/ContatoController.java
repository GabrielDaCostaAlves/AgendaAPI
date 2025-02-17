package com.agendaapi.agendaapi.controller;

import com.agendaapi.agendaapi.dto.contatodto.ContatoDto;
import com.agendaapi.agendaapi.dto.contatodto.ContatoUpdateDto;
import com.agendaapi.agendaapi.domain.model.entity.contato.Contato;
import com.agendaapi.agendaapi.domain.model.entity.usuario.Usuario;
import com.agendaapi.agendaapi.domain.service.ContatoService;
import com.agendaapi.agendaapi.domain.service.UsuarioService;
import com.agendaapi.agendaapi.util.conversor.ConverterClass;
import com.agendaapi.agendaapi.vo.ContatoVO;
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

@Tag(name = "Contato", description = "Gerenciamento de Contatos")
@RestController
@RequestMapping("/v1/agenda/contatos")
public class ContatoController {

    @Autowired
    private ContatoService contatoService;


    @Autowired
    private UsuarioService usuarioService;



    @Operation(
            summary = "Criar um contato",
            description = "Recebe os dados de um contato e o cadastra no sistema.",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Dados necessários para criar um novo contato",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ContatoDto.class),
                            examples = @ExampleObject(
                                    name = "Exemplo de entrada",
                                    value = """
                                        {
                                            "nome": "Teste de contato",
                                            "dataDeNascimento": "2000-01-24",
                                            "telefone": {
                                                "numero": "01199999-9999",
                                                "tipo": "Celular"
                                            },
                                            "endereco": {
                                                "logradouro": "Rua Teste",
                                                "numero": "01",
                                                "complemento": "Casa",
                                                "bairro": "Teste",
                                                "cidade": "Teste",
                                                "estado": "Teste",
                                                "cep": "00000-000"
                                            }
                                        }
                                        """
                            )
                    )
            ),
            responses = {
                    @ApiResponse(
                            responseCode = "201",
                            description = "Contato criado com sucesso",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = ContatoVO.class),
                                    examples = @ExampleObject(
                                            name = "Exemplo de resposta",
                                            value = """
                                                {
                                                    "nome": "Teste de contato",
                                                    "dataDeNascimento": "2000-01-24"
                                                }
                                                """
                                    )
                            )
                    )
            }
    )
    @PostMapping(
            consumes =  {MediaType.APPLICATION_JSON_VALUE,MediaType.APPLICATION_XML_VALUE},
            produces = {MediaType.APPLICATION_JSON_VALUE,MediaType.APPLICATION_XML_VALUE})
    public ResponseEntity<ContatoVO> createContato(
            @RequestHeader("Authorization") String authorizationHeader,
            @RequestBody @Valid ContatoDto contatoDto) throws NoSuchFieldException, InvocationTargetException, IllegalAccessException, InstantiationException, NoSuchMethodException {

        Usuario userSignedIn = usuarioService.getContatoByToken(authorizationHeader);


        Contato contato = contatoService.createContato(userSignedIn, contatoDto);
        ContatoVO contatoVO = ConverterClass.convert(contato, ContatoVO.class);


        return ResponseEntity.status(HttpStatus.CREATED).body(contatoVO);
    }

    @Operation(
            summary = "Atualizar um contato",
            description = "Recebe os dados atualizados de um contato existente e realiza a atualização no sistema.",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Dados necessários para atualizar o contato",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ContatoDto.class),
                            examples = @ExampleObject(
                                    name = "Exemplo de entrada",
                                    value = """
                                        {
                                            "nome": "Teste de contato atualizado",
                                            "dataDeNascimento": "1999-05-10"
                                        }
                                        """
                            )
                    )
            ),
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Contato atualizado com sucesso",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = ContatoVO.class),
                                    examples = @ExampleObject(
                                            name = "Exemplo de resposta",
                                            value = """
                                                {
                                                    "nome": "Teste de contato atualizado",
                                                    "dataDeNascimento": "1999-05-10"
                                                }
                                                """
                                    )
                            )
                    )
            }
    )
    @PutMapping(value = "/{contatoId}",
            consumes =  {MediaType.APPLICATION_JSON_VALUE,MediaType.APPLICATION_XML_VALUE},
            produces = {MediaType.APPLICATION_JSON_VALUE,MediaType.APPLICATION_XML_VALUE})
    public ResponseEntity<ContatoVO> updateContato(
            @RequestHeader("Authorization") String authorizationHeader,
            @PathVariable Long contatoId,
            @RequestBody @Valid ContatoUpdateDto contatoDto) throws NoSuchFieldException, InvocationTargetException, IllegalAccessException, InstantiationException, NoSuchMethodException {

        Usuario userSignedIn = usuarioService.getContatoByToken(authorizationHeader);


        Contato updatedContato = contatoService.updateContato(userSignedIn, contatoId, contatoDto);
        ContatoVO contatoVO = ConverterClass.convert(updatedContato, ContatoVO.class);


        return ResponseEntity.status(200).body(contatoVO);
    }

    @Operation(
            summary = "Buscar um contato pelo ID",
            description = "Retorna os dados de um contato específico, desde que pertença ao usuário autenticado.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Contato encontrado com sucesso",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = ContatoVO.class),
                                    examples = @ExampleObject(
                                            name = "Exemplo de resposta",
                                            value = """
                    {
                        "nome": "Teste de contato",
                        "dataDeNascimento": "1999-05-10"
                    }
                    """
                                    )
                            )
                    ),
                    @ApiResponse(responseCode = "404", description = "Contato não encontrado"),
                    @ApiResponse(responseCode = "403", description = "Acesso negado")
            }
    )
    @GetMapping(value = "/{contatoId}",
            produces = {MediaType.APPLICATION_JSON_VALUE,MediaType.APPLICATION_XML_VALUE})
    public ResponseEntity<ContatoVO> getContatoById(
            @RequestHeader("Authorization") String authorizationHeader,
            @PathVariable Long contatoId) throws NoSuchFieldException, InvocationTargetException, IllegalAccessException, InstantiationException, NoSuchMethodException {

        Usuario userSignedIn = usuarioService.getContatoByToken(authorizationHeader);

        Contato contato = contatoService.getContatoById(userSignedIn, contatoId);
        ContatoVO contatoVO = ConverterClass.convert(contato, ContatoVO.class);


        return ResponseEntity.status(200).body(contatoVO);
    }

    @Operation(
            summary = "Deleta um contato",
            description = "Deleta um contato",
            parameters = {
                    @Parameter(
                            name = "Authorization",
                            in = ParameterIn.HEADER,
                            description = "Token de autenticação para validar o usuário",
                            required = true,
                            schema = @Schema(type = "string")
                    ),
                    @Parameter(
                            name = "contatoId",
                            in = ParameterIn.PATH,
                            description = "ID do contato a ser atualizado",
                            required = true,
                            schema = @Schema(type = "integer", format = "int64")
                    )
            },
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Contato deletado com sucesso",
                            content = @Content(
                                    mediaType = "application/json",
                                    examples = @ExampleObject(
                                            name = "Exemplo de resposta",
                                            value = """
                            {
                                "message": "Contato deletado com sucesso!"
                            }
                            """
                                    )
                            )
                    )
            }
    )
    @DeleteMapping("/{contatoId}")
    public ResponseEntity<String> deleteContato(
            @RequestHeader("Authorization") String authorizationHeader,
            @PathVariable Long contatoId) {

        Usuario userSignedIn = usuarioService.getContatoByToken(authorizationHeader);
        contatoService.deleteContato(userSignedIn, contatoId);

        return new ResponseEntity<>("Contato deletado com sucesso!", HttpStatus.OK);
    }

    @Operation(
            summary = "Buscar todos os contatos do usuário",
            description = "Retorna uma lista paginada de todos os contatos associados ao usuário autenticado.",
            parameters = {
                    @Parameter(
                            name = "Authorization",
                            description = "Token JWT do usuário autenticado. Deve ser enviado no cabeçalho da requisição.",
                            in = ParameterIn.HEADER,
                            required = true,
                            schema = @Schema(type = "string")
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
                            description = "Contatos encontrados com sucesso",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = ContatoVO.class),
                                    examples = @ExampleObject(
                                            name = "Exemplo de resposta",
                                            value = """
                                                    [
                                                        {
                                                            "id": 1,
                                                            "nome": "João Silva",
                                                            "dataNascimento": "1980-05-15",
                                                            "telefones": [
                                                                {"numero": "011998877665", "tipo": "Celular"}
                                                            ],
                                                            "enderecos": [
                                                                {"logradouro": "Rua A", "numero": "123", "bairro": "Centro", "cidade": "São Paulo", "estado": "SP", "cep": "01000-000"}
                                                            ]
                                                        },
                                                        {
                                                            "id": 2,
                                                            "nome": "Maria Oliveira",
                                                            "dataNascimento": "1990-08-20",
                                                            "telefones": [
                                                                {"numero": "011987654321", "tipo": "Residencial"}
                                                            ],
                                                            "enderecos": [
                                                                {"logradouro": "Rua B", "numero": "456", "bairro": "Jardim", "cidade": "São Paulo", "estado": "SP", "cep": "02000-000"}
                                                            ]
                                                        }
                                                    ]
                                                    """
                                    )
                            )
                    )
            }
    )
    @GetMapping(
            produces = {MediaType.APPLICATION_JSON_VALUE,MediaType.APPLICATION_XML_VALUE})
    public Page<ContatoVO> getAllContatos(
            @RequestHeader("Authorization") String authorizationHeader,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Usuario userSignedIn = usuarioService.getContatoByToken(authorizationHeader);
        return contatoService.getAllContatos(userSignedIn, page, size);
    }
}
