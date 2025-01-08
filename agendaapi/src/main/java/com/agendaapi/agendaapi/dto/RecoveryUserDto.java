package com.agendaapi.agendaapi.dto;

import com.agendaapi.agendaapi.model.Role;

import java.util.List;

public record RecoveryUserDto(

        Long id,
        String email,
        List<Role> roles

) {
}