package es.uca.iw.carteruca.models;

import es.uca.iw.carteruca.CommonObjets;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;


public class UsuarioTest {
    @Test
    public void shouldProvideUsername() {

        // Given
        // a certain user (not stored on the database)
        Usuario testUser = CommonObjets.createTestUsuario("Fernando");

        // When
        // I invoke getUsername method
        String nombre = testUser.getNombre();

        // Then the result is equals to the provided username
        assertThat(nombre.equals("Fernando")).isTrue();

    }
}