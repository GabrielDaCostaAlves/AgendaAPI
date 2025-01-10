package com.agendaapi.agendaapi.dto;

import com.agendaapi.agendaapi.model.RoleName;

public record CreateUserDto(
        String nome,
        String email,
        String password,
        RoleName role
) {
}