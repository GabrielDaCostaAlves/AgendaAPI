package com.agendaapi.agendaapi.domain.repository;

import com.agendaapi.agendaapi.domain.model.entity.contato.Telefone;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TelefoneRepository extends JpaRepository<Telefone, Long> {

    Page<Telefone> findByContatoId(Long contatoId, Pageable pageable);
}
