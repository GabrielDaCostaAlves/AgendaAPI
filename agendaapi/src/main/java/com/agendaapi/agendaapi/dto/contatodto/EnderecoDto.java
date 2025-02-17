package com.agendaapi.agendaapi.dto.contatodto;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record EnderecoDto(
        @NotBlank(message = "Logradouro é obrigatório")
        String logradouro,
        @NotBlank(message = "Número é obrigatório")
        String numero,
        String complemento,
        @NotBlank(message = "Bairro é obrigatório")
        String bairro,
        @NotBlank(message = "Cidade é obrigatório")
        String cidade,
        @NotBlank(message = "Estado é obrigatório")
        String estado,
        @NotBlank(message = "CEP é obrigatório")
        @Pattern(regexp = "^[0-9]{5}-[0-9]{3}$", message = "O CEP deve ser válido (formato: 12345-678).")
        String cep) {
}

