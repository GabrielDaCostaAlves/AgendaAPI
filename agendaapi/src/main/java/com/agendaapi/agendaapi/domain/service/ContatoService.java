package com.agendaapi.agendaapi.domain.service;


import com.agendaapi.agendaapi.dto.contatodto.ContatoDto;
import com.agendaapi.agendaapi.dto.contatodto.ContatoUpdateDto;
import com.agendaapi.agendaapi.dto.contatodto.EnderecoDto;
import com.agendaapi.agendaapi.dto.contatodto.TelefoneDto;
import com.agendaapi.agendaapi.domain.model.entity.contato.Contato;
import com.agendaapi.agendaapi.domain.model.entity.contato.Endereco;
import com.agendaapi.agendaapi.domain.model.entity.contato.Telefone;
import com.agendaapi.agendaapi.domain.model.entity.usuario.Usuario;
import com.agendaapi.agendaapi.domain.repository.ContatoRepository;
import com.agendaapi.agendaapi.domain.repository.EnderecoRepository;
import com.agendaapi.agendaapi.domain.repository.TelefoneRepository;
import com.agendaapi.agendaapi.vo.ContatoVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;


@Service
public class ContatoService {

    @Autowired
    private ContatoRepository contatoRepository;

    @Autowired
    private EnderecoRepository enderecoRepository;

    @Autowired
    private TelefoneRepository telefoneRepository;



    public Contato createContato(Usuario userSignedIn, ContatoDto contatoDto) {



        Contato contato = new Contato();
        contato.setNome(contatoDto.nome());
        contato.setDataNascimento(contatoDto.dataDeNascimento());
        contato.setUsuario(userSignedIn);


        Contato savedContato = contatoRepository.save(contato);


        if (contatoDto.endereco() != null) {
            EnderecoDto enderecoDto = contatoDto.endereco();
            Endereco endereco = new Endereco();
            endereco.setLogradouro(enderecoDto.logradouro());
            endereco.setNumero(enderecoDto.numero());
            endereco.setComplemento(enderecoDto.complemento());
            endereco.setBairro(enderecoDto.bairro());
            endereco.setCidade(enderecoDto.cidade());
            endereco.setEstado(enderecoDto.estado());
            endereco.setCep(enderecoDto.cep());
            endereco.setContato(savedContato);

            try {
                enderecoRepository.save(endereco);
            } catch (RuntimeException e) {
                throw new RuntimeException("Erro ao criar telefone, codigo: " + e);
            }
        }


        if (contatoDto.telefone() != null) {
            TelefoneDto telefoneDto = contatoDto.telefone();
            Telefone telefone = new Telefone();
            telefone.setNumero(telefoneDto.numero());
            telefone.setTipo(telefoneDto.tipo());
            telefone.setContato(savedContato);

            try {
                telefoneRepository.save(telefone);
            } catch (RuntimeException e) {
                throw new RuntimeException("Erro ao criar telefone, codigo: " + e);
            }
        }
        return savedContato;

    }

    public Contato updateContato(Usuario userSignedIn, Long contatoId, ContatoUpdateDto contatoDto) {



        Contato contato = getContatoById(userSignedIn, contatoId);


        contato.setNome(contatoDto.nome());
        contato.setDataNascimento(contatoDto.dataDeNascimento());


        try {
            return contatoRepository.save(contato);
        } catch (RuntimeException e) {
            throw new RuntimeException("Erro ao atualizar usuario, codigo: " + e);
        }
    }

    public Contato getContatoById(Usuario userSignedIn, Long contatoId) {

        return contatoRepository.findById(contatoId)
                .filter(c -> c.getUsuario().getEmail().equals(userSignedIn.getEmail()))
                .orElseThrow(() -> new RuntimeException("Contato não encontrado ou não pertence ao usuário"));
    }

    public void deleteContato(Usuario userSignedIn, Long contatoId) {

        Contato contato = getContatoById(userSignedIn, contatoId);



        try {
            contatoRepository.delete(contato);
        } catch (RuntimeException e) {
            throw new RuntimeException("Erro ao deletar usuario, codigo: " + e);
        }

    }


    public Page<ContatoVO> getAllContatos(Usuario userSignedIn, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);

        return contatoRepository.findAllByUsuarioEmail(userSignedIn.getEmail(), pageable);
    }


}
