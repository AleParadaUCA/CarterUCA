package es.uca.iw.carteruca.security;

import es.uca.iw.carteruca.models.usuario.usuario;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class AuthenticatedUser {

    public Optional<usuario> get() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || authentication instanceof AnonymousAuthenticationToken) {
            return Optional.empty();
        }
        return Optional.of((usuario) authentication.getPrincipal());
    }

    public void logout() {
        SecurityContextHolder.clearContext();
    }
}