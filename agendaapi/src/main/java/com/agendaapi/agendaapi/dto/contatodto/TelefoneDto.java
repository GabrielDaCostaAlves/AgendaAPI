package com.agendaapi.agendaapi.dto.contatodto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;


public record TelefoneDto(
        @NotBlank(message = "Número de telefone é obrigatório")
        String numero,
        @NotBlank(message = "Tipo de telefone é obrigatório")
        @Pattern(regexp = "^(\\(\\d{2}\\)\\s?)?\\d{4,5}-\\d{4}$", message = "O número de telefone deve ser válido (formato: (11) 91234-5678 ou 11912345678).")
        String tipo
) {
}
