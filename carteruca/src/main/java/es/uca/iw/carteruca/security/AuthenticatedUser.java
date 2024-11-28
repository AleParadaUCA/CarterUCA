package es.uca.iw.carteruca.security;

import com.vaadin.flow.spring.security.AuthenticationContext;
import es.uca.iw.carteruca.models.usuario.Usuario;
import es.uca.iw.carteruca.repository.UsuarioRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Component
public class AuthenticatedUser {

    private final UsuarioRepository userRepository;
    private final AuthenticationContext authenticationContext;

    public AuthenticatedUser(AuthenticationContext authenticationContext, UsuarioRepository userRepository) {
        this.userRepository = userRepository;
        this.authenticationContext = authenticationContext;
    }

    @Transactional
    public Optional<Usuario> get() {
        return authenticationContext.getAuthenticatedUser(UserDetails.class)
                .map(userDetails -> userRepository.findByUsuario(userDetails.getUsername()).get());
    }

    public void logout() {
        authenticationContext.logout();
    }
}