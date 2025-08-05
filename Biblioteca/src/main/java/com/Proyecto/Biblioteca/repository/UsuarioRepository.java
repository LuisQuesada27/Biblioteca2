package com.Proyecto.Biblioteca.repository;

import com.Proyecto.Biblioteca.model.Usuario;
import java.util.Optional; // ¡Necesitas esta importación!
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
    // Ahora devuelve un Optional para manejar la posibilidad de que no se encuentre el usuario
    Optional<Usuario> findByUsername(String username);
}