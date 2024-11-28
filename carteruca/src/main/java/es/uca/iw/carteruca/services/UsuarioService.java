package es.uca.iw.carteruca.services;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import es.uca.iw.carteruca.models.usuario.Rol;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
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

    public boolean createUser(String nombre, String apellidos, String username, String email, String password) {
        // Crear un nuevo objeto Usuario con los datos del formulario
        Usuario nuevoUsuario = new Usuario();
        nuevoUsuario.setNombre(nombre);
        nuevoUsuario.setApellidos(apellidos);
        nuevoUsuario.setUsername(username);
        nuevoUsuario.setEmail(email);
        nuevoUsuario.setRol(Rol.Solicitante);
        nuevoUsuario.setPassword(passwordEncoder.encode(password));

        try {
            repository.save(nuevoUsuario);
//            emailService.sendRegistrationEmail(nuevoUsuario);
            return true;
        } catch (DataIntegrityViolationException e) {
            return false;
        }
    }

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Usuario user = repository.findByUsuario(username)
                .orElseThrow(() -> new UsernameNotFoundException("No user present with username: " + username));

        // Convierte roles a una lista de autoridades
        List<GrantedAuthority> authorities = Collections.singletonList(
                new SimpleGrantedAuthority("ROLE_" + user.getRol().name())
        );

        return new org.springframework.security.core.userdetails.User(
                user.getUsername(),
                user.getPassword(),
                user.isEnabled(), // Aquí puedes agregar más lógica si es necesario
                true,
                true,
                true,
                authorities
        );
    }

//    public Usuario saveUser(Usuario usuario) {
//        return repository.save(usuario);
//    }

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