package com.agendaapi.agendaapi.controller;

import com.agendaapi.agendaapi.model.Usuario;
import com.agendaapi.agendaapi.service.UsuarioService;
import com.agendaapi.agendaapi.vo.UsuarioVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/agenda/usuarios")
public class UsuarioController {

    @Autowired
    private UsuarioService usuarioService;

    // Criar usuário
    @PostMapping
    public ResponseEntity<Usuario> criarUsuario(@RequestBody @Valid UsuarioVO usuarioVO) {
        // Converter UsuarioVO para Usuario
        Usuario usuario = new Usuario(usuarioVO.getNome(), usuarioVO.getEmail(), usuarioVO.getSenha());
        Usuario usuarioCriado = usuarioService.criarUsuario(usuario);
        return ResponseEntity.status(201).body(usuarioCriado);
    }

    // Consultar todos os usuários
    @GetMapping
    public List<Usuario> consultarTodosUsuarios() {
        return usuarioService.consultarTodosUsuarios();
    }

    // Modificar usuário existente
    @PutMapping("/{id}")
    public ResponseEntity<Usuario> modificarUsuario(@PathVariable Long id, @RequestBody @Valid UsuarioVO usuarioVO) {
        // Converter UsuarioVO para Usuario
        Usuario usuario = new Usuario(usuarioVO.getNome(), usuarioVO.getEmail(), usuarioVO.getSenha());
        Usuario usuarioModificado = usuarioService.modificarUsuario(id, usuario);
        return ResponseEntity.ok(usuarioModificado);
    }

    // Deletar usuário
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletarUsuario(@PathVariable Long id) {
        usuarioService.deletarUsuario(id);
        return ResponseEntity.noContent().build();
    }
}
