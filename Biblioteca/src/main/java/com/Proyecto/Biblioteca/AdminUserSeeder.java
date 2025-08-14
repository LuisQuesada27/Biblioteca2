package com.Proyecto.Biblioteca;

import com.Proyecto.Biblioteca.model.*;
import com.Proyecto.Biblioteca.repository.*;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import java.time.LocalDate;
import java.util.Optional;
import java.util.ArrayList;
import java.util.List;

@Component
public class AdminUserSeeder implements CommandLineRunner {

    private final UsuarioRepository usuarioRepository;
    private final AutorRepository autorRepository;
    private final CategoriaRepository categoriaRepository;
    private final PasswordEncoder passwordEncoder;
    private final LibroRepository libroRepository;
    private final EjemplarRepository ejemplarRepository;
    private final PrestamoRepository prestamoRepository;

    public AdminUserSeeder(UsuarioRepository usuarioRepository, AutorRepository autorRepository, CategoriaRepository categoriaRepository, PasswordEncoder passwordEncoder, LibroRepository libroRepository, EjemplarRepository ejemplarRepository, PrestamoRepository prestamoRepository) {
        this.usuarioRepository = usuarioRepository;
        this.autorRepository = autorRepository;
        this.categoriaRepository = categoriaRepository;
        this.passwordEncoder = passwordEncoder;
        this.libroRepository = libroRepository;
        this.ejemplarRepository = ejemplarRepository;
        this.prestamoRepository = prestamoRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        crearAdminPorDefecto();
        crearUserPorDefecto();
        crearDatosDePrueba();
        crearDatosDePrestamo();
    }

    //Este metodo hace que cada vez que se inicie la aplicación, exista un usuario administrador por defecto.
    private void crearAdminPorDefecto() {
        System.out.println("Iniciando la creación de usuarios por defecto...");
        Optional<Usuario> adminOptional = usuarioRepository.findByUsername("admin");

        if (adminOptional.isEmpty()) {
            Usuario admin = new Usuario();
            admin.setUsername("admin");
            admin.setPassword(passwordEncoder.encode("password"));
            admin.setRol(Role.ADMIN);
            admin.setEmail("admin@biblioteca.com");
            usuarioRepository.save(admin);
            System.out.println("Usuario ADMIN creado exitosamente.");
        } else {
            System.out.println("El usuario ADMIN ya existe.");
        }
    }

        //Este metodo hace que cada vez que se inicie la aplicación, exista un usuario user por defecto.
    private void crearUserPorDefecto() {
        Optional<Usuario> userOptional = usuarioRepository.findByUsername("user");

        if (userOptional.isEmpty()) {
            Usuario user = new Usuario();
            user.setUsername("user");
            user.setPassword(passwordEncoder.encode("password"));
            user.setRol(Role.USER);
            user.setEmail("user@biblioteca.com");
            usuarioRepository.save(user);
            System.out.println("Usuario USER creado exitosamente.");
        } else {
            System.out.println("El usuario USER ya existe.");
        }
    }

