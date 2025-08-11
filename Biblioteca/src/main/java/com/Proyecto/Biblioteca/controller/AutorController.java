package com.Proyecto.Biblioteca.controller;

import com.Proyecto.Biblioteca.model.Autor;
import com.Proyecto.Biblioteca.service.AutorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/autores")
public class AutorController {
    
    private final AutorService autorService;

    @Autowired
    public AutorController(AutorService autorService) {
        this.autorService = autorService;
    }

    @GetMapping("/listar")
    public String listarAutores(Model model) {
        List<Autor> autores = autorService.obtenerTodosLosAutores();
        model.addAttribute("autores", autores);
        return "autor-list";
    }

    @GetMapping("/crear")
    public String mostrarFormularioCreacion(Model model) {
        model.addAttribute("autor", new Autor());
        return "autor-form";
    }

    @PostMapping("/guardar")
    public String guardarAutor(@ModelAttribute Autor autor, RedirectAttributes redirectAttributes) {
        autorService.guardarAutor(autor);
        redirectAttributes.addFlashAttribute("mensaje", "Autor guardado exitosamente.");
        return "redirect:/autores/listar";
    }

    @GetMapping("/editar/{id}")
    public String mostrarFormularioEdicion(@PathVariable Long id, Model model) {
        Autor autor = autorService.obtenerAutorPorId(id);
        model.addAttribute("autor", autor);
        return "autor-form";
    }
}