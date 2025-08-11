package com.Proyecto.Biblioteca.controller;

import com.Proyecto.Biblioteca.service.PrestamoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/reportes")
public class ReporteController {

    @Autowired
    private PrestamoService prestamoService;
    
    //Muestra al usuario sus propios préstamos atrasados

    @GetMapping("/mis-atrasados")
    public String misPrestamosAtrasados(Model model) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        model.addAttribute("prestamosAtrasados", prestamoService.obtenerPrestamosAtrasadosPorUsuario(username));
        return "reportes/prestamos-atrasados";
    }

    // Muestra al usuario sus propias multas generadas
    @GetMapping("/mis-multas")
    public String misMultasGeneradas(Model model) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        model.addAttribute("multasGeneradas", prestamoService.obtenerMultasGeneradasPorUsuario(username));
        return "reportes/multas-generadas";
    }
}