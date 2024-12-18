package es.uca.iw.carteruca.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/activate")
public class ActivationService {
    private final UsuarioService usuarioService;

    @Autowired
    public ActivationService(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    @GetMapping
    public ResponseEntity<String> activateUser(@RequestParam("token") String token) {
        String result = usuarioService.activateUser(token);
        return ResponseEntity.ok(result);
    }
}