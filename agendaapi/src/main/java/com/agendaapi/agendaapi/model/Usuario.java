package com.agendaapi.agendaapi.model;

import jakarta.persistence.*;
import lombok.Builder;

import java.time.ZonedDateTime;

@Entity
@Builder
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nome;

    @Column(unique = true)
    private String email;

    @Column(name = "password")
    private String password;

    @Column(name = "criado_em")
    private ZonedDateTime criadoEm; // Campo para data de criação

    // Relacionamento muitos para um (um usuário tem uma única role)
    @ManyToOne(fetch = FetchType.EAGER) // EAGER se você quiser carregar a role junto ao usuário
    @JoinColumn(name = "role_id", nullable = false) // Chave estrangeira para a role
    private Role role;

    public Usuario() {

    }

    public Usuario(String nome, String email, String password, Role role) {
        this.nome = nome;
        this.email = email;
        this.password = password;
        this.role = role;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public ZonedDateTime getCriadoEm() {
        return criadoEm;
    }

    public void setCriadoEm(ZonedDateTime criadoEm) {
        this.criadoEm = criadoEm;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }
}
