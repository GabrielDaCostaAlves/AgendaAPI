package com.agendaapi.agendaapi.vo;

import com.agendaapi.agendaapi.domain.model.enums.RoleName;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.RepresentationModel;

import java.time.ZonedDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@JacksonXmlRootElement(localName = "UsuarioVO")
public class UsuarioVO extends RepresentationModel<UsuarioVO> {

    @JsonProperty("id")
    private String key;

    @JsonProperty("nome")
    private String nome;

    @JsonProperty("email")
    private String email;

    @JsonProperty("criadoEm")
    private ZonedDateTime criadoEm;

    @JsonProperty("role")
    private RoleName role;

    private EntityModel<UsuarioVO> usuarioLinks;

    public void setUsuarioLinks(EntityModel<UsuarioVO> usuarioLinks) {
        this.usuarioLinks = usuarioLinks;
    }
}
