package com.agendaapi.agendaapi.security;


import com.agendaapi.agendaapi.model.Usuario;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;


public class UserDetailsImpl implements UserDetails {

    private Usuario user; // Classe de usuário que criamos anteriormente

    public UserDetailsImpl(Usuario user) {
        this.user = user;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
    /*
     Este metodo converte a única role associada ao usuário em uma coleção de GrantedAuthority.
     O Spring Security usa isso para representar papéis de usuário.
    */
        return Collections.singletonList(new SimpleGrantedAuthority(user.getRole().getName().name()));
    }

    @Override
    public String getPassword() {
        return user.getPassword();
    } // Retorna a credencial do usuário que criamos anteriormente

    @Override
    public String getUsername() {
        return user.getEmail();
    } // Retorna o nome de usuário do usuário que criamos anteriormente

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }



}