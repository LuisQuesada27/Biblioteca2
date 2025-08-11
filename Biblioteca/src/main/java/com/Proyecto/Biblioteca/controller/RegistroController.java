package com.Proyecto.Biblioteca.controller;

import com.Proyecto.Biblioteca.model.Usuario;
import com.Proyecto.Biblioteca.repository.UsuarioRepository;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder; // <-- IMPORTANTE
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import com.Proyecto.Biblioteca.model.Role;

@Controller
public class RegistroController {

    private final UsuarioRepository usuarioRepository;
    private final BCryptPasswordEncoder passwordEncoder; 
 
    public RegistroController(UsuarioRepository usuarioRepository, BCryptPasswordEncoder passwordEncoder) {
    this.usuarioRepository = usuarioRepository;
    this.passwordEncoder = passwordEncoder;
    }

    //muestra la página de registro al usuario
    @GetMapping("/registro")
    public String mostrarFormularioRegistro(Model model) {
         model.addAttribute("usuario", new Usuario());
    return "registro";
    }

    //procesa los datos enviados desde el formulario de registro
    @PostMapping("/registro")
    public String registrarUsuario(@ModelAttribute Usuario usuario, RedirectAttributes redirectAttributes) {
    // Encriptar la contraseña antes de guardarla
    usuario.setPassword(passwordEncoder.encode(usuario.getPassword()));

   
    usuario.setRol(Role.USER);

    usuarioRepository.save(usuario);
    redirectAttributes.addFlashAttribute("mensaje", "Usuario registrado exitosamente. Por favor, inicia sesión.");
    return "redirect:/login";
    }
}