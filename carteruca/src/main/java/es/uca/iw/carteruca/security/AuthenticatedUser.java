package es.uca.iw.carteruca.security;

import java.util.Optional;

import org.springframework.context.annotation.Lazy;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.vaadin.flow.spring.security.AuthenticationContext;

import es.uca.iw.carteruca.models.Usuario;
import es.uca.iw.carteruca.repository.UsuarioRepository;

@Component
public class AuthenticatedUser {

    private final UsuarioRepository userRepository;
    private final AuthenticationContext authenticationContext;

    public AuthenticatedUser(@Lazy AuthenticationContext authenticationContext, @Lazy UsuarioRepository userRepository) {
        this.userRepository = userRepository;
        this.authenticationContext = authenticationContext;
    }

    @Transactional
    public Optional<Usuario> get() {
        return authenticationContext.getAuthenticatedUser(UserDetails.class)
                .flatMap(userDetails -> {
                Optional<Usuario> usuario = userRepository.findByUsuario(userDetails.getUsername());
                if (usuario.isPresent() && !usuario.get().isActivo()) {
                    logout();
                    throw new UserNotActiveException("El usuario no está activo.");
                }
                return usuario;
            });
    }

    public void logout() {
        authenticationContext.logout();
    }

    public class UserNotActiveException extends RuntimeException {
    public UserNotActiveException(String message) {
        super(message);
    }
}
}