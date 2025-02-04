package com.agendaapi.agendaapi.domain.repository;

import com.agendaapi.agendaapi.domain.model.entity.usuario.Usuario;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

    Optional<Usuario> findByEmail(String email);

    Page<Usuario> findAll(Pageable pageable);
}
