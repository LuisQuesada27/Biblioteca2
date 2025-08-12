package com.Proyecto.Biblioteca.repository;

import com.Proyecto.Biblioteca.model.Usuario;
import java.util.Optional; 
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
    
    // Busca un usuario por su nombre de usuario.
    Optional<Usuario> findByUsername(String username);
    
    // método para buscar un usuario por su correo electrónico.
    Optional<Usuario> findByEmail(String email);
}