package com.Proyecto.Biblioteca.repository;

import com.Proyecto.Biblioteca.model.Ejemplar;
import com.Proyecto.Biblioteca.model.EstadoEjemplar; // Importa el enum
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EjemplarRepository extends JpaRepository<Ejemplar, Long> {
    // Usa el enum EstadoEjemplar en el parámetro
    List<Ejemplar> findByLibroIdAndEstado(Long libroId, EstadoEjemplar estado);
}