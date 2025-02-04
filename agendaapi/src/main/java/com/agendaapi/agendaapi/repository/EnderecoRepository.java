package com.agendaapi.agendaapi.repository;

import com.agendaapi.agendaapi.model.entity.Endereco;
import com.agendaapi.agendaapi.vo.EnderecoVO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EnderecoRepository extends JpaRepository<Endereco, Long> {

    Page<Endereco> findByContatoId(Long contatoId, Pageable pageable);
}
