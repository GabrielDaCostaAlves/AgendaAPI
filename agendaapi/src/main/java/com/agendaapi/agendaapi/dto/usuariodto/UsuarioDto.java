package com.agendaapi.agendaapi.dto.usuariodto;

import com.agendaapi.agendaapi.domain.model.enums.RoleName;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

@JacksonXmlRootElement(localName = "UsuarioDto")
public record UsuarioDto(

        @NotBlank(message = "O nome não pode ser vazio.")
        @Size(min = 2, max = 50, message = "O nome deve ter entre 2 e 50 caracteres.")
        @JsonProperty("nome")
        String nome,

        @NotBlank(message = "O email não pode ser vazio.")
        @Email(message = "O email deve ser válido.")
        @JsonProperty("email")
        String email,

        @NotBlank(message = "A senha não pode ser vazia.")
        @Size(min = 6, max = 20, message = "A senha deve ter entre 6 e 20 caracteres.")
        @JsonProperty("password")
        String password,

        @NotNull(message = "O role não pode ser nulo.")
        @JsonProperty("role")
        RoleName role
) {
}
