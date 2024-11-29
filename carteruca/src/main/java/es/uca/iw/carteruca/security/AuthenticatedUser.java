package es.uca.iw.carteruca.security;

import com.vaadin.flow.spring.security.AuthenticationContext;
import es.uca.iw.carteruca.models.usuario.Usuario;
import es.uca.iw.carteruca.repository.UsuarioRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Lazy;
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

    @Bean
    @Lazy
    public AuthenticationContext authenticationContext() {
        return new AuthenticationContext();
    }

//    public Optional<Usuario> get() {
//        Optional<UserDetails> authenticatedUserDetails = authenticationContext.getAuthenticatedUser(UserDetails.class);
//
//        if (authenticatedUserDetails.isPresent()) {
//            String username = authenticatedUserDetails.get().getUsername();
//            System.out.println("Authenticated user details found: " + username); // Registro de depuración
//
//            Optional<Usuario> user = userRepository.findByUsuario(username);
//
//            if (user.isPresent()) {
//                System.out.println("User found in repository: " + user.get().getNombre()); // Registro de depuración
//            } else {
//                System.out.println("User not found in repository for username: " + username); // Registro de depuración
//            }
//            return user;
//        } else {
//            System.out.println("No authenticated user details found"); // Registro de depuración
//            return Optional.empty();
//        }
//    }
}