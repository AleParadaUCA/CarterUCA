package es.uca.iw.carteruca.models.usuario;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import java.util.List;

public interface UsuarioRepository extends JpaRepository<usuario, Long>, JpaSpecificationExecutor<usuario> {

    //se puede usar Optional<clase> por si no existe la instancia de esa clase devolver error

    usuario findByUsuario(String usuario); //ej Optional<usuario> findByUsuario(String usuario);

    usuario findByNombreAndApellidos(String nombre, String apellidos); //devuelve usuario con nombre y apellidos dado

    usuario findByEmail(String Email); //devuelve un usuario con email dado

    List<usuario> findByRol(Rol rol); //devuelve lista de usuarios con un rol

    void deleteByemail(String email);//Elimina un usuario dado un email
}
