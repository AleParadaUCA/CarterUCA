package es.uca.iw.carteruca.repository;

import es.uca.iw.carteruca.models.Rol;
import es.uca.iw.carteruca.models.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import java.util.List;
import java.util.Optional;

public interface UsuarioRepository extends JpaRepository<Usuario, Long>, JpaSpecificationExecutor<Usuario> {

    Optional<Usuario> findByUsuario(String usuario); //ej Optional<usuario> findByUsuario(String usuario);

    Usuario findByNombreAndApellidos(String nombre, String apellidos); //devuelve usuario con nombre y apellidos dado

    Usuario findByEmail(String Email); //devuelve un usuario con email dado

    List<Usuario> findByRol(Rol rol); //devuelve lista de usuarios con un rol

    boolean existsByUsuario(String username);

    boolean existsByEmail(String email);

    void deleteByEmail(String email);//Elimina un usuario dado un email
}
