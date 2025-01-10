package com.agendaapi.agendaapi.service;

import com.agendaapi.agendaapi.dto.contatodto.EnderecoDto;
import com.agendaapi.agendaapi.model.Endereco;
import com.agendaapi.agendaapi.model.Contato;
import com.agendaapi.agendaapi.repository.EnderecoRepository;
import com.agendaapi.agendaapi.repository.ContatoRepository;
import com.agendaapi.agendaapi.security.JwtTokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class EnderecoService {

    @Autowired
    private EnderecoRepository enderecoRepository;

    @Autowired
    private ContatoRepository contatoRepository;

    @Autowired
    private JwtTokenService jwtTokenService; // Serviço para lidar com JWT

    public Endereco createEndereco(String token, EnderecoDto enderecoDto, Long contatoId) {
        // Decodificar o token e extrair o email do usuário
        String email = jwtTokenService.getSubjectFromToken(token); // O email do usuário do token

        // Buscar o contato no banco de dados
        Contato contato = contatoRepository.findById(contatoId)
                .orElseThrow(() -> new RuntimeException("Contato não encontrado"));

        // Criar e preencher o endereço
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
    }

    public Endereco updateEndereco(String token, Long enderecoId, EnderecoDto enderecoDto) {
        // Decodificar o token e extrair o email do usuário
        String email = jwtTokenService.getSubjectFromToken(token);

        // Buscar o endereço no banco de dados
        Endereco endereco = enderecoRepository.findById(enderecoId)
                .orElseThrow(() -> new RuntimeException("Endereço não encontrado"));

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
    }
}
