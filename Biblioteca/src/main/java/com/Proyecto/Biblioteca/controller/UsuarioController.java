package com.Proyecto.Biblioteca.controller;

import com.Proyecto.Biblioteca.model.Role;
import com.Proyecto.Biblioteca.model.Usuario;
import com.Proyecto.Biblioteca.repository.UsuarioRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Optional;

@Controller
public class UsuarioController {

    private final UsuarioRepository usuarioRepository;

    public UsuarioController(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    @GetMapping("/usuarios") 
    public String listarUsuarios(Model model) {
        model.addAttribute("usuarios", usuarioRepository.findAll());
        return "usuarios"; 
    }

    @GetMapping("/usuarios/editar/{id}")
    public String mostrarFormularioEdicion(@PathVariable Long id, Model model) {
        Optional<Usuario> usuarioOptional = usuarioRepository.findById(id);
        if (usuarioOptional.isPresent()) {
            model.addAttribute("usuario", usuarioOptional.get());
            model.addAttribute("roles", Role.values()); 
            return "usuario-form"; 
        }
        return "redirect:/usuarios";
    }

    @PostMapping("/usuarios/editar")
    public String guardarUsuario(@ModelAttribute Usuario usuario, RedirectAttributes redirectAttributes) {
        Optional<Usuario> usuarioExistenteOptional = usuarioRepository.findById(usuario.getId());

        if (usuarioExistenteOptional.isPresent()) {
            Usuario usuarioExistente = usuarioExistenteOptional.get();
            usuarioExistente.setUsername(usuario.getUsername());
            usuarioExistente.setEmail(usuario.getEmail());
            usuarioExistente.setRol(usuario.getRol());
            
            usuarioRepository.save(usuarioExistente);
            redirectAttributes.addFlashAttribute("mensaje", "Usuario actualizado exitosamente.");
        } else {
            redirectAttributes.addFlashAttribute("error", "Usuario no encontrado.");
        }
        return "redirect:/usuarios";
    }

    @GetMapping("/usuarios/eliminar/{id}")
    public String eliminarUsuario(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        if (usuarioRepository.existsById(id)) {
            usuarioRepository.deleteById(id);
            redirectAttributes.addFlashAttribute("mensaje", "Usuario eliminado exitosamente.");
        } else {
            redirectAttributes.addFlashAttribute("error", "Usuario no encontrado.");
        }
        return "redirect:/usuarios";
    }
}