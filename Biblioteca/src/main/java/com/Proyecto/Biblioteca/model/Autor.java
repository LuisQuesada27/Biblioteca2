package com.Proyecto.Biblioteca.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
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
    @JsonBackReference // Esto es suficiente para evitar el bucle
    private Set<Libro> libros;
}