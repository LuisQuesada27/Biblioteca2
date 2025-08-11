package com.Proyecto.Biblioteca.controller;

import com.Proyecto.Biblioteca.model.Usuario;
import com.Proyecto.Biblioteca.repository.UsuarioRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import java.util.Optional;

@Controller
public class UserController {

    private final UsuarioRepository usuarioRepository;

    public UserController(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    // Define el endpoint para acceder a la página de perfil del usuario.
    @GetMapping("/user/perfil")
    public String userProfile(Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();

        Optional<Usuario> usuarioOptional = usuarioRepository.findByUsername(username);

        if (usuarioOptional.isPresent()) {
            model.addAttribute("usuario", usuarioOptional.get());
        } else {
            // Manejar el caso si el usuario no se encuentra, por ejemplo, redirigiendo a la página de inicio de sesión
            return "redirect:/login";
        }

        return "user/perfil";
    }
}