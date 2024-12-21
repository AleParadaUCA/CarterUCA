package es.uca.iw.carteruca.services;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.regex.Pattern;
import java.util.stream.Stream;

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

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import es.uca.iw.carteruca.models.Centro;
import es.uca.iw.carteruca.models.Rol;
import es.uca.iw.carteruca.models.Usuario;
import es.uca.iw.carteruca.repository.UsuarioRepository;

@Service
public class UsuarioService implements UserDetailsService {

    private final UsuarioRepository repository;
    private final PasswordEncoder passwordEncoder;
    private final EmailService emailService;
    private static final Pattern EMAIL_PATTERN = Pattern.compile("^[\\w.%+-]+@[\\w.-]+\\.[a-zA-Z]{2,6}$");
    //private final ExplicitNullableTypeChecker typeChecker;

    @Autowired
    public UsuarioService(UsuarioRepository repository, PasswordEncoder passwordEncoder, EmailService emailService) {
        this.repository = repository;
        this.passwordEncoder = passwordEncoder;
        this.emailService = emailService;
        //this.typeChecker = typeChecker;
    }

    @Transactional
    public String createUser(String nombre, String apellidos, String usuario, String email, String password, Centro centro){
        // Eliminar espacios al inicio y al final de los campos
        nombre = nombre != null ? nombre.trim() : "";
        apellidos = apellidos != null ? apellidos.trim() : "";
        usuario = usuario != null ? usuario.trim() : "";
        email = email != null ? email.trim() : "";
        password = password != null ? password.trim() : "";

        // Validar campos vacíos
        if (!StringUtils.hasText(usuario) || !StringUtils.hasText(email) || !StringUtils.hasText(password) ||
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
        if (repository.existsByUsuario(usuario)) {
            return "El nombre de usuario ya está en uso.";
        }

        // Validar que el nombre de usuario no contenga espacios
        if (usuario.contains(" ")) {
            return "El nombre de usuario no debe contener espacios.";
        }

        // Validar que el usuario esté en el formato u + 8 números
        if (!usuario.matches("u\\d{8}")) {
            return "Formato de usuario incorrecto. Debe ser 'u' seguido de 8 números.";
        }

        // Comprobar si el correo ya existe
        if (repository.existsByEmail(email)) {
            return "El correo ya está en uso.";
        }

        Usuario nuevoUsuario = new Usuario();
        nuevoUsuario.setNombre(nombre);
        nuevoUsuario.setApellidos(apellidos);
        nuevoUsuario.setUsername(usuario);
        nuevoUsuario.setEmail(email);
        nuevoUsuario.setRol(obtenerRol(usuario));
        nuevoUsuario.setPassword(passwordEncoder.encode(password));
        nuevoUsuario.setCentro(centro);

        nuevoUsuario.setCodigoRegistro(UUID.randomUUID().toString().substring(0, 5));

        try {
            repository.save(nuevoUsuario);
            emailService.enviarCorreoRegistro(nuevoUsuario);
            return "Exito";
        } catch (DataIntegrityViolationException e) {
            return "Error: "+ e;
        }
    }

    private Rol obtenerRol(String usuario) {
        final String url = "https://e608f590-1a0b-43c5-b363-e5a883961765.mock.pstmn.io/sponsors";
        Rol rol = Rol.Solicitante;
        WebClient webClient = WebClient.create();

        // Realizar la solicitud
        String response = webClient.get()
            .uri(url)
            .retrieve()
            .bodyToMono(String.class)
            .block();

        // Parsear el JSON y buscar el ID
        ObjectMapper mapper = new ObjectMapper();
        JsonNode root;
        try {
            root = mapper.readTree(response);
            JsonNode data = root.path("data");
            for (JsonNode promotor : data) {
                if (usuario.equalsIgnoreCase(promotor.path("id").asText())) { rol = Rol.Promotor; }
            }
        } catch (JsonProcessingException ex) {}
        return rol;
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

    public Optional<Usuario> getUserById(Long id) {
        return repository.findById(id);
    }

    public Optional<Usuario> getUserByUsername(String username) {
        return repository.findByUsuario(username);
    }

    public void deleteUser(Long id) {
        repository.deleteById(id);
    }

    @Transactional
    public String updateUser(Long id, String nombre, String apellidos, String email, String username) {
        Optional<Usuario> userOptional = repository.findById(id);
        if (userOptional.isEmpty()) {
            return "Usuario no encontrado.";
        }

        Usuario usuario = userOptional.get();

        if (!StringUtils.hasText(nombre) || !StringUtils.hasText(apellidos) || !StringUtils.hasText(email)) {
            return "Todos los campos son obligatorios.";
        }

        if (!EMAIL_PATTERN.matcher(email).matches()) {
            return "El correo no es válido.";
        }

        if (!usuario.getEmail().equals(email) && repository.existsByEmail(email)) {
            return "El correo ya está en uso.";
        }

        if(!usuario.getUsername().equals(username) && repository.existsByUsuario(username)){
            return "El usuario ya esta en uso";
        }

        usuario.setNombre(nombre);
        usuario.setApellidos(apellidos);
        usuario.setEmail(email);
        usuario.setUsername(username);

        try {
            repository.save(usuario);
            return "Usuario actualizado correctamente.";
        } catch (DataIntegrityViolationException e) {
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
        return Stream.of(Rol.values())
                .filter(rol -> rol != Rol.Admin) // Excluir Admin
                .toList();
    }

    public List<Usuario> getAvaladores() {
        return repository.findByRol(Rol.Promotor);
    }

    public List<Usuario> getOTP(){ return repository.findByRol(Rol.OTP); }

    public boolean checkPassword(Usuario currentUser, String value) {
        return passwordEncoder.matches(value, currentUser.getPassword());
    }

    public void updatePassword(Usuario currentUser, String value) {
        currentUser.setPassword(passwordEncoder.encode(value));
        repository.save(currentUser);
    }

        public boolean activateUser(String email, String registerCode) {

        Optional<Usuario> user = repository.findByEmail(email);

        if (user.isPresent() && !user.get().isActivo() && user.get().getCodigoRegistro().equals(registerCode)) {
            user.get().setActivo(true);
            user.get().setCodigoRegistro(null);
            repository.save(user.get());
            return true;

        } else {
            return false;
        }

    }
}