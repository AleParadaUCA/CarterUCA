package es.uca.iw.carteruca.services;

import java.io.IOException;
import java.util.Collections;
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

import es.uca.iw.carteruca.models.Estado;
import es.uca.iw.carteruca.models.Solicitud;
import es.uca.iw.carteruca.models.Centro;
import es.uca.iw.carteruca.models.Rol;
import es.uca.iw.carteruca.models.Usuario;
import es.uca.iw.carteruca.repository.UsuarioRepository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import es.uca.iw.carteruca.models.Proyecto;

@Service
public class UsuarioService implements UserDetailsService {

    private static final Logger logger = LoggerFactory.getLogger(UsuarioService.class);
    private final UsuarioRepository repository;
    private final PasswordEncoder passwordEncoder;
    private final EmailService emailService;
    private final SolicitudService solicitudService;
    private static final Pattern EMAIL_PATTERN = Pattern.compile("^[\\w.%+-]+@[\\w.-]+\\.[a-zA-Z]{2,6}$");
    private final ProyectoService proyectoService;

    @Autowired
    public UsuarioService(UsuarioRepository repository, PasswordEncoder passwordEncoder, EmailService emailService, SolicitudService solicitudService, ProyectoService proyectoService) {
        this.repository = repository;
        this.passwordEncoder = passwordEncoder;
        this.emailService = emailService;
        this.solicitudService = solicitudService;
        this.proyectoService = proyectoService;
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
        nuevoUsuario.setPassword(passwordEncoder.encode(password));
        nuevoUsuario.setCentro(centro);

        nuevoUsuario.setCodigoRegistro(UUID.randomUUID().toString().substring(0, 5));

        try {
            logger.info("Añadido usuario con username: {} ",usuario);
            repository.save(nuevoUsuario);
            emailService.enviarCorreoRegistro(nuevoUsuario);
            return "Exito";
        } catch (DataIntegrityViolationException | IOException | InterruptedException e) {
            logger.warn("Error al crear usuario  {}",usuario);
            return "Error: "+ e;
        }
    }

    /**
     * Obtiene el rol de un usuario consultando un servicio externo.
     *
     * @param usuario El ID del usuario cuyo rol se desea determinar.
     * @return El rol del usuario, ya sea {@link Rol#Solicitante} o {@link Rol#Promotor}.
     */
    private Rol obtenerRol(String usuario) {
        final String url = "https://e608f590-1a0b-43c5-b363-e5a883961765.mock.pstmn.io/sponsors";
        Rol rol = Rol.Solicitante;
        WebClient webClient = WebClient.create();

        // Realizar la solicitud al servicio externo
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

    /**
     * Carga un usuario por su nombre de usuario desde la base de datos y construye un objeto {@link UserDetails}
     * para su uso en el sistema de autenticación de Spring Security.
     *
     * @param username El nombre de usuario del usuario que se desea cargar.
     * @return Un objeto {@link UserDetails} que representa al usuario autenticado, incluyendo su nombre de usuario,
     *         contraseña, estado de habilitación y roles.
     * @throws UsernameNotFoundException Si no se encuentra un usuario con el nombre de usuario proporcionado.
     */
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

    public void deleteUser(Long id) {
        Optional<Usuario> userOptional = repository.findById(id);
        if (userOptional.isPresent()) {
            Usuario usuario = userOptional.get();

            // Buscar todas las solicitudes del usuario con estado diferente de ACEPTADO
            List<Solicitud> solicitudes = solicitudService.getSolicitudesByUsuario(usuario);
            for (Solicitud solicitud : solicitudes) {
                if (solicitud.getEstado() != Estado.ACEPTADO) {
                    solicitudService.CancelarSolicitud(solicitud);
                }
            }

            // Establecer jefe_id a null en todos los proyectos donde este usuario es jefe
            List<Proyecto> proyectos = proyectoService.findByJefe(usuario);
            for (Proyecto proyecto : proyectos) {
                if (proyecto.getPorcentaje() != 100) { proyecto.setJefe(null);} //solo aquellos no terminados
                proyectoService.cambiarPorcentaje(proyecto); //IMPORTANTE 
            }

            try {
                logger.info("Eliminando usuario con ID: {} ", id);
                repository.deleteById(id);
            } catch (Exception e) {

                logger.warn("Intento fallido al eliminar usuario con ID: {} ",id);
                // Actualizar los datos del usuario a valores nulos o "usuario eliminado"
                usuario.setNombre("Cuenta eliminada");
                usuario.setApellidos("Cuenta eliminada");
                usuario.setEmail(null);
                usuario.setUsername("Cuenta eliminada");
                usuario.setPassword("none");
                usuario.setActivo(false);
                usuario.setCentro(null);
                usuario.setRol(null);
                
                repository.save(usuario);
            }
        }
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
            logger.info("Actualizando usuario con ID: {} ", id);
            repository.save(usuario);
            return "Usuario actualizado correctamente.";
        } catch (DataIntegrityViolationException e) {
            logger.warn("Intento de actualizacion fallido del usuario con ID: {} ", id);
            return "Error al actualizar el usuario.";
        }
    }

    public List<Usuario> findAllUsuariosExcludingAdmin() {
        return repository.findByRolNot(Rol.Admin).stream()
                .filter(usuario -> !(
                    "Cuenta eliminada".equals(usuario.getApellidos()) &&
                    usuario.getEmail() == null &&
                    "Cuenta eliminada".equals(usuario.getUsername()) &&
                    usuario.getPassword() == null &&
                    !usuario.isActivo() &&
                    usuario.getCentro() == null &&
                    usuario.getRol() == null
                ))
                .collect(Collectors.toList());
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
            user.get().setRol( obtenerRol(user.get().getUsername()));
            repository.save(user.get());
            return true;

        } else {

            return false;
        }

    }
}