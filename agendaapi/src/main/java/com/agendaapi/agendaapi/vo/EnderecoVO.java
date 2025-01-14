package com.agendaapi.agendaapi.vo;

import org.springframework.stereotype.Component;

@Component
public class EnderecoVO {

    private String logradouro; // Ex: Rua, Avenida, etc.
    private String numero; // Número da casa/apartamento
    private String complemento; // Ex: Apartamento, Bloco, Andar, etc.
    private String bairro; // Bairro
    private String cidade; // Cidade
    private String estado; // Estado
    private String cep; // Código postal

}
