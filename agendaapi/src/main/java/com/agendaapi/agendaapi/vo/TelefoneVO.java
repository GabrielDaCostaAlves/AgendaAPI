package com.agendaapi.agendaapi.vo;


import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;

@JacksonXmlRootElement(localName = "TelefoneVO")
public class TelefoneVO {
    @JsonProperty("numero")
    private String numero;
    @JsonProperty("tipo")
    private String tipo;

}
