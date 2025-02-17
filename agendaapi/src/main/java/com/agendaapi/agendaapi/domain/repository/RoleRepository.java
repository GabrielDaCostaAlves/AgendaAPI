package com.agendaapi.agendaapi.domain.repository;

import com.agendaapi.agendaapi.domain.model.entity.usuario.Role;
import com.agendaapi.agendaapi.domain.model.enums.RoleName;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, Long> {
    Optional<Role> findByName(RoleName name);
}