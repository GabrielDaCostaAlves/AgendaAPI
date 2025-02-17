package com.agendaapi.agendaapi.assembler;

import com.agendaapi.agendaapi.controller.UsuarioController;
import com.agendaapi.agendaapi.domain.model.entity.usuario.Usuario;
import com.agendaapi.agendaapi.util.conversor.ConverterClass;
import com.agendaapi.agendaapi.vo.UsuarioVO;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import java.lang.reflect.InvocationTargetException;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@Component
public class UsuarioAssembler implements RepresentationModelAssembler<Usuario, EntityModel<UsuarioVO>> {

    @Override
    public EntityModel<UsuarioVO> toModel(Usuario usuario)  {

        UsuarioVO usuarioVO;
        try {

            usuarioVO = ConverterClass.convert(usuario, UsuarioVO.class);
        } catch (IllegalAccessException | InstantiationException | NoSuchMethodException | InvocationTargetException |
                 NoSuchFieldException e) {
            throw new RuntimeException(e);
        }
        EntityModel<UsuarioVO> usuarioModel = EntityModel.of(usuarioVO);


        usuarioModel.add(linkTo(methodOn(UsuarioController.class).getUsuario(usuario.getId())).withSelfRel());


        return usuarioModel;
    }
}
