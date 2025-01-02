package es.uca.iw.carteruca.models;

import com.github.javafaker.Faker;

public class UsuarioTest {

    private static final Faker faker = new Faker();

    public static Usuario createTestUsuario() {
        Usuario testUsuario = new Usuario();
        testUsuario.setUsername(faker.name().username());
        testUsuario.setEmail(faker.internet().emailAddress());
        testUsuario.setPassword("password");
        return testUsuario;
    }

    public static Usuario createTestUsuario(String username) {
        Usuario testUsuario = new Usuario();
        testUsuario.setUsername(username);
        testUsuario.setEmail(faker.internet().emailAddress());
        testUsuario.setPassword("password");
        return testUsuario;
    }
}