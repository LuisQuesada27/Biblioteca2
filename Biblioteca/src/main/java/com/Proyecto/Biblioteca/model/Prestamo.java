package com.Proyecto.Biblioteca.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@Entity
public class Prestamo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_id")
    private Usuario usuario;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ejemplar_id")
    private Ejemplar ejemplar;

    private LocalDate fechaPrestamo;
    private LocalDate fechaVencimiento;
    private LocalDate fechaDevolucion;
    private Double multaGenerada;

    public Prestamo(Usuario usuario, Ejemplar ejemplar) {
    this.usuario = usuario;
    this.ejemplar = ejemplar;
    this.fechaPrestamo = LocalDate.now(); // Puedes inicializar la fecha aquí
    this.fechaVencimiento = LocalDate.now().plusDays(15); // Y la fecha de vencimiento
    this.multaGenerada = 0.0;
    }
}