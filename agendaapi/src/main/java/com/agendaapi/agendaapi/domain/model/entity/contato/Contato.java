package com.agendaapi.agendaapi.domain.model.entity.contato;

import com.agendaapi.agendaapi.domain.model.entity.usuario.Usuario;
import jakarta.persistence.*;
import lombok.Builder;

import java.time.LocalDate;
import java.util.Objects;

@Entity
@Builder
public class Contato {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nome;

    @Column(name = "data_nascimento")
    private LocalDate dataNascimento;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario usuario;

    public Contato(){}


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

    public LocalDate getDataNascimento() {
        return dataNascimento;
    }

    public void setDataNascimento(LocalDate dataNascimento) {
        this.dataNascimento = dataNascimento;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Contato contato = (Contato) o;
        return Objects.equals(id, contato.id) && Objects.equals(nome, contato.nome) && Objects.equals(dataNascimento, contato.dataNascimento) && Objects.equals(usuario, contato.usuario);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, nome, dataNascimento, usuario);
    }
}
