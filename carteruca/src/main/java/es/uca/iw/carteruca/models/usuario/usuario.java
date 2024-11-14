package es.uca.iw.carteruca.models.usuario;

public record usuario(
        Long id,
        String username,
        String password,
        String name,
        String lastname,
        String email,
        Rol rol
        ) {
}
