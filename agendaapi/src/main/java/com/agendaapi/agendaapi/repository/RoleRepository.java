package com.agendaapi.agendaapi.repository;

import com.agendaapi.agendaapi.model.entity.Role;
import com.agendaapi.agendaapi.model.enums.RoleName;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, Long> {
    Optional<Role> findByName(RoleName name);
}