package com.agendaapi.agendaapi.vo;


import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;

@JacksonXmlRootElement(localName = "LoginVO")
public class LoginVO {

    @JsonProperty("email")
    String email;
    @JsonProperty("password")
    String password;




}
