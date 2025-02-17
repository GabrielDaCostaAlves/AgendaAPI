package com.agendaapi.agendaapi.vo;



import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;

import java.util.Date;

@JacksonXmlRootElement(localName = "ContatoVO")
public class ContatoVO {

    @JsonProperty("nome")
    private String nome;

    @JsonProperty("dataNascimento")
    private Date dataNascimento;

}
