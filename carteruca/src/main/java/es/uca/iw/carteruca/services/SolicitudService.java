package es.uca.iw.carteruca.services;

import es.uca.iw.carteruca.models.Solicitud;
import es.uca.iw.carteruca.models.Rol;
import es.uca.iw.carteruca.models.Usuario;
import es.uca.iw.carteruca.repository.SolicitudRepository;
import es.uca.iw.carteruca.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SolicitudService {
    private SolicitudRepository repository;
    private UsuarioRepository usuarioRepository;

    @Autowired
    public SolicitudService(SolicitudRepository repository, UsuarioRepository usuarioRepository) {
        this.repository = repository;
        this.usuarioRepository = usuarioRepository;
    }

    public Solicitud guardar(Solicitud solicitud){
        return repository.save(solicitud);
    }

    public List<Usuario> getAvaladores(Rol rol){
        return usuarioRepository.findByRol(rol);
    }
}
