package com.agendaapi.agendaapi.domain.service;

import com.agendaapi.agendaapi.dto.contatodto.TelefoneDto;
import com.agendaapi.agendaapi.domain.model.entity.contato.Telefone;
import com.agendaapi.agendaapi.domain.model.entity.contato.Contato;
import com.agendaapi.agendaapi.domain.model.entity.usuario.Usuario;
import com.agendaapi.agendaapi.domain.repository.TelefoneRepository;
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


        Contato contato = contatoRepository.findById(contatoId)
                .orElseThrow(() -> new RuntimeException("Contato não encontrado"));

        if (Objects.equals(contato.getUsuario().getEmail(), userSignedIn.getEmail())) {
            try {

            Telefone telefone = new Telefone();
            telefone.setNumero(telefoneDto.numero());
            telefone.setTipo(telefoneDto.tipo());
            telefone.setContato(contato);




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


        Telefone telefone = telefoneRepository.findById(telefoneId)
                .orElseThrow(() -> new RuntimeException("Telefone não encontrado"));

        if (Objects.equals(telefone.getContato().getUsuario().getEmail(), userSignedIn.getEmail())) {

            try {

                telefone.setNumero(telefoneDto.numero());
                telefone.setTipo(telefoneDto.tipo());


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


        Telefone telefone = telefoneRepository.findById(telefoneId)
                .orElseThrow(() -> new RuntimeException("Telefone não encontrado"));


        if (Objects.equals(telefone.getContato().getUsuario().getEmail(), userSignedIn.getEmail())) {

            telefoneRepository.delete(telefone);



        }

    }



    public Telefone getTelefoneById(Usuario userSignedIn, Long telefoneId) {
        if (userSignedIn == null || telefoneId == null) {
            throw new IllegalArgumentException("Usuário ou ID do telefone não podem ser nulos");
        }

        return telefoneRepository.findById(telefoneId)
                .filter(telefone -> telefone.getContato().getUsuario().equals(userSignedIn))
                .orElseThrow(() -> new EntityNotFoundException("Telefone não encontrado ou não pertence ao usuário"));
    }


    public Page<Telefone> getTelefonesByUsuario(Long contatoId, Usuario userSignedIn, int page, int size) {

        if (userSignedIn == null || contatoId == null) {
            throw new IllegalArgumentException("Usuário ou ID do contato não podem ser nulos");
        }

        Contato contato = contatoRepository.findById(contatoId)
                .orElseThrow(() -> new RuntimeException("Contato não encontrado"));


        if (!Objects.equals(contato.getUsuario().getEmail(), userSignedIn.getEmail())) {
            throw new IllegalArgumentException("Contato não pertence ao usuário autenticado");
        }


        Pageable pageable = PageRequest.of(page, size);


        return telefoneRepository.findByContatoId(contatoId, pageable);
    }


}
