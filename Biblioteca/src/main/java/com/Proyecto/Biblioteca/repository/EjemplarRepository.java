package com.Proyecto.Biblioteca.repository;

import com.Proyecto.Biblioteca.model.Ejemplar;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EjemplarRepository extends JpaRepository<Ejemplar, Long> {
    List<Ejemplar> findByLibroIdAndEstado(Long libroId, String estado);
}