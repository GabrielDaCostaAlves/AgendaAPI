package com.agendaapi.agendaapi.model;



import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Builder;

import java.time.ZonedDateTime;


@Entity
@Builder
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "O nome não pode ser vazio.")
    private String nome;

    @NotBlank(message = "O email não pode ser vazio.")
    @Email(message = "O email deve ser válido.")
    @Column(unique = true)
    private String email;


    @NotBlank(message = "A senha não pode ser vazia.")
    @Size(min = 6, message = "A senha deve ter pelo menos 6 caracteres.")
    @Column(name = "password")
    private String password;


    @Column(name = "criado_em")
    private ZonedDateTime criadoEm;  // Novo campo para data de criação

    // Relacionamento muitos para um (um usuário tem uma única role)
    @ManyToOne(fetch = FetchType.EAGER) // EAGER se você quiser carregar a role junto ao usuário
    @JoinColumn(name = "role_id", nullable = false) // Chave estrangeira para a role
    private Role role;



    public Usuario() {

    }
    public Usuario(String nome,String email, String password, Role role) {
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

    public @NotBlank(message = "O nome não pode ser vazio.") String getNome() {
        return nome;
    }

    public void setNome(@NotBlank(message = "O nome não pode ser vazio.") String nome) {
        this.nome = nome;
    }

    public @NotBlank(message = "O email não pode ser vazio.") @Email(message = "O email deve ser válido.") String getEmail() {
        return email;
    }

    public void setEmail(@NotBlank(message = "O email não pode ser vazio.") @Email(message = "O email deve ser válido.") String email) {
        this.email = email;
    }

    public @NotBlank(message = "A senha não pode ser vazia.") @Size(min = 6, message = "A senha deve ter pelo menos 6 caracteres.") String getPassword() {
        return password;
    }

    public void setPassword(@NotBlank(message = "A senha não pode ser vazia.") @Size(min = 6, message = "A senha deve ter pelo menos 6 caracteres.") String password) {
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
