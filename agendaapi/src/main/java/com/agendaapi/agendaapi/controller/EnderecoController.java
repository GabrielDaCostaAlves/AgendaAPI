package com.agendaapi.agendaapi.controller;

import com.agendaapi.agendaapi.dto.contatodto.EnderecoDto;
import com.agendaapi.agendaapi.domain.model.entity.contato.Endereco;
import com.agendaapi.agendaapi.domain.model.entity.usuario.Usuario;
import com.agendaapi.agendaapi.domain.service.EnderecoService;
import com.agendaapi.agendaapi.domain.service.UsuarioService;
import com.agendaapi.agendaapi.util.conversor.ConverterClass;
import com.agendaapi.agendaapi.vo.EnderecoVO;
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


@Tag(name = "Endereço", description = "Gerenciamento de Endereços")
@RestController
@RequestMapping("/v1/agenda/enderecos")
public class EnderecoController {

    @Autowired
    private EnderecoService enderecoService;

    @Autowired
    private UsuarioService usuarioService;


    @Operation(
            summary = "Criar um endereço",
            description = "Recebe os dados de um endereço e o cadastra no sistema para um contato específico.",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Dados necessários para criar um novo endereço para um contato",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = EnderecoDto.class),
                            examples = @ExampleObject(
                                    name = "Exemplo de entrada",
                                    value = """
                                {
                                    "logradouro": "Rua Teste",
                                    "numero": "01",
                                    "complemento": "Casa",
                                    "bairro": "Teste",
                                    "cidade": "Teste",
                                    "estado": "Teste",
                                    "cep": "00000-000"
                                }
                                """
                            )
                    )
            ),
            responses = {
                    @ApiResponse(
                            responseCode = "201",
                            description = "Endereço criado com sucesso",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = EnderecoVO.class),
                                    examples = @ExampleObject(
                                            name = "Exemplo de resposta",
                                            value = """
                                        {
                                            "logradouro": "Rua Teste",
                                            "numero": "01",
                                            "complemento": "Casa",
                                            "bairro": "Teste",
                                            "cidade": "Teste",
                                            "estado": "Teste",
                                            "cep": "00000-000"
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
    public ResponseEntity<EnderecoVO> createEndereco(
            @RequestHeader("Authorization") String authorizationHeader,
            @RequestBody @Valid EnderecoDto enderecoDto,
            @PathVariable Long contatoId) throws NoSuchFieldException, InvocationTargetException, IllegalAccessException, InstantiationException, NoSuchMethodException {

        Usuario userSignedIn = usuarioService.getContatoByToken(authorizationHeader);


        Endereco savedEndereco = enderecoService.createEndereco(userSignedIn, enderecoDto, contatoId);
        EnderecoVO enderecoVO = ConverterClass.convert(savedEndereco, EnderecoVO.class);


        return ResponseEntity.status(HttpStatus.CREATED).body(enderecoVO);
    }

    @Operation(
            summary = "Atualizar um endereço",
            description = "Recebe os dados atualizados de um endereço e os aplica a um endereço existente no sistema.",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Dados necessários para atualizar um endereço existente",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = EnderecoDto.class),
                            examples = @ExampleObject(
                                    name = "Exemplo de entrada",
                                    value = """
                                {
                                    "logradouro": "Rua Atualizada",
                                    "numero": "123",
                                    "complemento": "Apto 101",
                                    "bairro": "Centro",
                                    "cidade": "Cidade Atualizada",
                                    "estado": "Estado Atualizado",
                                    "cep": "11111-222"
                                }
                                """
                            )
                    )
            ),
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Endereço atualizado com sucesso",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = EnderecoVO.class),
                                    examples = @ExampleObject(
                                            name = "Exemplo de resposta",
                                            value = """
                                        {
                                            "logradouro": "Rua Atualizada",
                                            "numero": "123",
                                            "complemento": "Apto 101",
                                            "bairro": "Centro",
                                            "cidade": "Cidade Atualizada",
                                            "estado": "Estado Atualizado",
                                            "cep": "11111-222"
                                        }
                                        """
                                    )
                            )
                    )
            }
    )
    @PutMapping(value = "/{enderecoId}",
            consumes =  {MediaType.APPLICATION_JSON_VALUE,MediaType.APPLICATION_XML_VALUE},
            produces = {MediaType.APPLICATION_JSON_VALUE,MediaType.APPLICATION_XML_VALUE})
    public ResponseEntity<EnderecoVO> updateEndereco(
            @RequestHeader("Authorization") String authorizationHeader,
            @PathVariable Long enderecoId,
            @RequestBody @Valid EnderecoDto enderecoDto) throws NoSuchFieldException, InvocationTargetException, IllegalAccessException, InstantiationException, NoSuchMethodException {

        Usuario userSignedIn = usuarioService.getContatoByToken(authorizationHeader);


        Endereco updatedEndereco = enderecoService.updateEndereco(userSignedIn, enderecoId, enderecoDto);
        EnderecoVO enderecoVO = ConverterClass.convert(updatedEndereco, EnderecoVO.class);


        return ResponseEntity.status(HttpStatus.CREATED).body(enderecoVO);
    }

    @Operation(
            summary = "Excluir um endereço",
            description = "Recebe um ID de endereço e exclui o endereço correspondente do sistema.",
            parameters = {
                    @Parameter(
                            name = "Authorization",
                            description = "Token de autenticação do usuário",
                            required = true,
                            in = ParameterIn.HEADER
                    ),
                    @Parameter(
                            name = "enderecoId",
                            description = "ID do endereço a ser excluído",
                            required = true,
                            in = ParameterIn.PATH
                    )
            },
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Endereço excluído com sucesso",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = String.class),
                                    examples = @ExampleObject(
                                            name = "Exemplo de resposta",
                                            value = """
                                        "Endereço deletado com sucesso!"
                                        """
                                    )
                            )
                    )
            }
    )
    @DeleteMapping("/{enderecoId}")
    public ResponseEntity<String> deleteEndereco(
            @RequestHeader("Authorization") String authorizationHeader,
            @PathVariable Long enderecoId) {

        String response;
        Usuario userSignedIn = usuarioService.getContatoByToken(authorizationHeader);


        enderecoService.deleteEndereco(userSignedIn, enderecoId);


        return new ResponseEntity<>("Usuario deletado com sucesso!", HttpStatus.OK);
    }


    @Operation(
            summary = "Buscar endereço por ID",
            description = "Recebe o ID de um endereço e retorna os detalhes do endereço associado ao usuário autenticado.",
            parameters = {
                    @Parameter(
                            name = "enderecoId",
                            in = ParameterIn.PATH,
                            description = "ID do endereço a ser recuperado",
                            required = true,
                            schema = @Schema(type = "integer", example = "1")
                    ),
                    @Parameter(
                            name = "Authorization",
                            in = ParameterIn.HEADER,
                            description = "Token de autenticação JWT do usuário",
                            required = true,
                            schema = @Schema(type = "string", example = "Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...")
                    )
            },
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Endereço encontrado com sucesso",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = EnderecoVO.class),
                                    examples = @ExampleObject(
                                            name = "Exemplo de resposta",
                                            value = """
                        {
                            "logradouro": "Rua Teste",
                            "numero": "01",
                            "complemento": "Casa",
                            "bairro": "Teste",
                            "cidade": "Teste",
                            "estado": "Teste",
                            "cep": "00000-000"
                        }
                    """
                                    )
                            )
                    )
            }
    )
    @GetMapping(value = "/{enderecoId}",
            produces = {MediaType.APPLICATION_JSON_VALUE,MediaType.APPLICATION_XML_VALUE})
    public ResponseEntity<EnderecoVO> getEnderecoById(
            @RequestHeader("Authorization") String authorizationHeader,
            @PathVariable Long enderecoId) throws NoSuchFieldException, InvocationTargetException, IllegalAccessException, InstantiationException, NoSuchMethodException {

        Usuario userSignedIn = usuarioService.getContatoByToken(authorizationHeader);


        Endereco endereco = enderecoService.getEnderecoById(userSignedIn, enderecoId);


        EnderecoVO enderecoVO = ConverterClass.convert(endereco, EnderecoVO.class);

        return ResponseEntity.ok(enderecoVO);
    }

    @Operation(
            summary = "Buscar endereços por contato",
            description = "Retorna uma lista paginada de endereços associados a um contato específico do usuário autenticado.",
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
                            description = "ID do contato para buscar os endereços associados.",
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
                            description = "Endereços encontrados com sucesso",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = EnderecoVO.class),
                                    examples = @ExampleObject(
                                            name = "Exemplo de resposta",
                                            value = """
                                                    [
                                                        {
                                                            "logradouro": "Rua A",
                                                            "numero": "123",
                                                            "bairro": "Centro",
                                                            "cidade": "São Paulo",
                                                            "estado": "SP",
                                                            "cep": "01000-000"
                                                        },
                                                        {
                                                            "logradouro": "Rua B",
                                                            "numero": "456",
                                                            "bairro": "Jardim",
                                                            "cidade": "São Paulo",
                                                            "estado": "SP",
                                                            "cep": "02000-000"
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
    public Page<EnderecoVO> getEnderecosByContato(
            @RequestHeader("Authorization") String authorizationHeader,
            @PathVariable Long contatoId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        Usuario userSignedIn = usuarioService.getContatoByToken(authorizationHeader);


        Page<Endereco> enderecos = enderecoService.getEnderecosByContato(contatoId, userSignedIn, page, size);
        return enderecos.map(endereco -> {
            try {
                return ConverterClass.convert(endereco, EnderecoVO.class);
            } catch (Exception e) {
                throw new RuntimeException("Erro ao converter Endereco para EnderecoVO", e);
            }
        });
    }
}
