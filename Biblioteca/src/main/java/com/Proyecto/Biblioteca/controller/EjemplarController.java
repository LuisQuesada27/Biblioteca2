package com.Proyecto.Biblioteca.controller;

import com.Proyecto.Biblioteca.model.Ejemplar;
import com.Proyecto.Biblioteca.model.EstadoEjemplar;
import com.Proyecto.Biblioteca.repository.EjemplarRepository;
import com.Proyecto.Biblioteca.repository.LibroRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/ejemplares")
public class EjemplarController {

    private final EjemplarRepository ejemplarRepository;
    private final LibroRepository libroRepository;

    @Autowired
    public EjemplarController(EjemplarRepository ejemplarRepository, LibroRepository libroRepository) {
        this.ejemplarRepository = ejemplarRepository;
        this.libroRepository = libroRepository;
    }

    // Endpoint para agregar un nuevo ejemplar a un libro
    @PostMapping
    public ResponseEntity<Ejemplar> agregarEjemplar(@RequestParam Long libroId) {
        return libroRepository.findById(libroId)
            .map(libro -> {
                Ejemplar nuevoEjemplar = new Ejemplar();
                nuevoEjemplar.setLibro(libro);
                nuevoEjemplar.setEstado(EstadoEjemplar.DISPONIBLE); // Estado inicial del ejemplar
                ejemplarRepository.save(nuevoEjemplar);
                return ResponseEntity.ok(nuevoEjemplar);
            })
            .orElse(ResponseEntity.notFound().build());
    }

    // Endpoint para obtener todos los ejemplares
    @GetMapping
    public List<Ejemplar> obtenerTodosLosEjemplares() {
        return ejemplarRepository.findAll();
    }
}