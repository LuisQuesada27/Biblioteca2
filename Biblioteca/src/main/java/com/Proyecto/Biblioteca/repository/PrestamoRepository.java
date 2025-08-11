package com.Proyecto.Biblioteca.repository;

import com.Proyecto.Biblioteca.model.Prestamo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface PrestamoRepository extends JpaRepository<Prestamo, Long> {
    
    long countByUsuarioAndFechaDevolucionIsNull(com.Proyecto.Biblioteca.model.Usuario usuario);

    @Query("SELECT p FROM Prestamo p LEFT JOIN FETCH p.ejemplar e LEFT JOIN FETCH e.libro LEFT JOIN FETCH p.usuario")
    List<Prestamo> findAllWithDetails();
    
    
    @Query("SELECT p FROM Prestamo p LEFT JOIN FETCH p.ejemplar e LEFT JOIN FETCH e.libro LEFT JOIN FETCH p.usuario WHERE p.usuario.username = :username")
    List<Prestamo> findByUsuarioUsernameWithDetails(@Param("username") String username);

    List<Prestamo> findByFechaVencimientoBeforeAndFechaDevolucionIsNull(LocalDate fecha);
    List<Prestamo> findByMultaGeneradaGreaterThan(double multa);

    // Este método es necesario para el servicio 'obtenerPrestamosAtrasadosPorUsuario'
    List<Prestamo> findByUsuarioUsernameAndFechaVencimientoBeforeAndFechaDevolucionIsNull(String username, LocalDate fecha);
    
    // Este método es necesario para el servicio 'obtenerMultasGeneradasPorUsuario'
    List<Prestamo> findByUsuarioUsernameAndMultaGeneradaGreaterThan(String username, double multa);
}
