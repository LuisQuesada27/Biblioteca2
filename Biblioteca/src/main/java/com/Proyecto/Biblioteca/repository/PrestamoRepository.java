package com.Proyecto.Biblioteca.repository;

import com.Proyecto.Biblioteca.model.Prestamo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PrestamoRepository extends JpaRepository<Prestamo, Long> {
    
    // Consulta corregida para cargar todos los préstamos, incluso si les faltan datos
    @Query("SELECT p FROM Prestamo p LEFT JOIN FETCH p.ejemplar e LEFT JOIN FETCH e.libro LEFT JOIN FETCH p.usuario")
    List<Prestamo> findAllWithDetails();

}