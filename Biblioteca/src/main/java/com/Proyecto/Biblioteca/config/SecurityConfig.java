package com.Proyecto.Biblioteca.config;

import com.Proyecto.Biblioteca.service.UserDetailsServiceImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.hierarchicalroles.RoleHierarchy;
import org.springframework.security.access.hierarchicalroles.RoleHierarchyImpl;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final UserDetailsServiceImpl userDetailsService;

    public SecurityConfig(UserDetailsServiceImpl userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public RoleHierarchy roleHierarchy() {       //Configura una jerarquía de roles
        RoleHierarchyImpl roleHierarchy = new RoleHierarchyImpl();
        roleHierarchy.setHierarchy("ADMIN > USER");
        return roleHierarchy;
    }
    
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {    //define la cadena de filtros de seguridad.
        http
            .authorizeHttpRequests(authorize -> authorize   //Configura las reglas de autorización para diferentes URL
                // Reglas para el ADMIN
                .requestMatchers("/admin/**", "/h2-console/**").hasRole("ADMIN")
                .requestMatchers("/libros/crear", "/libros/editar/**", "/libros/eliminar/**").hasRole("ADMIN")
                .requestMatchers("/autores/crear", "/autores/editar/**", "/autores/eliminar/**").hasRole("ADMIN")
                
                // Reglas para USER y ADMIN
                .requestMatchers("/user/**", "/prestamos/**").hasAnyRole("ADMIN", "USER")
                
                // Reglas para todos (públicas)
                .requestMatchers("/", "/libros", "/login", "/registro").permitAll()

                // Todas las demás peticiones requieren autenticación
                .anyRequest().authenticated()
            )
            .formLogin(form -> form        //Configura el comportamiento del formulario de inicio de sesión
                .loginPage("/login").permitAll()
            )
            .logout(logout -> logout
                .permitAll()
            );

        http.csrf(csrf -> csrf.disable());    // Deshabilita la protección CSRF (Cross-Site Request Forgery).Es como una protección
        http.headers(headers -> headers.frameOptions(frame -> frame.disable()));

        return http.build();
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }
}