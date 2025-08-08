package com.Proyecto.Biblioteca.controller;

import com.Proyecto.Biblioteca.service.PrestamoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/reportes")
public class ReporteController {

    @Autowired
    private PrestamoService prestamoService;

    @GetMapping("/prestamos-atrasados")
    public String prestamosAtrasados(Model model) {
        model.addAttribute("prestamosAtrasados", prestamoService.obtenerPrestamosAtrasados());
        return "reportes/prestamos-atrasados";
    }

    @GetMapping("/multas-generadas")
    public String multasGeneradas(Model model) {
        model.addAttribute("multasGeneradas", prestamoService.obtenerPrestamosConMulta());
        return "reportes/multas-generadas";
    }
}