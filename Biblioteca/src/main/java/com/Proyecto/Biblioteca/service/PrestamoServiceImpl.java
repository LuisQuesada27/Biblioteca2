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
import java.util.List;

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
    @Transactional
    public Prestamo crearPrestamo(Long usuarioId, Long ejemplarId) {
        Usuario usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new EntityNotFoundException("Usuario no encontrado"));
        Ejemplar ejemplar = ejemplarRepository.findById(ejemplarId)
                .orElseThrow(() -> new EntityNotFoundException("Ejemplar no encontrado"));

        if (ejemplar.getEstado() != EstadoEjemplar.DISPONIBLE) {
            throw new IllegalStateException("El ejemplar no está disponible para préstamo.");
        }
        
        long prestamosActivos = prestamoRepository.countByUsuarioAndFechaDevolucionIsNull(usuario);
        if (prestamosActivos >= 5) {
            throw new IllegalStateException("El usuario ya tiene el máximo de 5 préstamos activos.");
        }

        Prestamo nuevoPrestamo = new Prestamo();
        nuevoPrestamo.setUsuario(usuario);
        nuevoPrestamo.setEjemplar(ejemplar);
        nuevoPrestamo.setFechaPrestamo(LocalDate.now());
        nuevoPrestamo.setFechaVencimiento(LocalDate.now().plusDays(15));
        
        ejemplar.setEstado(EstadoEjemplar.PRESTADO);
        ejemplarRepository.save(ejemplar);

        return prestamoRepository.save(nuevoPrestamo);
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
            double multa = diasAtraso * 0.5; // Multa de 0.5 por día
            prestamo.setMultaGenerada(multa);
        }
        
        Ejemplar ejemplar = prestamo.getEjemplar();
        ejemplar.setEstado(EstadoEjemplar.DISPONIBLE);
        ejemplarRepository.save(ejemplar);

        return prestamoRepository.save(prestamo);
    }

    @Override
    public List<Prestamo> obtenerTodosLosPrestamos() {
        return prestamoRepository.findAllWithDetails();
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
}