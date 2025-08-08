package com.Proyecto.Biblioteca.controller;

import com.Proyecto.Biblioteca.model.Ejemplar;
import com.Proyecto.Biblioteca.model.Prestamo;
import com.Proyecto.Biblioteca.service.PrestamoService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/prestamos")
public class PrestamoController {
    
    private final PrestamoService prestamoService;

    @Autowired
    public PrestamoController(PrestamoService prestamoService) {
        this.prestamoService = prestamoService;
    }

    @GetMapping("/listar")
    public String listarPrestamos(Model model) {
        model.addAttribute("prestamos", prestamoService.obtenerTodosLosPrestamos());
        return "prestamo-list";
    }

    @GetMapping("/crear")
    public String mostrarFormularioCreacion(Model model) {
        model.addAttribute("prestamo", new Prestamo());
        model.addAttribute("libros", prestamoService.obtenerTodosLosLibros());
        model.addAttribute("usuarios", prestamoService.obtenerTodosLosUsuarios());
        return "prestamo-form";
    }

    @PostMapping("/guardar")
    public String guardarPrestamo(@ModelAttribute Prestamo prestamo, RedirectAttributes redirectAttributes) {
        try {
            Long usuarioId = prestamo.getUsuario().getId();
            Long ejemplarId = prestamo.getEjemplar().getId();
            
            prestamoService.crearPrestamo(usuarioId, ejemplarId);
            
            redirectAttributes.addFlashAttribute("mensaje", "Préstamo registrado exitosamente.");
        } catch (IllegalStateException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/prestamos/crear";
        }
        return "redirect:/prestamos/listar";
    }

    @PostMapping("/devolver/{id}")
    public String devolverPrestamo(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            prestamoService.devolverPrestamo(id);
            redirectAttributes.addFlashAttribute("mensaje", "Devolución registrada exitosamente.");
        } catch (IllegalStateException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/prestamos/listar";
    }
    
    @GetMapping("/ejemplares-disponibles/{libroId}")
    @ResponseBody
    public List<Ejemplar> getEjemplaresDisponibles(@PathVariable Long libroId) {
        return prestamoService.obtenerEjemplaresDisponibles(libroId);
    }
}