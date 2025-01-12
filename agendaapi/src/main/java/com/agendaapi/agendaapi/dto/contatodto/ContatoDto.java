package com.agendaapi.agendaapi.dto.contatodto;


import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;

import java.time.LocalDate;

public record ContatoDto(

        @NotNull(message = "O nome do contato não pode ser nulo.")
        String nome,  // Nome do contato (obrigatório)

        @NotNull(message = "A data de nascimento não pode ser nula.")
        @Past(message = "A data de nascimento deve ser no passado.")
        LocalDate dataDeNascimento,// Data de nascimento (obrigatória, deve ser no passado)


        TelefoneDto telefone, // Para um telefone associado ao contato

        EnderecoDto endereco // Para um endereço associado ao contato

) {
}
