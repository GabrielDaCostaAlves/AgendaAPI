package com.agendaapi.agendaapi.vo;


public class LoginVO {
    String email;

    String password;

    public LoginVO(String email, String password) {
        this.email = email;
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }
}
