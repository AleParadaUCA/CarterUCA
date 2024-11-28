package es.uca.iw.carteruca.controllers;

import es.uca.iw.carteruca.models.usuario.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class usuarioController {

    @Autowired
    private UsuarioRepository usuarioRepository;

}
