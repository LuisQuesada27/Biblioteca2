package com.Proyecto.Biblioteca.controller;

import com.Proyecto.Biblioteca.model.Prestamo;
import com.Proyecto.Biblioteca.model.Ejemplar;
import com.Proyecto.Biblioteca.repository.PrestamoRepository;
import com.Proyecto.Biblioteca.repository.EjemplarRepository;
import com.Proyecto.Biblioteca.repository.LibroRepository;
import com.Proyecto.Biblioteca.repository.UsuarioRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDate;
import java.util.List;

@Controller
@RequestMapping("/prestamos")
public class PrestamoController {

    private final PrestamoRepository prestamoRepository;
    private final EjemplarRepository ejemplarRepository;
    private final LibroRepository libroRepository;
    private final UsuarioRepository usuarioRepository;

    public PrestamoController(PrestamoRepository prestamoRepository, EjemplarRepository ejemplarRepository, LibroRepository libroRepository, UsuarioRepository usuarioRepository) {
        this.prestamoRepository = prestamoRepository;
        this.ejemplarRepository = ejemplarRepository;
        this.libroRepository = libroRepository;
        this.usuarioRepository = usuarioRepository;
    }

    @GetMapping("/listar")
    public String listarPrestamos(Model model) {
        model.addAttribute("prestamos", prestamoRepository.findAllWithDetails());
        return "prestamo-list";
    }

    @GetMapping("/crear")
    public String mostrarFormularioCreacion(Model model) {
        model.addAttribute("prestamo", new Prestamo());
        model.addAttribute("libros", libroRepository.findAll());
        model.addAttribute("usuarios", usuarioRepository.findAll());
        return "prestamo-form";
    }

    @PostMapping("/guardar")
    public String guardarPrestamo(@ModelAttribute Prestamo prestamo, RedirectAttributes redirectAttributes) {
        // Primero, obtenemos el ID del ejemplar del objeto incompleto que llega del formulario.
        Long ejemplarId = prestamo.getEjemplar().getId();

        // Luego, cargamos el objeto Ejemplar completo desde la base de datos.
        Ejemplar ejemplar = ejemplarRepository.findById(ejemplarId).orElse(null);

        if (ejemplar != null) {
            // Ahora que tenemos el Ejemplar completo, actualizamos su estado
            ejemplar.setEstado("Prestado");
            ejemplarRepository.save(ejemplar);

            // Asignamos este Ejemplar completo al Préstamo antes de guardarlo.
            prestamo.setEjemplar(ejemplar);
        }

        prestamo.setFechaPrestamo(LocalDate.now());
        prestamo.setFechaVencimiento(LocalDate.now().plusWeeks(2));
        prestamoRepository.save(prestamo);

        redirectAttributes.addFlashAttribute("mensaje", "Préstamo registrado exitosamente.");
        return "redirect:/prestamos/listar";
    }


    @PostMapping("/devolver/{id}")
    public String devolverPrestamo(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        Prestamo prestamo = prestamoRepository.findById(id).orElse(null);
        if (prestamo != null && prestamo.getEjemplar() != null) {
            // Marcar el ejemplar como disponible
            Ejemplar ejemplar = prestamo.getEjemplar();
            ejemplar.setEstado("Disponible");
            ejemplarRepository.save(ejemplar);

            // Registrar la fecha de devolución en el préstamo
            prestamo.setFechaDevolucion(LocalDate.now()); // <-- AÑADE ESTA LÍNEA
            prestamoRepository.save(prestamo); // <-- AÑADE ESTA LÍNEA
            
            redirectAttributes.addFlashAttribute("mensaje", "Devolución registrada exitosamente.");
        }
        return "redirect:/prestamos/listar";
    }

    @GetMapping("/ejemplares-disponibles/{libroId}")
    @ResponseBody
    public List<Ejemplar> getEjemplaresDisponibles(@PathVariable Long libroId) {
        return ejemplarRepository.findByLibroIdAndEstado(libroId, "Disponible");
    }
}