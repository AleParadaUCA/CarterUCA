package es.uca.iw.carteruca.services;

import es.uca.iw.carteruca.models.Usuario;

public interface EmailInterfaceService {

    boolean enviarCorreoRegistro(Usuario user);
}