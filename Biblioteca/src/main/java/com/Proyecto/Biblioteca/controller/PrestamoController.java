package com.Proyecto.Biblioteca.controller;

import com.Proyecto.Biblioteca.model.Ejemplar;
import com.Proyecto.Biblioteca.model.Prestamo;
import com.Proyecto.Biblioteca.model.Usuario;
import com.Proyecto.Biblioteca.repository.UsuarioRepository;
import com.Proyecto.Biblioteca.service.PrestamoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.security.core.Authentication;
import java.time.LocalDate;
import java.util.List;
import java.util.Collections;

@Controller
@RequestMapping("/prestamos")
public class PrestamoController {
    
    // Declaración de variables finales, ambas son inyectadas.
    private final PrestamoService prestamoService;
    private final UsuarioRepository usuarioRepository;

    @Autowired
    public PrestamoController(PrestamoService prestamoService, UsuarioRepository usuarioRepository) {
        this.prestamoService = prestamoService;
        this.usuarioRepository = usuarioRepository;
    }

    //Muestra lista para los préstamos y autentica por rol
    @GetMapping("/listar")
    public String listarPrestamos(Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();
        
        boolean isAdmin = auth.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));

        List<Prestamo> prestamos;

        if (isAdmin) {
            prestamos = prestamoService.obtenerTodosLosPrestamos();
        } else {
            prestamos = prestamoService.obtenerPrestamosPorUsuario(username);
        }

        model.addAttribute("prestamos", prestamos);
        return "prestamo-list";
    }

    @GetMapping("/crear")
    public String mostrarFormularioCreacion(Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        boolean isAdmin = auth.getAuthorities().stream()
            .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));

        List<Usuario> usuarios;
        if (isAdmin) {
            usuarios = prestamoService.obtenerTodosLosUsuarios();
        } else {
            String username = auth.getName();
            Usuario usuarioActual = usuarioRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado"));
            usuarios = Collections.singletonList(usuarioActual);
        }

        Prestamo nuevoPrestamo = new Prestamo();
        nuevoPrestamo.setFechaPrestamo(LocalDate.now());
        nuevoPrestamo.setFechaVencimiento(LocalDate.now().plusDays(15));
        
        model.addAttribute("prestamo", nuevoPrestamo);
        model.addAttribute("libros", prestamoService.obtenerTodosLosLibros());
        model.addAttribute("usuarios", usuarios);
        model.addAttribute("isAdmin", isAdmin);
        return "prestamo-form";
    }

    @PostMapping("/guardar")
    public String guardarPrestamo(@ModelAttribute Prestamo prestamo, Model model, RedirectAttributes redirectAttributes) {
        try {
            if (prestamo.getId() == null) {
                // Al guardar un nuevo préstamo, los campos de fecha se establecen en el servicio
                // Esto asegura que la lógica esté en un solo lugar.
            }
            prestamoService.guardarPrestamo(prestamo);
            
            if (prestamo.getId() == null) {
                redirectAttributes.addFlashAttribute("mensaje", "Préstamo registrado exitosamente.");
            } else {
                redirectAttributes.addFlashAttribute("mensaje", "Préstamo actualizado exitosamente.");
            }
        } catch (IllegalArgumentException e) {
            model.addAttribute("error", e.getMessage());
            model.addAttribute("prestamo", prestamo);
            model.addAttribute("libros", prestamoService.obtenerTodosLosLibros());
            
            // Lógica para volver a obtener la lista de usuarios y el flag isAdmin
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            boolean isAdmin = auth.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));
            
            List<Usuario> usuarios;
            if (isAdmin) {
                usuarios = prestamoService.obtenerTodosLosUsuarios();
            } else {
                String username = auth.getName();
                Usuario usuarioActual = usuarioRepository.findByUsername(username)
                    .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado"));
                usuarios = Collections.singletonList(usuarioActual);
            }
            model.addAttribute("usuarios", usuarios);
            model.addAttribute("isAdmin", isAdmin);
            
            return "prestamo-form"; 
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

    @GetMapping("/editar/{id}")
    public String mostrarFormularioEdicion(@PathVariable Long id, Model model) {
        Prestamo prestamo = prestamoService.obtenerPrestamoPorId(id);
        model.addAttribute("prestamo", prestamo);
        // Opcional: Si quieres mantener el estilo de solo lectura en edición,
        // también podrías agregar el flag isAdmin aquí y en el HTML.
        return "prestamo-form"; 
    }

    // Reporte de multas para administradores
    @GetMapping("/reporte-multas")
    public String mostrarReporteMultas(Model model) {
        List<Prestamo> multasGeneradas = prestamoService.obtenerTodosLosPrestamosConMulta();
        model.addAttribute("multasGeneradas", multasGeneradas);
        return "reportes/multas-generadas";
    }

    // Lógica para pagar la multa 
    @PostMapping("/pagar-multa")
    public String pagarMulta(@RequestParam("prestamoId") Long prestamoId, RedirectAttributes redirectAttributes) {
        prestamoService.pagarMulta(prestamoId);
        redirectAttributes.addFlashAttribute("mensaje", "Multa pagada exitosamente.");
        return "redirect:/prestamos/listar";
    }

    // Reporte de préstamos atrasados para administradores
    @GetMapping("/reporte-atrasados")
    public String mostrarReporteAtrasados(Model model) {
        List<Prestamo> prestamosAtrasados = prestamoService.obtenerPrestamosAtrasados();
        model.addAttribute("prestamosAtrasados", prestamosAtrasados);
        return "reportes/prestamos-atrasados"; 
    }
}