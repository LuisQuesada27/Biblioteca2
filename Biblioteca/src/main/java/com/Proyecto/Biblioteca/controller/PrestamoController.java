package com.Proyecto.Biblioteca.controller;

import com.Proyecto.Biblioteca.model.Ejemplar;
import com.Proyecto.Biblioteca.model.Prestamo;
import com.Proyecto.Biblioteca.service.PrestamoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.security.core.Authentication;
import java.util.List;

@Controller
@RequestMapping("/prestamos")
public class PrestamoController {
    
    private final PrestamoService prestamoService;

    @Autowired
    public PrestamoController(PrestamoService prestamoService) {
        this.prestamoService = prestamoService;
    }

    //Muestra lista para los préstamos y autentica por rol
    @GetMapping("/listar")
    public String listarPrestamos(Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();
        
        System.out.println("Usuario autenticado: " + username); 
        System.out.println("Roles del usuario: " + auth.getAuthorities()); 

        boolean isAdmin = auth.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));
        
        System.out.println("¿Es administrador?: " + isAdmin); 

        List<Prestamo> prestamos;

        if (isAdmin) {
            prestamos = prestamoService.obtenerTodosLosPrestamos();
        } else {
            prestamos = prestamoService.obtenerPrestamosPorUsuario(username);
        }

        model.addAttribute("prestamos", prestamos);
        return "prestamo-list";
    }

    //Muestra el formulario para crear un nuevo préstamo
    @GetMapping("/crear")
    public String mostrarFormularioCreacion(Model model) {
        model.addAttribute("prestamo", new Prestamo());
        model.addAttribute("libros", prestamoService.obtenerTodosLosLibros());
        model.addAttribute("usuarios", prestamoService.obtenerTodosLosUsuarios());
        return "prestamo-form";
    }

    ////Muestra el formulario para guardar un nuevo préstamo
    @PostMapping("/guardar")
    public String guardarPrestamo(@ModelAttribute Prestamo prestamo, RedirectAttributes redirectAttributes) {
        try {
            if (prestamo.getId() == null) {
                Long usuarioId = prestamo.getUsuario().getId();
                Long ejemplarId = prestamo.getEjemplar().getId();
                prestamoService.crearPrestamo(usuarioId, ejemplarId);
                redirectAttributes.addFlashAttribute("mensaje", "Préstamo registrado exitosamente.");
            } else {
                prestamoService.guardarPrestamo(prestamo);
                redirectAttributes.addFlashAttribute("mensaje", "Préstamo actualizado exitosamente.");
            }
        } catch (IllegalStateException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/prestamos/crear";
        }
        return "redirect:/prestamos/listar";
    }

    // Maneja la solicitud para devolver un préstamo.
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
    
    //muestra una lista de objetos Ejemplar en formato JSON.
    @GetMapping("/ejemplares-disponibles/{libroId}")
    @ResponseBody
    public List<Ejemplar> getEjemplaresDisponibles(@PathVariable Long libroId) {
        return prestamoService.obtenerEjemplaresDisponibles(libroId);
    }

    @GetMapping("/editar/{id}")
    public String mostrarFormularioEdicion(@PathVariable Long id, Model model) {
        Prestamo prestamo = prestamoService.obtenerPrestamoPorId(id);
        model.addAttribute("prestamo", prestamo);
        return "prestamo-form"; 
    }

    //Muestra la vista del reporte de multas. 
    @GetMapping("/reporte-multas")
    public String mostrarReporteMultas(Model model) {
        // Llama al nuevo método que obtiene ambos tipos de multas
        List<Prestamo> multasGeneradas = prestamoService.obtenerTodosLosPrestamosConMulta();
        model.addAttribute("multasGeneradas", multasGeneradas);
        return "multas-reporte";
    }

    // lógica para pagar la multa 
    @PostMapping("/pagar-multa")
    public String pagarMulta(@RequestParam("prestamoId") Long prestamoId, RedirectAttributes redirectAttributes) {
    prestamoService.pagarMulta(prestamoId);
    redirectAttributes.addFlashAttribute("mensaje", "Multa pagada exitosamente.");
    return "redirect:/reportes/mis-multas"; 
    }

    //Muestra la vista del reporte de prestamos atrasados. 
    @GetMapping("/reporte-atrasados")
    public String mostrarReporteAtrasados(Model model) {
        List<Prestamo> prestamosAtrasados = prestamoService.obtenerPrestamosAtrasados();
        model.addAttribute("prestamosAtrasados", prestamosAtrasados);
        return "prestamos-atrasados-reporte";
    }
}