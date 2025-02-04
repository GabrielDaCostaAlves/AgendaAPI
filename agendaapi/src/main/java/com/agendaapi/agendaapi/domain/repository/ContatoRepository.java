package com.agendaapi.agendaapi.domain.repository;

import com.agendaapi.agendaapi.domain.model.entity.contato.Contato;
import com.agendaapi.agendaapi.vo.ContatoVO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ContatoRepository extends JpaRepository<Contato, Long> {
    Page<ContatoVO> findAllByUsuarioEmail(String usuarioEmail, Pageable pageable);
}
