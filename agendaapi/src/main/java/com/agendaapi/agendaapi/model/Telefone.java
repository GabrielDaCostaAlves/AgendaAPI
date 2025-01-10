package com.agendaapi.agendaapi.model;

import jakarta.persistence.*;

@Entity
public class Telefone {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String numero;

    private String tipo; // Ex: Celular, Residencial, Comercial

    // Relacionamento muitos para um (um telefone pertence a um contato)
    @ManyToOne(fetch = FetchType.LAZY)  // Usando LAZY para carregar o contato somente quando necessário
    @JoinColumn(name = "contato_id", nullable = false)  // Chave estrangeira para o contato
    private Contato contato;

    // Getters e Setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNumero() {
        return numero;
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public Contato getContato() {
        return contato;
    }

    public void setContato(Contato contato) {
        this.contato = contato;
    }
}
