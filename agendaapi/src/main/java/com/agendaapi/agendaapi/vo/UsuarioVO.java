package com.agendaapi.agendaapi.vo;


import com.agendaapi.agendaapi.model.RoleName;


import java.time.ZonedDateTime;

public class UsuarioVO {

    private String nome;

    private String email;

    private ZonedDateTime criadoEm;

    private RoleName role;

    public UsuarioVO(){

    }
    public String getNome() {
        return nome;
    }

    public String getEmail() {
        return email;
    }

    public ZonedDateTime getCriadoEm() {
        return criadoEm;
    }

    public RoleName getRole() {
        return role;
    }
}
