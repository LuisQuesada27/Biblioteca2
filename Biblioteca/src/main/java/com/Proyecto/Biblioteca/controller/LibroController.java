package com.Proyecto.Biblioteca.controller;


import com.Proyecto.Biblioteca.model.Ejemplar;
import com.Proyecto.Biblioteca.model.EstadoEjemplar;
import com.Proyecto.Biblioteca.model.Libro;
import com.Proyecto.Biblioteca.repository.AutorRepository;
import com.Proyecto.Biblioteca.repository.CategoriaRepository;
import com.Proyecto.Biblioteca.repository.EjemplarRepository;
import com.Proyecto.Biblioteca.repository.LibroRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import org.springframework.transaction.annotation.Transactional;
import java.util.stream.Collectors;

@Controller
public class LibroController {

    private final LibroRepository libroRepository;
    private final AutorRepository autorRepository;
    private final CategoriaRepository categoriaRepository;
    private final EjemplarRepository ejemplarRepository; 

    public LibroController(LibroRepository libroRepository, AutorRepository autorRepository, CategoriaRepository categoriaRepository, EjemplarRepository ejemplarRepository) {
        this.libroRepository = libroRepository;
        this.autorRepository = autorRepository;
        this.categoriaRepository = categoriaRepository;
        this.ejemplarRepository = ejemplarRepository; 
    }

    // Muestra la lista de libros
    @GetMapping("/libros")
    public String listarLibros(Model model) {
        model.addAttribute("libros", libroRepository.findAll());
        return "lista-libros";
    }

    // Muestra el formulario para crear un nuevo libro
    @GetMapping("/libros/crear")
    public String mostrarFormularioCreacion(Model model) {
        model.addAttribute("libro", new Libro());
        model.addAttribute("autores", autorRepository.findAll());
        model.addAttribute("categorias", categoriaRepository.findAll());
        return "libro-form";
    }

    // Maneja la petición POST para guardar un libro
    @PostMapping("/libros/crear")
    public String guardarLibro(@ModelAttribute Libro libro, RedirectAttributes redirectAttributes) {
        // Busca y asigna los autores y la categoría completos antes de guardar
        if (libro.getAutores() != null) {
            libro.setAutores(libro.getAutores().stream()
                    .map(autor -> autorRepository.findById(autor.getId()).orElse(null))
                    .collect(Collectors.toList()));
        }
        if (libro.getCategoria() != null && libro.getCategoria().getId() != null) {
            libro.setCategoria(categoriaRepository.findById(libro.getCategoria().getId()).orElse(null));
        }

        libroRepository.save(libro);
        redirectAttributes.addFlashAttribute("mensaje", "Libro creado exitosamente.");
        return "redirect:/libros";
    }

    // Muestra el formulario para editar un libro existente
    @GetMapping("/libros/editar/{id}")
    public String mostrarFormularioEdicion(@PathVariable("id") Long id, Model model) {
        Libro libro = libroRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("ID de libro no válido:" + id));

        model.addAttribute("libro", libro);
        model.addAttribute("autores", autorRepository.findAll());
        model.addAttribute("categorias", categoriaRepository.findAll());
        return "libro-form-edicion";
    }

    // Maneja la petición POST para actualizar un libro
    @PostMapping("/libros/actualizar/{id}")
    public String actualizarLibro(@PathVariable("id") Long id, @ModelAttribute Libro libro) {
        libro.setId(id);

        // Se asegura de buscar los objetos completos de Autor y Categoria antes de guardar
        if (libro.getAutores() != null) {
            libro.setAutores(libro.getAutores().stream()
                    .map(autor -> autorRepository.findById(autor.getId()).orElse(null))
                    .collect(Collectors.toList()));
        }
        if (libro.getCategoria() != null && libro.getCategoria().getId() != null) {
            libro.setCategoria(categoriaRepository.findById(libro.getCategoria().getId()).orElse(null));
        }

        libroRepository.save(libro);
        return "redirect:/libros";
    }

    // Maneja la petición GET para eliminar un libro
    @GetMapping("/libros/eliminar/{id}")
    public String eliminarLibro(@PathVariable("id") Long id) {
        libroRepository.deleteById(id);
        return "redirect:/libros";
    }

    //MÉTODO PARA CREAR EJEMPLARES ---

    // Añade un nuevo ejemplar a un libro existente
    @Transactional
    @GetMapping("/libros/crear-ejemplar/{libroId}")
    public String crearEjemplar(@PathVariable("libroId") Long libroId, RedirectAttributes redirectAttributes) {
        Libro libro = libroRepository.findById(libroId)
                .orElseThrow(() -> new IllegalArgumentException("ID de libro no válido:" + libroId));

        Ejemplar ejemplar = new Ejemplar();
        ejemplar.setLibro(libro);
        ejemplar.setEstado(EstadoEjemplar.DISPONIBLE);
        ejemplarRepository.save(ejemplar);

        redirectAttributes.addFlashAttribute("mensaje", "Ejemplar del libro '" + libro.getTitulo() + "' creado exitosamente.");
        return "redirect:/libros";
    }
}