package es.uca.iw.carteruca.repository;

import es.uca.iw.carteruca.models.Usuario;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
public class UsuarioRepositoryTest {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Test
    public void shouldNotFindANotExistingUsuario() {
        // Given
        // a random user Id
        Long usuarioId = UUID.randomUUID().getMostSignificantBits() & Long.MAX_VALUE;

        // When invoking the method
        Optional<Usuario> foundUsuario = usuarioRepository.findById(usuarioId);

        // Then
        assertThat(foundUsuario.isPresent()).isFalse();
    }

    @Test
    public void shouldFindAnExistingUsuario() {
        // Given
        // a certain user
        Usuario testUsuario = new Usuario();
        testUsuario.setNombre("Test");
        testUsuario.setApellidos("User");
        testUsuario.setEmail("test.user@example.com");
        // stored in the repository
        usuarioRepository.save(testUsuario);

        // When invoking the method findById
        Optional<Usuario> foundUsuario = usuarioRepository.findById(testUsuario.getId());

        // Then
        assertThat(foundUsuario.get()).isEqualTo(testUsuario);
    }
}