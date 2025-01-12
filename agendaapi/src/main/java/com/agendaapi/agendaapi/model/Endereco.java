package com.agendaapi.agendaapi.model;

import jakarta.persistence.*;

import java.util.Objects;

@Entity
public class Endereco {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String logradouro; // Ex: Rua, Avenida, etc.
    private String numero; // Número da casa/apartamento
    private String complemento; // Ex: Apartamento, Bloco, Andar, etc.
    private String bairro; // Bairro
    private String cidade; // Cidade
    private String estado; // Estado
    private String cep; // Código postal

    // Relacionamento muitos para um (um endereço pertence a um contato)
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

    public String getLogradouro() {
        return logradouro;
    }

    public void setLogradouro(String logradouro) {
        this.logradouro = logradouro;
    }

    public String getNumero() {
        return numero;
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }

    public String getComplemento() {
        return complemento;
    }

    public void setComplemento(String complemento) {
        this.complemento = complemento;
    }

    public String getBairro() {
        return bairro;
    }

    public void setBairro(String bairro) {
        this.bairro = bairro;
    }

    public String getCidade() {
        return cidade;
    }

    public void setCidade(String cidade) {
        this.cidade = cidade;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getCep() {
        return cep;
    }

    public void setCep(String cep) {
        this.cep = cep;
    }

    public Contato getContato() {
        return contato;
    }

    public void setContato(Contato contato) {
        this.contato = contato;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Endereco endereco = (Endereco) o;
        return Objects.equals(id, endereco.id) && Objects.equals(logradouro, endereco.logradouro) && Objects.equals(numero, endereco.numero) && Objects.equals(complemento, endereco.complemento) && Objects.equals(bairro, endereco.bairro) && Objects.equals(cidade, endereco.cidade) && Objects.equals(estado, endereco.estado) && Objects.equals(cep, endereco.cep) && Objects.equals(contato, endereco.contato);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, logradouro, numero, complemento, bairro, cidade, estado, cep, contato);
    }
}