    //Esto garantiza que siempre haya datos básicos, como un autor y una categoría.
    private void crearDatosDePrueba() {
    // Si no hay autores, los creamos
    if (autorRepository.count() == 0) {
        List<Autor> autores = new ArrayList<>();

        Autor autor1 = new Autor();
        autor1.setNombreCompleto("Isaac Asimov");
        autor1.setPaisOrigen("Rusia");
        autores.add(autor1);

        Autor autor2 = new Autor();
        autor2.setNombreCompleto("Stephen King");
        autor2.setPaisOrigen("Estados Unidos");
        autores.add(autor2);

        Autor autor3 = new Autor();
        autor3.setNombreCompleto("Jane Austen");
        autor3.setPaisOrigen("Reino Unido");
        autores.add(autor3);
        
        autorRepository.saveAll(autores);
        System.out.println("Autores por defecto creados.");
    }

    // Si no hay categorías, las creamos
    if (categoriaRepository.count() == 0) {
        List<Categoria> categorias = new ArrayList<>();

        Categoria categoria1 = new Categoria();
        categoria1.setNombre("Fantasía");
        categoria1.setDescripcion("Género que incluye elementos sobrenaturales o mágicos.");
        categorias.add(categoria1);
        
        Categoria categoria2 = new Categoria();
        categoria2.setNombre("Ciencia Ficción");
        categoria2.setDescripcion("Género basado en futuros tecnológicos y descubrimientos científicos.");
        categorias.add(categoria2);
        
        Categoria categoria3 = new Categoria();
        categoria3.setNombre("Misterio");
        categoria3.setDescripcion("Género centrado en la resolución de crímenes o acertijos.");
        categorias.add(categoria3);
        
        Categoria categoria4 = new Categoria();
        categoria4.setNombre("Biografía");
        categoria4.setDescripcion("Narración de la vida de una persona real.");
        categorias.add(categoria4);
        
        Categoria categoria5 = new Categoria();
        categoria5.setNombre("Historia");
        categoria5.setDescripcion("Género que relata eventos históricos reales.");
        categorias.add(categoria5);

        categoriaRepository.saveAll(categorias);
        System.out.println("Categorías por defecto creadas.");
    }
}

    private void crearDatosDePrestamo() {
        if (libroRepository.count() == 0) {
            // Crea Autores
            Autor autor1 = new Autor();
            autor1.setNombreCompleto("Gabriel García Márquez");
            autor1.setPaisOrigen("Colombia");
            autorRepository.save(autor1);
            
            Autor autor2 = new Autor();
            autor2.setNombreCompleto("J.R.R. Tolkien");
            autor2.setPaisOrigen("Sudáfrica");
            autorRepository.save(autor2);

            // Crea Libros 
            Libro libro1 = new Libro("Cien años de soledad", 1967, List.of(autor1));
            Libro libro2 = new Libro("El Señor de los Anillos", 1954, List.of(autor2));
            libroRepository.save(libro1);
            libroRepository.save(libro2);

            // Crea Ejemplares
            Ejemplar ejemplar1 = new Ejemplar(libro1, EstadoEjemplar.PRESTADO);
            Ejemplar ejemplar2 = new Ejemplar(libro2, EstadoEjemplar.DISPONIBLE);
            Ejemplar ejemplar3 = new Ejemplar(libro1, EstadoEjemplar.PRESTADO);
            ejemplarRepository.save(ejemplar1);
            ejemplarRepository.save(ejemplar2);
            ejemplarRepository.save(ejemplar3);

            // Crea Usuarios 
            Usuario usuario1 = usuarioRepository.findByUsername("user").orElseThrow();
            Usuario usuario2 = usuarioRepository.findByUsername("admin").orElseThrow();

            // Préstamo Atrasado
            Prestamo prestamoAtrasado = new Prestamo();
            prestamoAtrasado.setUsuario(usuario1);
            prestamoAtrasado.setEjemplar(ejemplar1);
            prestamoAtrasado.setFechaPrestamo(LocalDate.now().minusDays(20));
            prestamoAtrasado.setFechaVencimiento(LocalDate.now().minusDays(5));
            prestamoRepository.save(prestamoAtrasado);

            // Préstamo con Multa
            Prestamo prestamoConMulta = new Prestamo();
            prestamoConMulta.setUsuario(usuario2);
            prestamoConMulta.setEjemplar(ejemplar3);
            prestamoConMulta.setFechaPrestamo(LocalDate.now().minusDays(20));
            prestamoConMulta.setFechaVencimiento(LocalDate.now().minusDays(10));
            prestamoConMulta.setFechaDevolucion(LocalDate.now().minusDays(5));
            long diasAtraso = prestamoConMulta.getFechaVencimiento().until(prestamoConMulta.getFechaDevolucion()).getDays();
            prestamoConMulta.setMultaGenerada(diasAtraso * 300.00);
            prestamoRepository.save(prestamoConMulta);
        }
    }
}