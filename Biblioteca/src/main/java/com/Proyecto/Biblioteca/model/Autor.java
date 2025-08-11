package com.Proyecto.Biblioteca.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.Set;

@Data
@NoArgsConstructor
@Entity
@Table(name = "autores") 
public class Autor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nombreCompleto;
    private String paisOrigen;

    // Relación N-N con Libro (mappedBy para evitar tabla duplicada)
    @ManyToMany(mappedBy = "autores")
    private Set<Libro> libros;
}