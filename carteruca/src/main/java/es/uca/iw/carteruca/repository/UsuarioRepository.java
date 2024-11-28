package es.uca.iw.carteruca.repository;

import es.uca.iw.carteruca.models.usuario.Rol;
import es.uca.iw.carteruca.models.usuario.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import java.util.List;

public interface UsuarioRepository extends JpaRepository<Usuario, Long>, JpaSpecificationExecutor<Usuario> {

    //se puede usar Optional<clase> por si no existe la instancia de esa clase devolver error

    Usuario findByUsuario(String usuario); //ej Optional<usuario> findByUsuario(String usuario);

    Usuario findByNombreAndApellidos(String nombre, String apellidos); //devuelve usuario con nombre y apellidos dado

    Usuario findByEmail(String Email); //devuelve un usuario con email dado

    List<Usuario> findByRol(Rol rol); //devuelve lista de usuarios con un rol

    void deleteByEmail(String email);//Elimina un usuario dado un email
}
