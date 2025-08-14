package com.Proyecto.Biblioteca.service;

import com.Proyecto.Biblioteca.model.Categoria;
import com.Proyecto.Biblioteca.repository.CategoriaRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoriaServiceImpl implements CategoriaService {

    private final CategoriaRepository categoriaRepository;

    @Autowired
    public CategoriaServiceImpl(CategoriaRepository categoriaRepository) {
        this.categoriaRepository = categoriaRepository;
    }

    @Override
    public List<Categoria> obtenerTodasLasCategorias() {
        return categoriaRepository.findAll();
    }

    @Override
    public Categoria guardarCategoria(Categoria categoria) {
        // Buscar una categoría existente por nombre, ignorando mayúsculas/minúsculas
        Categoria categoriaExistente = categoriaRepository.findByNombreIgnoreCase(categoria.getNombre());
        
        // Si se encontró una categoría con el mismo nombre y su ID es diferente al que estamos intentando guardar...
        if (categoriaExistente != null && (categoria.getId() == null || !categoriaExistente.getId().equals(categoria.getId()))) {
            throw new IllegalArgumentException("Ya existe una categoría con ese nombre.");
        }
        
        // Si no hay duplicados, guarda el objeto.
        return categoriaRepository.save(categoria);
    }

    @Override
    public Categoria obtenerCategoriaPorId(Long id) {
        return categoriaRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Categoría no encontrada con ID: " + id));
    }

    @Override
    public void eliminarCategoria(Long id) {
        categoriaRepository.deleteById(id);
    }
}