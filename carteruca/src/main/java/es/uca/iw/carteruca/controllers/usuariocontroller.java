package es.uca.iw.carteruca.controllers;

import es.uca.iw.carteruca.models.usuario.Rol;
import es.uca.iw.carteruca.models.usuario.Usuario;
import es.uca.iw.carteruca.models.usuario.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class usuariocontroller {

    @Autowired
    private UsuarioRepository usuarioRepository;

}
