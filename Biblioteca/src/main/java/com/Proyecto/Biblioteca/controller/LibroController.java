package com.Proyecto.Biblioteca.controller;

import com.Proyecto.Biblioteca.model.Libro;
import com.Proyecto.Biblioteca.repository.LibroRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/libros")
public class LibroController {

    @Autowired
    private LibroRepository libroRepository;

    @GetMapping
    public List<Libro> getAllLibros() {
        return libroRepository.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Libro> getLibroById(@PathVariable Long id) {
        Optional<Libro> libro = libroRepository.findById(id);
        return libro.map(value -> new ResponseEntity<>(value, HttpStatus.OK))
                    .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PostMapping
    public ResponseEntity<Libro> createLibro(@RequestBody Libro libro) {
        Libro newLibro = libroRepository.save(libro);
        return new ResponseEntity<>(newLibro, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Libro> updateLibro(@PathVariable Long id, @RequestBody Libro libroDetails) {
        Optional<Libro> optionalLibro = libroRepository.findById(id);
        if (optionalLibro.isPresent()) {
            Libro existingLibro = optionalLibro.get();
            existingLibro.setTitulo(libroDetails.getTitulo());
            existingLibro.setAnioPublicacion(libroDetails.getAnioPublicacion());
            existingLibro.setIsbn(libroDetails.getIsbn());
            existingLibro.setDescripcion(libroDetails.getDescripcion());
            
            // Aquí se deberían manejar las relaciones con Autor y Categoría
            // existingLibro.setAutores(libroDetails.getAutores());
            // existingLibro.setCategoria(libroDetails.getCategoria());

            Libro updatedLibro = libroRepository.save(existingLibro);
            return new ResponseEntity<>(updatedLibro, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<HttpStatus> deleteLibro(@PathVariable Long id) {
        try {
            libroRepository.deleteById(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}