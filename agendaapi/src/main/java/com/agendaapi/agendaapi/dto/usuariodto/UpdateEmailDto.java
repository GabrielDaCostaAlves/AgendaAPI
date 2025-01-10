package com.agendaapi.agendaapi.dto.usuariodto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record UpdateEmailDto(

        @NotBlank(message = "O email atual não pode ser vazio.")
        @Email(message = "O email atual deve ser válido.")
        String email,

        @NotBlank(message = "O novo email não pode ser vazio.")
        @Email(message = "O novo email deve ser válido.")
        String newemail

) {
}
