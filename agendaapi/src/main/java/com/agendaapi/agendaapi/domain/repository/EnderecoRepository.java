package com.agendaapi.agendaapi.domain.repository;

import com.agendaapi.agendaapi.domain.model.entity.contato.Endereco;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EnderecoRepository extends JpaRepository<Endereco, Long> {

    Page<Endereco> findByContatoId(Long contatoId, Pageable pageable);
}
