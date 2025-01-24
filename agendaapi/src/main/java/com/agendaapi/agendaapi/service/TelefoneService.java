package com.agendaapi.agendaapi.service;

import com.agendaapi.agendaapi.dto.contatodto.TelefoneDto;
import com.agendaapi.agendaapi.model.Telefone;
import com.agendaapi.agendaapi.model.Contato;
import com.agendaapi.agendaapi.model.Usuario;
import com.agendaapi.agendaapi.repository.TelefoneRepository;
import com.agendaapi.agendaapi.repository.ContatoRepository;
import com.agendaapi.agendaapi.security.JwtTokenService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
public class TelefoneService {

    @Autowired
    private TelefoneRepository telefoneRepository;

    @Autowired
    private ContatoRepository contatoRepository;

    @Autowired
    private JwtTokenService jwtTokenService;

    public Telefone createTelefone(Usuario userSignedIn, TelefoneDto telefoneDto, Long contatoId) {

        if (userSignedIn == null || contatoId == null || telefoneDto==null) {
            throw new IllegalArgumentException("Usuário, ID do contato ou Json não podem ser nulos");
        }

        // Buscar o contato no banco de dados
        Contato contato = contatoRepository.findById(contatoId)
                .orElseThrow(() -> new RuntimeException("Contato não encontrado"));

        if (Objects.equals(contato.getUsuario().getEmail(), userSignedIn.getEmail())) {
            try {
            // Criar e preencher o telefone
            Telefone telefone = new Telefone();
            telefone.setNumero(telefoneDto.numero());
            telefone.setTipo(telefoneDto.tipo());
            telefone.setContato(contato); // Associar o telefone ao contato

            // Salvar o telefone no banco de dados


                return telefoneRepository.save(telefone);
            } catch (RuntimeException e) {
                throw new RuntimeException("Ocorreu um erro ao tentar salvar o telefone, erro: " + e);
            }
        }
        return null;
    }

    public Telefone updateTelefone(Usuario userSignedIn, Long telefoneId, TelefoneDto telefoneDto) {

        if (userSignedIn == null || telefoneId == null || telefoneDto==null) {
            throw new IllegalArgumentException("Usuário, ID do telefone ou Json não podem ser nulos");
        }

        // Buscar o telefone no banco de dados
        Telefone telefone = telefoneRepository.findById(telefoneId)
                .orElseThrow(() -> new RuntimeException("Telefone não encontrado"));

        if (Objects.equals(telefone.getContato().getUsuario().getEmail(), userSignedIn.getEmail())) {

            try {
                // Atualizar o telefone com os novos dados
                telefone.setNumero(telefoneDto.numero());
                telefone.setTipo(telefoneDto.tipo());

                // Salvar o telefone atualizado no banco de dados
                return telefoneRepository.save(telefone);
            } catch (RuntimeException e) {
                throw new RuntimeException("Ocorreu um erro ao tentar atualizar o telefone, erro: " +e);
            }

        }
        return null;
    }

    public void deleteTelefone(Usuario userSignedIn, Long telefoneId) {
        if (userSignedIn == null || telefoneId == null) {
            throw new IllegalArgumentException("Usuário ou ID do telefone não podem ser nulos");
        }

        // Buscar o telefone no banco de dados
        Telefone telefone = telefoneRepository.findById(telefoneId)
                .orElseThrow(() -> new RuntimeException("Telefone não encontrado"));


        if (Objects.equals(telefone.getContato().getUsuario().getEmail(), userSignedIn.getEmail())) {
            // Deleta o telefone atualizado no banco de dados
            telefoneRepository.delete(telefone);



        }

    }


    // Buscar o contato pelo ID, garantindo que pertence ao usuário autenticado
    public Telefone getTelefoneById(Usuario userSignedIn, Long telefoneId) {
        if (userSignedIn == null || telefoneId == null) {
            throw new IllegalArgumentException("Usuário ou ID do telefone não podem ser nulos");
        }

        return telefoneRepository.findById(telefoneId)
                .filter(telefone -> telefone.getContato().getUsuario().equals(userSignedIn))
                .orElseThrow(() -> new EntityNotFoundException("Telefone não encontrado ou não pertence ao usuário"));
    }



    //todo: metodo para retornar os telefones do contato.

}
