package com.agendaapi.agendaapi.dto.usuariodto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public class LoginUserDto {

        @NotBlank(message = "O email não pode ser vazio.")
        @Email(message = "O email deve ser válido.")
        private String email;

        @NotBlank(message = "A senha não pode ser vazia.")
        private String password;

        public LoginUserDto() {}

        public LoginUserDto(String email, String password) {
                this.email = email;
                this.password = password;
        }


        public String getEmail() {
                return email;
        }

        public void setEmail(String email) {
                this.email = email;
        }

        public String getPassword() {
                return password;
        }

        public void setPassword(String password) {
                this.password = password;
        }
}
