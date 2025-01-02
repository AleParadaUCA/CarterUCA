package es.uca.iw.carteruca.models;

import com.github.javafaker.Faker;


public class UsuarioTest {

    private static final Faker faker = new Faker();
    private static final Random random = new Random();

    public static Usuario createTestUsuario() {

        String username = "u" + String.format("%08d", random.nextInt(100000000));

        Usuario testUsuario = new Usuario();
        testUsuario.setNombre(faker.name().fullName());
        testUsuario.setApellidos(faker.name().fullName());
        testUsuario.setUsername(username);
        testUsuario.setEmail(faker.internet().emailAddress());
        testUsuario.setPassword("password");
        return testUsuario;
    }

}