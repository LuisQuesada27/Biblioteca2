package com.Proyecto.Biblioteca.service;

import com.Proyecto.Biblioteca.model.Autor;
import java.util.List;

public interface AutorService {
    
    List<Autor> obtenerTodosLosAutores();
    
    Autor guardarAutor(Autor autor);
    
    Autor obtenerAutorPorId(Long id);

    void eliminarAutor(Long id);
}