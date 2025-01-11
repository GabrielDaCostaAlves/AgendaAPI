package com.agendaapi.agendaapi.service;

import com.agendaapi.agendaapi.dto.contatodto.EnderecoDto;
import com.agendaapi.agendaapi.model.Endereco;
import com.agendaapi.agendaapi.model.Contato;
import com.agendaapi.agendaapi.model.Usuario;
import com.agendaapi.agendaapi.repository.EnderecoRepository;
import com.agendaapi.agendaapi.repository.ContatoRepository;
import com.agendaapi.agendaapi.security.JwtTokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
public class EnderecoService {

    @Autowired
    private EnderecoRepository enderecoRepository;

    @Autowired
    private ContatoRepository contatoRepository;

    @Autowired
    private JwtTokenService jwtTokenService; // Serviço para lidar com JWT

    public Endereco createEndereco(Usuario userSignedIn, EnderecoDto enderecoDto, Long contatoId) {

        if (userSignedIn == null || contatoId == null || enderecoDto == null) {
            throw new IllegalArgumentException("Usuário, ID do contato ou Json não podem ser nulos");
        }
        // Buscar o contato no banco de dados
        Contato contato = contatoRepository.findById(contatoId)
                .orElseThrow(() -> new RuntimeException("Contato não encontrado"));


        if (Objects.equals(userSignedIn.getEmail(), contato.getUsuario().getEmail())) {
            // Criar e preencher o endereço

            try {
                Endereco endereco = new Endereco();
                endereco.setLogradouro(enderecoDto.logradouro());
                endereco.setNumero(enderecoDto.numero());
                endereco.setComplemento(enderecoDto.complemento());
                endereco.setBairro(enderecoDto.bairro());
                endereco.setCidade(enderecoDto.cidade());
                endereco.setEstado(enderecoDto.estado());
                endereco.setCep(enderecoDto.cep());
                endereco.setContato(contato); // Associar o endereço ao contato

                // Salvar o endereço no banco de dados
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
        // Buscar o endereço no banco de dados
        Endereco endereco = enderecoRepository.findById(enderecoId)
                .orElseThrow(() -> new RuntimeException("Endereço não encontrado"));


        if (Objects.equals(endereco.getContato().getUsuario().getEmail(), userSignedIn.getEmail())) {


            try {
                // Atualizar o endereço com os novos dados
                endereco.setLogradouro(enderecoDto.logradouro());
                endereco.setNumero(enderecoDto.numero());
                endereco.setComplemento(enderecoDto.complemento());
                endereco.setBairro(enderecoDto.bairro());
                endereco.setCidade(enderecoDto.cidade());
                endereco.setEstado(enderecoDto.estado());
                endereco.setCep(enderecoDto.cep());

                // Salvar o endereço atualizado no banco de dados
                return enderecoRepository.save(endereco);
            } catch (RuntimeException e) {
                throw new RuntimeException("Ocorreu um erro ao tentar atualizaro endereco, erro: " + e);
            }
        }
        return null;
    }

    public boolean deleteEndereco(Usuario userSignidIn, Long enderecoId) {

        if (userSignidIn == null || enderecoId == null) {
            throw new IllegalArgumentException("Usuário, ID do endereço não podem ser nulos");
        }

        // Buscar o endereco no banco de dados
        Endereco endereco = enderecoRepository.findById(enderecoId)
                .orElseThrow(() -> new RuntimeException("Telefone não encontrado"));


        if (Objects.equals(endereco.getContato().getUsuario().getEmail(), userSignidIn.getEmail())) {

            try {
                // Deleta o endereco atualizado no banco de dados
                enderecoRepository.delete(endereco);
                return true;
            } catch (RuntimeException e) {
                throw new RuntimeException("Ocorreu um erro ao tentar deletar o endereco, erro: " + e);
            }
        }
        return false;
    }


    //todo: Criar metodo para retornar lista de endereços.
}
