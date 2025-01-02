package es.uca.iw.carteruca.services;

import es.uca.iw.carteruca.models.Usuario;
import es.uca.iw.carteruca.repository.UsuarioRepository;
import es.uca.iw.carteruca.models.UsuarioTest;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
// import org.mockito.Mock;

@SpringBootTest
@Transactional(propagation = Propagation.REQUIRES_NEW) // después de cada test se hace un rollback de la base de datos
public class UsuarioServiceTest {

    @Autowired
    private UsuarioService usuarioService;

    @MockBean
    private UsuarioRepository usuarioRepository;
    
    @MockBean
    private EmailService emailService; // Inyecta el EmailFakeService

    @Test
    public void shouldNotActivateANoExistingUsuario() {

        // Given a certain user (not stored on the database)
        Usuario testUsuario = UsuarioTest.createTestUsuario();

        // When invoking the method ActivateUser
        boolean result = usuarioService.activateUser(testUsuario.getEmail(), testUsuario.getCodigoRegistro());

        // Then the result method is false
        assertThat(result).isFalse();

        // When invoking the method FindActiveUsers
        List<Usuario> returnedUsuarios = usuarioRepository.findAll().stream()
                .filter(Usuario::isActivo)
                .collect(Collectors.toList());

        // Then the result does not include the user
        assertThat(returnedUsuarios.contains(testUsuario)).isFalse();
    }

    @Test
    public void shouldActivateAnExistingUsuario() {

        // Given
        // a certain user
        Usuario testUsuario = UsuarioTest.createTestUsuario();

        // the repo methods are stubbed
        given(usuarioRepository.findByEmail(anyString())).willReturn(Optional.of(testUsuario));
        given(usuarioRepository.findAll()).willReturn(List.of(testUsuario));

        // who is registered
        usuarioService.createUser(testUsuario.getNombre(), testUsuario.getApellidos(), testUsuario.getUsername(), testUsuario.getEmail(), testUsuario.getPassword(), testUsuario.getCentro());


        // When invoking the method ActivateUser
        boolean result = usuarioService.activateUser(testUsuario.getEmail(), testUsuario.getCodigoRegistro());

        // Then the result method is true
        assertThat(result).isTrue();

        // When invoking the method FindActive
        List<Usuario> returnedUsuarios = usuarioRepository.findAll().stream()
                .filter(Usuario::isActivo)
                .collect(Collectors.toList());

        // Then the result includes the user
        assertThat(returnedUsuarios.contains(testUsuario)).isTrue();
    }
}