package com.agendaapi.agendaapi.service;

import com.agendaapi.agendaapi.dto.contatodto.TelefoneDto;
import com.agendaapi.agendaapi.model.Telefone;
import com.agendaapi.agendaapi.model.Contato;
import com.agendaapi.agendaapi.repository.TelefoneRepository;
import com.agendaapi.agendaapi.repository.ContatoRepository;
import com.agendaapi.agendaapi.security.JwtTokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TelefoneService {

    @Autowired
    private TelefoneRepository telefoneRepository;

    @Autowired
    private ContatoRepository contatoRepository;

    @Autowired
    private JwtTokenService jwtTokenService;

    public Telefone createTelefone(String token, TelefoneDto telefoneDto, Long contatoId) {
        // Decodificar o token e extrair o email do usuário
        String email = jwtTokenService.getSubjectFromToken(token);

        // Buscar o contato no banco de dados
        Contato contato = contatoRepository.findById(contatoId)
                .orElseThrow(() -> new RuntimeException("Contato não encontrado"));

        // Criar e preencher o telefone
        Telefone telefone = new Telefone();
        telefone.setNumero(telefoneDto.numero());
        telefone.setTipo(telefoneDto.tipo());
        telefone.setContato(contato); // Associar o telefone ao contato

        // Salvar o telefone no banco de dados
        return telefoneRepository.save(telefone);
    }

    public Telefone updateTelefone(String token, Long telefoneId, TelefoneDto telefoneDto) {
        // Decodificar o token e extrair o email do usuário
        String email = jwtTokenService.getSubjectFromToken(token);

        // Buscar o telefone no banco de dados
        Telefone telefone = telefoneRepository.findById(telefoneId)
                .orElseThrow(() -> new RuntimeException("Telefone não encontrado"));

        // Atualizar o telefone com os novos dados
        telefone.setNumero(telefoneDto.numero());
        telefone.setTipo(telefoneDto.tipo());

        // Salvar o telefone atualizado no banco de dados
        return telefoneRepository.save(telefone);
    }
}
