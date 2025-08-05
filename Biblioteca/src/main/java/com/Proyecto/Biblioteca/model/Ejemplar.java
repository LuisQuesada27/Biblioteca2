package com.Proyecto.Biblioteca.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

@Data
@NoArgsConstructor
@Entity
public class Ejemplar {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "libro_id")
    private Libro libro;

    private String estado;
    
    // Relación 1-N con Prestamo
    @OneToMany(mappedBy = "ejemplar")
    @JsonIgnore // <-- AÑADE ESTA LÍNEA
    private List<Prestamo> prestamos;
}