package com.agendaapi.agendaapi.controller;

import com.agendaapi.agendaapi.dto.contatodto.ContatoDto;
import com.agendaapi.agendaapi.dto.contatodto.ContatoUpdateDto;
import com.agendaapi.agendaapi.model.Contato;
import com.agendaapi.agendaapi.model.Usuario;
import com.agendaapi.agendaapi.service.ContatoService;
import com.agendaapi.agendaapi.service.UsuarioService;
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
import org.springframework.http.HttpStatus;
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
    @PostMapping("/createcontato")
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
    @PutMapping("/{contatoId}")
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
                                        "dataDeNascimento": "1999-05-10",
                                        "telefone": {
                                            "numero": "01199999-8888",
                                            "tipo": "Celular"
                                        },
                                        "endereco": {
                                            "logradouro": "Rua Atualizada",
                                            "numero": "100",
                                            "complemento": "Apartamento 10",
                                            "bairro": "Atualizado",
                                            "cidade": "Atualizado",
                                            "estado": "Atualizado",
                                            "cep": "11111-111"
                                        }
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
    @GetMapping("/{contatoId}")
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

    //todo: Endpoint para listar contatos do usuario.
}
