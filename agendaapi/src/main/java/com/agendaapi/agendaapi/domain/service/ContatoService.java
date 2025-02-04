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


    // Criar e preencher o contato (telefone e endereço se incluido)
    public Contato createContato(Usuario userSignedIn, ContatoDto contatoDto) {


        // Criar e preencher o contato
        Contato contato = new Contato();
        contato.setNome(contatoDto.nome());
        contato.setDataNascimento(contatoDto.dataDeNascimento());
        contato.setUsuario(userSignedIn); // Associar o contato ao usuário encontrado

        // Salvar o contato no banco de dados
        Contato savedContato = contatoRepository.save(contato);

        // Se um endereço foi fornecido no DTO, salvá-lo
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
            // Associar o endereço ao contato
            try {
                enderecoRepository.save(endereco);
            } catch (RuntimeException e) {
                throw new RuntimeException("Erro ao criar telefone, codigo: " + e);
            }
        }

        // Se um telefone foi fornecido no DTO, salvá-lo
        if (contatoDto.telefone() != null) {
            TelefoneDto telefoneDto = contatoDto.telefone();
            Telefone telefone = new Telefone();
            telefone.setNumero(telefoneDto.numero());
            telefone.setTipo(telefoneDto.tipo());
            telefone.setContato(savedContato);
            // Associar o telefone ao contato
            try {
                telefoneRepository.save(telefone);
            } catch (RuntimeException e) {
                throw new RuntimeException("Erro ao criar telefone, codigo: " + e);
            }
        }
        return savedContato;

    }

    public Contato updateContato(Usuario userSignedIn, Long contatoId, ContatoUpdateDto contatoDto) {


        // Buscar o contato para o usuário autenticado
        Contato contato = getContatoById(userSignedIn, contatoId);

        // Atualizar as informações do contato
        contato.setNome(contatoDto.nome());
        contato.setDataNascimento(contatoDto.dataDeNascimento());

        // Salvar o contato atualizado
        try {
            return contatoRepository.save(contato);
        } catch (RuntimeException e) {
            throw new RuntimeException("Erro ao atualizar usuario, codigo: " + e);
        }
    }

    public Contato getContatoById(Usuario userSignedIn, Long contatoId) {
        // Buscar o contato pelo ID, garantindo que pertence ao usuário autenticado
        return contatoRepository.findById(contatoId)
                .filter(c -> c.getUsuario().getEmail().equals(userSignedIn.getEmail()))
                .orElseThrow(() -> new RuntimeException("Contato não encontrado ou não pertence ao usuário"));
    }

    public void deleteContato(Usuario userSignedIn, Long contatoId) {
        // Buscar o contato para o usuário autenticado
        Contato contato = getContatoById(userSignedIn, contatoId);
        // Buscar o contato para o usuário autenticado

        // Deletar o contato
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
