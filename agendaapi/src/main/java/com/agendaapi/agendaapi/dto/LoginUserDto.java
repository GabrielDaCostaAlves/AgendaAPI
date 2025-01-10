package com.agendaapi.agendaapi.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record LoginUserDto(

        @NotBlank(message = "O email não pode ser vazio.")
        @Email(message = "O email deve ser válido.")
        String email,

        @NotBlank(message = "A senha não pode ser vazia.")
        String password

) {
}
