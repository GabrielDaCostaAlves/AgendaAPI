package com.agendaapi.agendaapi.dto.contatodto;


import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;

import java.time.LocalDate;

public record ContatoDto(

        @NotNull(message = "O nome do contato não pode ser nulo.")
        String nome,

        @NotNull(message = "A data de nascimento não pode ser nula.")
        @Past(message = "A data de nascimento deve ser no passado.")
        LocalDate dataDeNascimento,


        TelefoneDto telefone,

        EnderecoDto endereco

) {
}
