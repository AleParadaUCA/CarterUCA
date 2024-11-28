package es.uca.iw.carteruca.services;

import es.uca.iw.carteruca.models.usuario.Usuario;
import es.uca.iw.carteruca.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class UsuarioManagementService implements UserDetailsService {

    private final UsuarioRepository repository;
    //private final EmailService emailService;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UsuarioManagementService(UsuarioRepository repository, PasswordEncoder passwordEncoder) {
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

//
//    public boolean activateUser(String email, String registerCode) {
//
//        Optional<Usuario> user = repository.findByEmail(email);
//
//        if (user.isPresent() && user.get().getRegisterCode().equals(registerCode)) {
//            user.get().setActive(true);
//            repository.save(user.get());
//            return true;
//
//        } else {
//            return false;
//        }
//
//    }
//
//
//    public Optional<Usuario> loadUserById(UUID userId) {
//        return repository.findById(userId);
//    }
//
//    public List<Usuario> loadActiveUsers() {
//        return repository.findByActiveTrue();
//    }
//
//    public void delete(Usuario testUser) {
//        repository.delete(testUser);
//
//    }
//
//
//    public int count() {
//        return (int) repository.count();
//    }
}
