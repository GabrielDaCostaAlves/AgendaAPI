package com.agendaapi.agendaapi.repository;

import com.agendaapi.agendaapi.model.entity.Telefone;
import com.agendaapi.agendaapi.vo.TelefoneVO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TelefoneRepository extends JpaRepository<Telefone, Long> {

    Page<Telefone> findByContatoId(Long contatoId, Pageable pageable);
}
