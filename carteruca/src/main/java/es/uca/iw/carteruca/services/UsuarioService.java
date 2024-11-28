package es.uca.iw.carteruca.services;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import es.uca.iw.carteruca.models.usuario.Usuario;
import es.uca.iw.carteruca.repository.UsuarioRepository;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UsuarioService implements UserDetailsService {

    private final UsuarioRepository repository;
    private final PasswordEncoder passwordEncoder;
    //private final EmailService emailService;

    @Autowired
    public UsuarioService(UsuarioRepository repository, PasswordEncoder passwordEncoder) {
        this.repository = repository;
        this.passwordEncoder = passwordEncoder;
        // this.emailService = emailService;
    }

    @Override
    @Transactional
    public Usuario loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<Usuario> user = repository.findByUsuario(username);
        if (!user.isPresent()) {
            throw new UsernameNotFoundException("No user present with username: " + username);
        } else {
            return user.get();
        }
    }

    public Usuario saveUser(Usuario usuario) {
        return repository.save(usuario);
    }

    public Optional<Usuario> getUserById(Long id) {
        return repository.findById(id);
    }

    public Usuario getUserByEmail(String email) {
        return repository.findByEmail(email);
    }

    public Usuario getUserByUsername(String username) {
        return repository.findByUsuario(username).get();
    }

    public void deleteUser(Long id) {
        repository.deleteById(id);
    }
}