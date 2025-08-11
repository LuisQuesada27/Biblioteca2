package com.Proyecto.Biblioteca.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

@Data
@NoArgsConstructor
@Entity
@Table(name = "ejemplares")
public class Ejemplar {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Relación ManyToOne con Libro
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "libro_id")
    private Libro libro;

    // enum para el estado para garantizar valores válidos
    @Enumerated(EnumType.STRING)
    private EstadoEjemplar estado;
    
    // Relación OneToMany con Prestamo
    @OneToMany(mappedBy = "ejemplar")
    @JsonIgnore

    private List<Prestamo> prestamos;
    public Ejemplar(Libro libro, EstadoEjemplar estado) {
    this.libro = libro;
    this.estado = estado;
}
}