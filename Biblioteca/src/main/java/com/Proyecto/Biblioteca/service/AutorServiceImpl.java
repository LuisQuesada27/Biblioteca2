package com.Proyecto.Biblioteca.service;

import com.Proyecto.Biblioteca.model.Autor;
import com.Proyecto.Biblioteca.repository.AutorRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AutorServiceImpl implements AutorService {

    private final AutorRepository autorRepository;

    @Autowired
    public AutorServiceImpl(AutorRepository autorRepository) {
        this.autorRepository = autorRepository;
    }

    @Override
    public List<Autor> obtenerTodosLosAutores() {
        return autorRepository.findAll();
    }

    @Override
    public Autor guardarAutor(Autor autor) {
        return autorRepository.save(autor);
    }

    @Override
    public Autor obtenerAutorPorId(Long id) {
        return autorRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Autor no encontrado con ID: " + id));
    }

    @Override
    public void eliminarAutor(Long id) {
        autorRepository.deleteById(id);
    }
}