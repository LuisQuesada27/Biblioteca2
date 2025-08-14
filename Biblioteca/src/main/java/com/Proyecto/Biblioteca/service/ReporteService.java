package com.Proyecto.Biblioteca.service;

import com.Proyecto.Biblioteca.model.EstadoEjemplar;
import com.Proyecto.Biblioteca.repository.EjemplarRepository;
import com.Proyecto.Biblioteca.repository.PrestamoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.HashMap;

@Service
public class ReporteService {

    @Autowired
    private EjemplarRepository ejemplarRepository;

    @Autowired
    private PrestamoRepository prestamoRepository;

    public Map<String, Long> getEstadisticasDisponibilidad() {
        long prestados = ejemplarRepository.countByEstado(EstadoEjemplar.PRESTADO);
        long disponibles = ejemplarRepository.countByEstado(EstadoEjemplar.DISPONIBLE);

        Map<String, Long> estadisticas = new HashMap<>();
        estadisticas.put("prestados", prestados);
        estadisticas.put("disponibles", disponibles);
        return estadisticas;
    }

    public Map<String, Long> getLibrosMasPrestados() {
        List<Object[]> resultados = prestamoRepository.findLibrosMasPrestados();

        return resultados.stream()
                .collect(Collectors.toMap(
                    o -> (String) o[0], // Título del libro
                    o -> (Long) o[1]    // Cantidad de préstamos
                ));
    }
}