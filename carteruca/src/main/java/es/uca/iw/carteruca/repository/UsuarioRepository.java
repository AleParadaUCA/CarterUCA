package es.uca.iw.carteruca.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import es.uca.iw.carteruca.models.Rol;
import es.uca.iw.carteruca.models.Usuario;

public interface UsuarioRepository extends JpaRepository<Usuario, Long>, JpaSpecificationExecutor<Usuario> {

    Optional<Usuario> findByUsuario(String usuario); //ej Optional<usuario> findByUsuario(String usuario);

    Usuario findByNombreAndApellidos(String nombre, String apellidos); //devuelve usuario con nombre y apellidos dado

    Optional<Usuario> findByEmail(String Email); //devuelve un usuario con email dado

    List<Usuario> findByRol(Rol rol); //devuelve lista de usuarios con un rol

    boolean existsByUsuario(String username);

    boolean existsByEmail(String email);

    void deleteByEmail(String email);//Elimina un usuario dado un email

    List<Usuario> findByRolNot(Rol rol); // Buscar todos los usuarios excepto los de un tipo de rol concreto

    public Optional<Usuario> findByCodigoRegistro(String token);

}
