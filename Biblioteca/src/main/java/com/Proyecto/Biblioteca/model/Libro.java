package com.Proyecto.Biblioteca.model;

import jakarta.persistence.*;
import java.util.List;

@Entity
public class Libro {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String titulo;
    private Integer anioPublicacion;
    private String isbn;
    private String descripcion;

    @OneToMany(mappedBy = "libro", cascade = CascadeType.ALL)
    private List<Ejemplar> ejemplares;


    @ManyToOne
    private Categoria categoria;

    @ManyToMany
    @JoinTable(
        name = "libro_autor", // Nombre de la tabla de unión
        joinColumns = @JoinColumn(name = "libro_id"), // Columna que referencia a la tabla Libro
        inverseJoinColumns = @JoinColumn(name = "autor_id") // Columna que referencia a la tabla Autor
    )
    private List<Autor> autores;

    // Getters y Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public Integer getAnioPublicacion() {
        return anioPublicacion;
    }

    public void setAnioPublicacion(Integer anioPublicacion) {
        this.anioPublicacion = anioPublicacion;
    }

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public Categoria getCategoria() {
        return categoria;
    }

    public void setCategoria(Categoria categoria) {
        this.categoria = categoria;
    }

    public List<Autor> getAutores() {
        return autores;
    }

    public void setAutores(List<Autor> autores) {
        this.autores = autores;
    }
   
    public Libro() {
    }

    
    public Libro(String titulo, Integer anioPublicacion, List<Autor> autores) {
    this.titulo = titulo;
    this.anioPublicacion = anioPublicacion;
    this.autores = autores;
    }

    
}