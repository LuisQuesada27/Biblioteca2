package com.Proyecto.Biblioteca.repository;

import com.Proyecto.Biblioteca.model.Prestamo;
import com.Proyecto.Biblioteca.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface PrestamoRepository extends JpaRepository<Prestamo, Long> {
    
    long countByUsuarioAndFechaDevolucionIsNull(Usuario usuario);

    // NUEVO MÉTODO DE VALIDACIÓN: verifica si un usuario tiene un préstamo sin devolver con multa.
    boolean existsByUsuarioAndMultaGeneradaGreaterThan(Usuario usuario, double multaMinima);

    // NUEVO MÉTODO DE VALIDACIÓN: verifica si un usuario tiene préstamos atrasados sin devolver.
    boolean existsByUsuarioAndFechaVencimientoBeforeAndFechaDevolucionIsNull(Usuario usuario, LocalDate fecha);

    @Query("SELECT p FROM Prestamo p LEFT JOIN FETCH p.ejemplar e LEFT JOIN FETCH e.libro LEFT JOIN FETCH p.usuario")
    List<Prestamo> findAllWithDetails();
    
    @Query("SELECT p FROM Prestamo p LEFT JOIN FETCH p.ejemplar e LEFT JOIN FETCH e.libro LEFT JOIN FETCH p.usuario WHERE p.usuario.username = :username")
    List<Prestamo> findByUsuarioUsernameWithDetails(@Param("username") String username);

    List<Prestamo> findByFechaVencimientoBeforeAndFechaDevolucionIsNull(LocalDate fecha);
    List<Prestamo> findByMultaGeneradaGreaterThan(double multa);

    List<Prestamo> findByUsuarioUsernameAndFechaVencimientoBeforeAndFechaDevolucionIsNull(String username, LocalDate fecha);
    
    List<Prestamo> findByUsuarioUsernameAndMultaGeneradaGreaterThan(String username, double multa);

    @Query("SELECT e.libro.titulo, COUNT(p) " +
            "FROM Prestamo p JOIN p.ejemplar e " +
            "GROUP BY e.libro.titulo " +
            "ORDER BY COUNT(p) DESC")
    List<Object[]> findLibrosMasPrestados();
}