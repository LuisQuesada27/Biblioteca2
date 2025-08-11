package com.Proyecto.Biblioteca.service;

import com.Proyecto.Biblioteca.model.*;
import com.Proyecto.Biblioteca.repository.EjemplarRepository;
import com.Proyecto.Biblioteca.repository.LibroRepository;
import com.Proyecto.Biblioteca.repository.PrestamoRepository;
import com.Proyecto.Biblioteca.repository.UsuarioRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class PrestamoServiceImpl implements PrestamoService {

    private final PrestamoRepository prestamoRepository;
    private final EjemplarRepository ejemplarRepository;
    private final UsuarioRepository usuarioRepository;
    private final LibroRepository libroRepository;

    @Autowired
    public PrestamoServiceImpl(PrestamoRepository prestamoRepository, EjemplarRepository ejemplarRepository, UsuarioRepository usuarioRepository, LibroRepository libroRepository) {
        this.prestamoRepository = prestamoRepository;
        this.ejemplarRepository = ejemplarRepository;
        this.usuarioRepository = usuarioRepository;
        this.libroRepository = libroRepository;
    }


    @Override
    public List<Prestamo> obtenerTodosLosPrestamos() {
    return prestamoRepository.findAll();
    
    }

    

    @Override
    @Transactional
    public Prestamo devolverPrestamo(Long prestamoId) {
        Prestamo prestamo = prestamoRepository.findById(prestamoId)
                .orElseThrow(() -> new EntityNotFoundException("Préstamo no encontrado"));

        if (prestamo.getFechaDevolucion() != null) {
            throw new IllegalStateException("El préstamo ya ha sido devuelto.");
        }

        prestamo.setFechaDevolucion(LocalDate.now());
        
        if (prestamo.getFechaDevolucion().isAfter(prestamo.getFechaVencimiento())) {
            long diasAtraso = prestamo.getFechaVencimiento().until(prestamo.getFechaDevolucion()).getDays();
            double multa = diasAtraso * 0.5;
            prestamo.setMultaGenerada(multa);
        }
        
        Ejemplar ejemplar = prestamo.getEjemplar();
        ejemplar.setEstado(EstadoEjemplar.DISPONIBLE);
        ejemplarRepository.save(ejemplar);

        return prestamoRepository.save(prestamo);
    }

        //método que devuelve préstamos con multa Y préstamos atrasados
        public List<Prestamo> obtenerTodosLosPrestamosConMulta() {
        // Obtiene los préstamos con multas ya generadas (cuando ya se devolvió el libro)
        List<Prestamo> prestamosConMultaGenerada = prestamoRepository.findByMultaGeneradaGreaterThan(0.0);
        
        // Obtiene los préstamos que están atrasados pero no devueltos
        List<Prestamo> prestamosAtrasados = prestamoRepository.findByFechaVencimientoBeforeAndFechaDevolucionIsNull(LocalDate.now());

        // Combina las dos listas y evita duplicados
        List<Prestamo> resultado = new ArrayList<>();
        resultado.addAll(prestamosConMultaGenerada);
        resultado.addAll(prestamosAtrasados.stream()
                .filter(p -> !resultado.contains(p))
                .collect(Collectors.toList()));
        
        return resultado;
    }
    

    @Override
    public List<Prestamo> obtenerPrestamosPorUsuario(String username) {
        return prestamoRepository.findByUsuarioUsernameWithDetails(username);
    }

    @Override
    public List<Ejemplar> obtenerEjemplaresDisponibles(Long libroId) {
        return ejemplarRepository.findByLibroIdAndEstado(libroId, EstadoEjemplar.DISPONIBLE);
    }

    @Override
    public List<Libro> obtenerTodosLosLibros() {
        return libroRepository.findAll();
    }

    @Override
    public List<Usuario> obtenerTodosLosUsuarios() {
        return usuarioRepository.findAll();
    }

    @Override
    public List<Prestamo> obtenerPrestamosAtrasados() {
    return prestamoRepository.findByFechaVencimientoBeforeAndFechaDevolucionIsNull(LocalDate.now());
    }

    @Override
    public List<Prestamo> obtenerPrestamosConMulta() {
    return prestamoRepository.findByMultaGeneradaGreaterThan(0.0);
    }

    @Override
    public List<Prestamo> obtenerPrestamosAtrasadosPorUsuario(String username) {
        return prestamoRepository.findByUsuarioUsernameAndFechaVencimientoBeforeAndFechaDevolucionIsNull(username, LocalDate.now());
    }
    
    @Override
    public List<Prestamo> obtenerMultasGeneradasPorUsuario(String username) {
        return prestamoRepository.findByUsuarioUsernameAndMultaGeneradaGreaterThan(username, 0.0);
    }

    @Override
    public Prestamo obtenerPrestamoPorId(Long id) {
        return prestamoRepository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("Préstamo no encontrado"));
    }

    @Override
    public Prestamo guardarPrestamo(Prestamo prestamo) {
        return prestamoRepository.save(prestamo);
    }

    @Override
    public void pagarMulta(Long prestamoId) {
    Prestamo prestamo = prestamoRepository.findById(prestamoId)
        .orElseThrow(() -> new EntityNotFoundException("Préstamo no encontrado"));
    
    // Asigna un valor de 0.0 para "limpiar" la multa. Esto asegura que ya no aparecerá en el reporte de multas pendientes.
    prestamo.setMultaGenerada(0.0);
    prestamoRepository.save(prestamo);
    }
}