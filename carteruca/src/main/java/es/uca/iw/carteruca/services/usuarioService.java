package es.uca.iw.carteruca.services;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import es.uca.iw.carteruca.models.usuario.Usuario;
import es.uca.iw.carteruca.repository.UsuarioRepository;

@Service
public class usuarioService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    public Usuario saveUser(Usuario usuario) {
        return usuarioRepository.save(usuario);
    }

    public Optional<Usuario> getUserById(Long id) {
        return usuarioRepository.findById(id);
    }

    public Usuario getUserByEmail(String email) {
        return usuarioRepository.findByEmail(email);
    }

    public Usuario getUserByUsername(String username) {
        return usuarioRepository.findByUsuario(username).get();
    }

    public void deleteUser(Long id) {
        usuarioRepository.deleteById(id);
    }
}