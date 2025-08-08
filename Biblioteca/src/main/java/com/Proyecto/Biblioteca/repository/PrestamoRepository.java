package com.Proyecto.Biblioteca.repository;

import com.Proyecto.Biblioteca.model.Prestamo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface PrestamoRepository extends JpaRepository<Prestamo, Long> {
    
    // Método para contar préstamos activos de un usuario
    long countByUsuarioAndFechaDevolucionIsNull(com.Proyecto.Biblioteca.model.Usuario usuario);

    // Método para traer todos los préstamos con detalles del ejemplar y el usuario
    @Query("SELECT p FROM Prestamo p LEFT JOIN FETCH p.ejemplar e LEFT JOIN FETCH e.libro LEFT JOIN FETCH p.usuario")
    List<Prestamo> findAllWithDetails();

    // **Nuevos métodos para los reportes**
    List<Prestamo> findByFechaVencimientoBeforeAndFechaDevolucionIsNull(LocalDate fecha);
    List<Prestamo> findByMultaGeneradaGreaterThan(double multa);
}