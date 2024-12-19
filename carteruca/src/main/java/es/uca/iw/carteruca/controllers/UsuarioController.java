package es.uca.iw.carteruca.controllers;

import es.uca.iw.carteruca.services.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/usuarios")
public class UsuarioController {

    private final UsuarioService usuarioService;

    @Autowired
    public UsuarioController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    @GetMapping("/activar/{token}")
    public String activateUser(@PathVariable String token) {
        return usuarioService.activarteUser(token);
    }
}