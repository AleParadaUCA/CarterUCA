package es.uca.iw.carteruca.services;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.regex.Pattern;


import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.reactive.function.client.WebClient;

import com.vaadin.hilla.ExplicitNullableTypeChecker;

import es.uca.iw.carteruca.models.Centro;
import es.uca.iw.carteruca.models.Rol;
import es.uca.iw.carteruca.models.Usuario;
import es.uca.iw.carteruca.repository.UsuarioRepository;

@Service
public class UsuarioService implements UserDetailsService {

    private final UsuarioRepository repository;
    private final PasswordEncoder passwordEncoder;
    //private final EmailService emailService;
    private static final Pattern EMAIL_PATTERN = Pattern.compile("^[\\w.%+-]+@[\\w.-]+\\.[a-zA-Z]{2,6}$");
    //private final ExplicitNullableTypeChecker typeChecker;

    @Autowired
    public UsuarioService(UsuarioRepository repository, PasswordEncoder passwordEncoder, ExplicitNullableTypeChecker typeChecker) {
        this.repository = repository;
        this.passwordEncoder = passwordEncoder;
        // this.emailService = emailService;
        //this.typeChecker = typeChecker;
    }

    @Transactional
    public String createUser(String nombre, String apellidos, String username, String email, String password, Centro centro) {
        // Eliminar espacios al inicio y al final de los campos
        nombre = nombre != null ? nombre.trim() : "";
        apellidos = apellidos != null ? apellidos.trim() : "";
        username = username != null ? username.trim() : "";
        email = email != null ? email.trim() : "";
        password = password != null ? password.trim() : "";

        // Validar campos vacíos
        if (!StringUtils.hasText(username) || !StringUtils.hasText(email) || !StringUtils.hasText(password) ||
                !StringUtils.hasText(apellidos) || !StringUtils.hasText(nombre)) {
            return "Todos los campos son obligatorios.";
        }

        // Validar el centro
        if (centro == null || !StringUtils.hasText(centro.getNombre())) {
            return "Debe seleccionar un centro válido.";
        }

        // Validar formato del email
        if (!EMAIL_PATTERN.matcher(email).matches()) {
            return "El correo no es válido.";
        }

        // Comprobar si el usuario ya existe
        if (repository.existsByUsuario(username)) {
            return "El nombre de usuario ya está en uso.";
        }

        // Validar que el nombre de usuario no contenga espacios
        if (username.contains(" ")) {
            return "El nombre de usuario no debe contener espacios.";
        }

        // Comprobar si el correo ya existe
        if (repository.existsByEmail(email)) {
            return "El correo ya está en uso.";
        }

        // Determinar el rol del usuario
        Rol rol = Rol.Solicitante; // Valor predeterminado
        try {
            // Verificar si el usuario es un promotor mediante la API
            if (esPromotor(nombre+" "+apellidos)) {
                rol = Rol.Promotor;
            }
        } catch (Exception e) {
            return "Error al verificar el rol: " + e.getMessage();
        }

        Usuario nuevoUsuario = new Usuario();
        nuevoUsuario.setNombre(nombre);
        nuevoUsuario.setApellidos(apellidos);
        nuevoUsuario.setUsername(username);
        nuevoUsuario.setEmail(email);
        nuevoUsuario.setRol(rol);
        nuevoUsuario.setPassword(passwordEncoder.encode(password));
        nuevoUsuario.setCentro(centro);

        repository.save(nuevoUsuario);
        return "Exito";
    }

    private boolean esPromotor(String nombre) {
        final String url = "https://e608f590-1a0b-43c5-b363-e5a883961765.mock.pstmn.io/sponsors";
        WebClient webClient = WebClient.create();

        System.out.println("Nombre: " + nombre);
        // Realizar la solicitud
        try {
            String response = webClient.get()
                    .uri(url)
                    .retrieve()
                    .bodyToMono(String.class)
                    .block();

            // Parsear el JSON y buscar el nombre
            ObjectMapper mapper = new ObjectMapper();
            JsonNode root = mapper.readTree(response);
            JsonNode data = root.path("data");
            for (JsonNode promotor : data) {
                if (nombre.equalsIgnoreCase(promotor.path("nombre").asText())) {
                    System.out.println("promotor\n");
                    return true;
                }

            }
        } catch (Exception e) {
            throw new RuntimeException("Error al consultar la API de promotores", e);
        }
        System.out.println("no coincide con la lista\n");
        return false;
    }


    @Override
    @Transactional
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Usuario user = repository.findByUsuario(username)
                .orElseThrow(() -> new UsernameNotFoundException("No user present with username: " + username));

        // Convierte roles a una lista de autoridades
        List<GrantedAuthority> authorities = Collections.singletonList(
                new SimpleGrantedAuthority("ROLE_" + user.getRol().name())
        );

        return new org.springframework.security.core.userdetails.User(
                user.getUsername(),
                user.getPassword(),
                user.isEnabled(), // Aquí puedes agregar más lógica si es necesario
                true,
                true,
                true,
                authorities
        );
    }

//    public Usuario saveUser(Usuario usuario) {
//        return repository.save(usuario);
//    }

    public Optional<Usuario> getUserById(Long id) {
        return repository.findById(id);
    }

    public Usuario getUserByEmail(String email) {
        return repository.findByEmail(email);
    }

    public Usuario getUserByUsername(String username) {
        return repository.findByUsuario(username).get();
    }

    public void deleteUser(Long id) {
        repository.deleteById(id);
    }

    @Transactional
    public String updateUser(Long id, String nombre, String apellidos, String email) {
        Optional<Usuario> userOptional = repository.findById(id);
        if (userOptional.isEmpty()) {
            System.out.println("Usuario no encontrado.");
            return "Usuario no encontrado.";
        }

        Usuario usuario = userOptional.get();

        if (!StringUtils.hasText(nombre) || !StringUtils.hasText(apellidos) || !StringUtils.hasText(email)) {
            System.out.println("Faltan campos obligatorios.");
            return "Todos los campos son obligatorios.";
        }

        if (!EMAIL_PATTERN.matcher(email).matches()) {
            System.out.println("Correo no válido.");
            return "El correo no es válido.";
        }

        if (!usuario.getEmail().equals(email) && repository.existsByEmail(email)) {
            System.out.println("Correo ya está en uso.");
            return "El correo ya está en uso.";
        }

        usuario.setNombre(nombre);
        usuario.setApellidos(apellidos);
        usuario.setEmail(email);

        try {
            repository.save(usuario);
            System.out.println("Usuario actualizado correctamente.");
            return "Usuario actualizado correctamente.";
        } catch (DataIntegrityViolationException e) {
            System.out.println("Error al guardar: " + e.getMessage());
            return "Error al actualizar el usuario.";
        }
    }

    public List<Usuario> findAllUsuariosExcludingAdmin(){
        return repository.findByRolNot(Rol.Admin);
    }

    @Transactional
    public Usuario updateUsuarioRol(Long userId, Rol rol){
        Usuario usuario = repository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado."));
        usuario.setRol(rol);
        return repository.save(usuario);
    }

    public List<Rol> getRolesExcludingAdmin() {
        return List.of(Rol.values()).stream()
                .filter(rol -> rol != Rol.Admin) // Excluir Admin
                .toList();
    }




}