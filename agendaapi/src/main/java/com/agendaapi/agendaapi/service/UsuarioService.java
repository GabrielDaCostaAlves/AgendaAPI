package com.agendaapi.agendaapi.service;


import com.agendaapi.agendaapi.model.Usuario;
import com.agendaapi.agendaapi.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UsuarioService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    public Usuario criarUsuario(Usuario usuario) {
        return usuarioRepository.save(usuario);
    }

    public List<Usuario> consultarTodosUsuarios() {
        return usuarioRepository.findAll();
    }

    public Usuario modificarUsuario(Long id, Usuario usuario) {
        Optional<Usuario> usuarioExistente = usuarioRepository.findById(id);
        if (usuarioExistente.isPresent()) {
            usuario.setId(id);  // Atualizar o ID para o existente
            return usuarioRepository.save(usuario);
        }
        throw new RuntimeException("Usuário não encontrado");
    }

    public void deletarUsuario(Long id) {
        usuarioRepository.deleteById(id);
    }
}
