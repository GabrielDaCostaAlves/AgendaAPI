package com.agendaapi.agendaapi.service;


import com.agendaapi.agendaapi.dto.contatodto.ContatoDto;
import com.agendaapi.agendaapi.dto.contatodto.EnderecoDto;
import com.agendaapi.agendaapi.dto.contatodto.TelefoneDto;
import com.agendaapi.agendaapi.model.Contato;
import com.agendaapi.agendaapi.model.Endereco;
import com.agendaapi.agendaapi.model.Telefone;
import com.agendaapi.agendaapi.model.Usuario;
import com.agendaapi.agendaapi.repository.ContatoRepository;
import com.agendaapi.agendaapi.repository.EnderecoRepository;
import com.agendaapi.agendaapi.repository.TelefoneRepository;
import com.agendaapi.agendaapi.repository.UsuarioRepository;
import com.agendaapi.agendaapi.security.JwtTokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;



@Service
public class ContatoService {

    @Autowired
    private ContatoRepository contatoRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private EnderecoRepository enderecoRepository;

    @Autowired
    private TelefoneRepository telefoneRepository;

    @Autowired
    private JwtTokenService jwtTokenService; // Serviço para lidar com JWT

    public void createContato(String token, ContatoDto contatoDto) {
        // Decodificar o token e extrair o email do usuário
        String email = jwtTokenService.getSubjectFromToken(token);

        // Buscar o usuário no banco de dados
        Usuario usuario = usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

        // Criar e preencher o contato
        Contato contato = new Contato();
        contato.setNome(contatoDto.nome());
        contato.setDataNascimento(contatoDto.dataDeNascimento());
        contato.setUsuario(usuario); // Associar o contato ao usuário encontrado

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
            endereco.setContato(savedContato); // Associar o endereço ao contato
            enderecoRepository.save(endereco);
        }

        // Se um telefone foi fornecido no DTO, salvá-lo
        if (contatoDto.telefone() != null) {
            TelefoneDto telefoneDto = contatoDto.telefone();
            Telefone telefone = new Telefone();
            telefone.setNumero(telefoneDto.numero());
            telefone.setTipo(telefoneDto.tipo());
            telefone.setContato(savedContato); // Associar o telefone ao contato
            telefoneRepository.save(telefone);
        }


    }

    public Contato updateContato(String token, Long contatoId, ContatoDto contatoDto) {
        // Decodificar o token e extrair o email do usuário
        String email = jwtTokenService.getSubjectFromToken(token); // O email do usuário do token

        // Buscar o usuário no banco de dados
        Usuario usuario = usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

        // Buscar o contato para o usuário autenticado
        Contato contato = contatoRepository.findById(contatoId)
                .filter(c -> c.getUsuario().getEmail().equals(email))
                .orElseThrow(() -> new RuntimeException("Contato não encontrado ou não pertence ao usuário"));

        // Atualizar as informações do contato
        contato.setNome(contatoDto.nome());
        contato.setDataNascimento(contatoDto.dataDeNascimento());

        // Salvar o contato atualizado
        return contatoRepository.save(contato);
    }

    public Contato getContatoById(String token, Long contatoId) {
        // Decodificar o token e extrair o email do usuário
        String email = jwtTokenService.getSubjectFromToken(token); // O email do usuário do token

        // Buscar o contato pelo ID, garantindo que pertence ao usuário autenticado
        return contatoRepository.findById(contatoId)
                .filter(c -> c.getUsuario().getEmail().equals(email))
                .orElseThrow(() -> new RuntimeException("Contato não encontrado ou não pertence ao usuário"));
    }

    public void deleteContato(String token, Long contatoId) {
        // Decodificar o token e extrair o email do usuário
        String email = jwtTokenService.getSubjectFromToken(token); // O email do usuário do token

        // Buscar o contato para o usuário autenticado
        Contato contato = contatoRepository.findById(contatoId)
                .filter(c -> c.getUsuario().getEmail().equals(email))
                .orElseThrow(() -> new RuntimeException("Contato não encontrado ou não pertence ao usuário"));

        // Deletar o contato
        contatoRepository.delete(contato);
    }
}
