package com.Proyecto.Biblioteca.service;

import com.Proyecto.Biblioteca.model.Ejemplar;
import com.Proyecto.Biblioteca.model.Libro;
import com.Proyecto.Biblioteca.model.Prestamo;
import com.Proyecto.Biblioteca.model.Usuario;

import java.util.List;

public interface PrestamoService {
  
    Prestamo crearPrestamo(Long usuarioId, Long ejemplarId);
    Prestamo devolverPrestamo(Long prestamoId);
    List<Prestamo> obtenerTodosLosPrestamos();
    List<Ejemplar> obtenerEjemplaresDisponibles(Long libroId);
    List<Libro> obtenerTodosLosLibros();
    List<Usuario> obtenerTodosLosUsuarios();
    List<Prestamo> obtenerPrestamosAtrasados();
    List<Prestamo> obtenerPrestamosConMulta();
    List<Prestamo> obtenerPrestamosPorUsuario(String username); 
    List<Prestamo> obtenerPrestamosAtrasadosPorUsuario(String username);
    List<Prestamo> obtenerMultasGeneradasPorUsuario(String username);
    Prestamo obtenerPrestamoPorId(Long id);
    Prestamo guardarPrestamo(Prestamo prestamo);
    List<Prestamo> obtenerTodosLosPrestamosConMulta();
    void pagarMulta(Long prestamoId);
}