package com.Proyecto.Biblioteca;

import com.Proyecto.Biblioteca.model.Autor;
import com.Proyecto.Biblioteca.model.Categoria;
import com.Proyecto.Biblioteca.model.Usuario;
import com.Proyecto.Biblioteca.model.Role;
import com.Proyecto.Biblioteca.repository.AutorRepository;
import com.Proyecto.Biblioteca.repository.CategoriaRepository;
import com.Proyecto.Biblioteca.repository.UsuarioRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class AdminUserSeeder implements CommandLineRunner {

    private final UsuarioRepository usuarioRepository;
    private final AutorRepository autorRepository;
    private final CategoriaRepository categoriaRepository;
    private final PasswordEncoder passwordEncoder;

    public AdminUserSeeder(UsuarioRepository usuarioRepository, AutorRepository autorRepository, CategoriaRepository categoriaRepository, PasswordEncoder passwordEncoder) {
        this.usuarioRepository = usuarioRepository;
        this.autorRepository = autorRepository;
        this.categoriaRepository = categoriaRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) throws Exception {
        crearAdminPorDefecto();
        crearUserPorDefecto(); // <-- ¡Añade esta línea!
        crearDatosDePrueba();
    }

    private void crearAdminPorDefecto() {
        System.out.println("-----> Iniciando la creación de usuarios por defecto...");
        Optional<Usuario> adminOptional = usuarioRepository.findByUsername("admin");

        if (adminOptional.isEmpty()) {
            Usuario admin = new Usuario();
            admin.setUsername("admin");
            admin.setPassword(passwordEncoder.encode("password"));
            admin.setRol(Role.ADMIN);
            admin.setEmail("admin@biblioteca.com");
            usuarioRepository.save(admin);
            System.out.println("-----> Usuario ADMIN creado exitosamente.");
        } else {
            System.out.println("-----> El usuario ADMIN ya existe.");
        }
    }

    // --- Nuevo método para crear el usuario USER por defecto ---
    private void crearUserPorDefecto() {
        Optional<Usuario> userOptional = usuarioRepository.findByUsername("user");

        if (userOptional.isEmpty()) {
            Usuario user = new Usuario();
            user.setUsername("user");
            user.setPassword(passwordEncoder.encode("password"));
            user.setRol(Role.USER);
            user.setEmail("user@biblioteca.com");
            usuarioRepository.save(user);
            System.out.println("-----> Usuario USER creado exitosamente.");
        } else {
            System.out.println("-----> El usuario USER ya existe.");
        }
    }

    private void crearDatosDePrueba() {
        if (autorRepository.count() == 0) {
            Autor autor = new Autor();
            autor.setNombreCompleto("J.R.R. Tolkien");
            autor.setPaisOrigen("Sudáfrica");
            autorRepository.save(autor);
            System.out.println("-----> Autor por defecto creado.");
        }

        if (categoriaRepository.count() == 0) {
            Categoria categoria = new Categoria();
            categoria.setNombre("Fantasía");
            categoria.setDescripcion("Género que incluye elementos sobrenaturales o mágicos.");
            categoriaRepository.save(categoria);
            System.out.println("-----> Categoría por defecto creada.");
        }
    }
}