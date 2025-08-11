package com.Proyecto.Biblioteca.service;

import com.Proyecto.Biblioteca.model.Categoria;
import java.util.List;

public interface CategoriaService {

    List<Categoria> obtenerTodasLasCategorias();

    Categoria guardarCategoria(Categoria categoria);

    Categoria obtenerCategoriaPorId(Long id);

    void eliminarCategoria(Long id);
}