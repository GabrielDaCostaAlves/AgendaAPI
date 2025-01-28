package com.agendaapi.agendaapi.vo;

import com.agendaapi.agendaapi.model.enums.RoleName;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.ZonedDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@JacksonXmlRootElement(localName = "UsuarioVO") // Define o nome do elemento raiz no XML
public class UsuarioVO {

    @JsonProperty("nome") // Define a propriedade para JSON e XML
    private String nome;

    @JsonProperty("email")
    private String email;

    @JsonProperty("criadoEm")
    private ZonedDateTime criadoEm;

    @JsonProperty("role")
    private RoleName role;
}
