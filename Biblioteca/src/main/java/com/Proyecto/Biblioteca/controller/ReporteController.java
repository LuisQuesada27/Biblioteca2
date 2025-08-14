package com.Proyecto.Biblioteca.controller;

import com.Proyecto.Biblioteca.service.PrestamoService;
import com.Proyecto.Biblioteca.service.ReporteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import java.util.Map;

@Controller
@RequestMapping("/reportes")
public class ReporteController {

    @Autowired
    private PrestamoService prestamoService;

    @Autowired
    private ReporteService reporteService;
    
    @GetMapping("/estadisticas")
    public String mostrarReporteEstadisticas(Model model) {
        return "reportes/reporte_estadisticas";
    }

    @GetMapping("/api/disponibilidad")
    @ResponseBody
    public ResponseEntity<Map<String, Long>> getEstadisticasDisponibilidad() {
        Map<String, Long> estadisticas = reporteService.getEstadisticasDisponibilidad();
        return ResponseEntity.ok(estadisticas);
    }

    @GetMapping("/api/mas-prestados")
    @ResponseBody
    public ResponseEntity<Map<String, Long>> getLibrosMasPrestados() {
        Map<String, Long> masPrestados = reporteService.getLibrosMasPrestados();
        return ResponseEntity.ok(masPrestados);
    }

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