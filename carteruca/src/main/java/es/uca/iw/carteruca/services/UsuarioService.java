package es.uca.iw.carteruca.services;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import com.vaadin.hilla.ExplicitNullableTypeChecker;

import es.uca.iw.carteruca.models.Centro;
import es.uca.iw.carteruca.models.Rol;
import es.uca.iw.carteruca.models.Usuario;
import es.uca.iw.carteruca.repository.UsuarioRepository;

@Service
public class UsuarioService implements UserDetailsService {

    private final UsuarioRepository repository;
    private final PasswordEncoder passwordEncoder;
    //private final EmailService emailService;
    private static final Pattern EMAIL_PATTERN = Pattern.compile("^[\\w.%+-]+@[\\w.-]+\\.[a-zA-Z]{2,6}$");
    //private final ExplicitNullableTypeChecker typeChecker;

    @Autowired
    public UsuarioService(UsuarioRepository repository, PasswordEncoder passwordEncoder, ExplicitNullableTypeChecker typeChecker) {
        this.repository = repository;
        this.passwordEncoder = passwordEncoder;
        // this.emailService = emailService;
        //this.typeChecker = typeChecker;
    }

    @Transactional
    public String createUser(String nombre, String apellidos, String username, String email, String password, Centro centro) {
        // Validar campos vacíos
        if (!StringUtils.hasText(username) || !StringUtils.hasText(email) || !StringUtils.hasText(password) || !StringUtils.hasText(apellidos) || !StringUtils.hasText(nombre)) {
            return ("Todos los campos son obligatorios.");
        }
        // Validar el centro
        if (centro == null || !StringUtils.hasText(centro.getNombre())) {
            return "Debe seleccionar un centro válido.";
        }

        // Validar formato del email
        if (!EMAIL_PATTERN.matcher(email).matches()) {
            return ("El correo no es válido.");
        }

        // Comprobar si el usuario ya existe
            if (repository.existsByUsuario(username)) {
            return ("El nombre de usuario ya está en uso.");
        }

        // Comprobar si el correo ya existe
        if (repository.existsByEmail(email)) {
            return ("El correo ya está en uso.");
        }

        Usuario nuevoUsuario = new Usuario();
        nuevoUsuario.setNombre(nombre);
        nuevoUsuario.setApellidos(apellidos);
        nuevoUsuario.setUsername(username);
        nuevoUsuario.setEmail(email);
        nuevoUsuario.setRol(Rol.Solicitante);
        nuevoUsuario.setPassword(passwordEncoder.encode(password));
        nuevoUsuario.setCentro(centro);

        try {
            repository.save(nuevoUsuario);
//            emailService.sendRegistrationEmail(nuevoUsuario); //futuro
            return "Exito";
        } catch (DataIntegrityViolationException e) {
            return "Error";
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