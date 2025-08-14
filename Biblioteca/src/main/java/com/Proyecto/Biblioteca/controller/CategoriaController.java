package com.Proyecto.Biblioteca.controller;

import com.Proyecto.Biblioteca.model.Categoria;
import com.Proyecto.Biblioteca.service.CategoriaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/categorias")
public class CategoriaController {

    @Autowired
    private final CategoriaService categoriaService;

    @Autowired
    public CategoriaController(CategoriaService categoriaService) {
        this.categoriaService = categoriaService;
    }

    @GetMapping("/listar")
    public String listarCategorias(Model model) {
        List<Categoria> categorias = categoriaService.obtenerTodasLasCategorias();
        model.addAttribute("categorias", categorias);
        return "categoria-list";
    }

    @GetMapping("/crear")
    public String mostrarFormularioCreacion(Model model) {
        model.addAttribute("categoria", new Categoria());
        return "categoria-form";
    }

    @PostMapping("/guardar")
    public String guardarCategoria(@ModelAttribute Categoria categoria, RedirectAttributes redirectAttributes, Model model) {
        try {
            categoriaService.guardarCategoria(categoria);
            redirectAttributes.addFlashAttribute("mensaje", "Categoría guardada exitosamente.");
            return "redirect:/categorias/listar";
        } catch (IllegalArgumentException e) {
            // Se envía el error y el objeto "categoria" al formulario
            model.addAttribute("error", e.getMessage());
            model.addAttribute("categoria", categoria);
            return "categoria-form";
        }
    }

    @GetMapping("/editar/{id}")
    public String mostrarFormularioEdicion(@PathVariable Long id, Model model) {
        Categoria categoria = categoriaService.obtenerCategoriaPorId(id);
        model.addAttribute("categoria", categoria);
        return "categoria-form";
    }
}