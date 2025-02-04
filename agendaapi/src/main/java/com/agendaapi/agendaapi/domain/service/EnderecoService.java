package com.agendaapi.agendaapi.domain.service;

import com.agendaapi.agendaapi.dto.contatodto.EnderecoDto;
import com.agendaapi.agendaapi.domain.model.entity.contato.Endereco;
import com.agendaapi.agendaapi.domain.model.entity.contato.Contato;
import com.agendaapi.agendaapi.domain.model.entity.usuario.Usuario;
import com.agendaapi.agendaapi.domain.repository.EnderecoRepository;
import com.agendaapi.agendaapi.domain.repository.ContatoRepository;
import com.agendaapi.agendaapi.security.JwtTokenService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
public class EnderecoService {

    @Autowired
    private EnderecoRepository enderecoRepository;

    @Autowired
    private ContatoRepository contatoRepository;

    @Autowired
    private JwtTokenService jwtTokenService;

    public Endereco createEndereco(Usuario userSignedIn, EnderecoDto enderecoDto, Long contatoId) {

        if (userSignedIn == null || contatoId == null || enderecoDto == null) {
            throw new IllegalArgumentException("Usuário, ID do contato ou Json não podem ser nulos");
        }

        Contato contato = contatoRepository.findById(contatoId)
                .orElseThrow(() -> new RuntimeException("Contato não encontrado"));


        if (Objects.equals(userSignedIn.getEmail(), contato.getUsuario().getEmail())) {


            try {
                Endereco endereco = new Endereco();
                endereco.setLogradouro(enderecoDto.logradouro());
                endereco.setNumero(enderecoDto.numero());
                endereco.setComplemento(enderecoDto.complemento());
                endereco.setBairro(enderecoDto.bairro());
                endereco.setCidade(enderecoDto.cidade());
                endereco.setEstado(enderecoDto.estado());
                endereco.setCep(enderecoDto.cep());
                endereco.setContato(contato);


                return enderecoRepository.save(endereco);

            } catch (RuntimeException e) {
                throw new RuntimeException("Ocorreu um erro ao tentar atualizaro endereco, erro: " + e);
            }
        }
        return null;
    }

    public Endereco updateEndereco(Usuario userSignedIn, Long enderecoId, EnderecoDto enderecoDto) {

        if (userSignedIn == null || enderecoId == null || enderecoDto == null) {
            throw new IllegalArgumentException("Usuário, ID do endereço ou Json não podem ser nulos");
        }
        Endereco endereco = enderecoRepository.findById(enderecoId)
                .orElseThrow(() -> new RuntimeException("Endereço não encontrado"));


        if (Objects.equals(endereco.getContato().getUsuario().getEmail(), userSignedIn.getEmail())) {


            try {
                endereco.setLogradouro(enderecoDto.logradouro());
                endereco.setNumero(enderecoDto.numero());
                endereco.setComplemento(enderecoDto.complemento());
                endereco.setBairro(enderecoDto.bairro());
                endereco.setCidade(enderecoDto.cidade());
                endereco.setEstado(enderecoDto.estado());
                endereco.setCep(enderecoDto.cep());

                return enderecoRepository.save(endereco);
            } catch (RuntimeException e) {
                throw new RuntimeException("Ocorreu um erro ao tentar atualizaro endereco, erro: " + e);
            }
        }
        return null;
    }

    public void deleteEndereco(Usuario userSignidIn, Long enderecoId) {

        if (userSignidIn == null || enderecoId == null) {
            throw new IllegalArgumentException("Usuário, ID do endereço não podem ser nulos");
        }

        Endereco endereco = enderecoRepository.findById(enderecoId)
                .orElseThrow(() -> new RuntimeException("Telefone não encontrado"));


        if (Objects.equals(endereco.getContato().getUsuario().getEmail(), userSignidIn.getEmail())) {

            try {
                enderecoRepository.delete(endereco);

            } catch (RuntimeException e) {
                throw new RuntimeException("Ocorreu um erro ao tentar deletar o endereco, erro: " + e);
            }
        }

    }
    public Endereco getEnderecoById(Usuario userSignedIn, Long enderecoId) {
        if (userSignedIn == null || enderecoId == null) {
            throw new IllegalArgumentException("Usuário ou ID do endereço não podem ser nulos");
        }


        return enderecoRepository.findById(enderecoId)
                .filter(e -> e.getContato().getUsuario().equals(userSignedIn))
                .orElseThrow(() -> new EntityNotFoundException("Endereço não encontrado ou não pertence ao usuário"));
    }


    public Page<Endereco> getEnderecosByContato(Long contatoId, Usuario userSignedIn, int page, int size) {
        if (userSignedIn == null || contatoId == null) {
            throw new IllegalArgumentException("Usuário ou ID do contato não podem ser nulos");
        }


        Contato contato = contatoRepository.findById(contatoId)
                .orElseThrow(() -> new RuntimeException("Contato não encontrado"));

        if (!Objects.equals(contato.getUsuario().getEmail(), userSignedIn.getEmail())) {
            throw new IllegalArgumentException("Contato não pertence ao usuário autenticado");
        }


        Pageable pageable = PageRequest.of(page, size);


        return enderecoRepository.findByContatoId(contatoId, pageable);
    }
}
