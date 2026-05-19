package com.brayanbautista.kinalapp.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(auth -> auth
                        // 1. Recursos públicos
                        .requestMatchers("/css/**", "/js/**", "/login", "/registro").permitAll()

                        // 2. Permisos de Lectura (GET) compartidos
                        .requestMatchers(HttpMethod.GET, "/clientes/**").hasAnyRole("ADMIN", "USER")
                        .requestMatchers(HttpMethod.GET, "/productos/**").hasAnyRole("ADMIN", "USER")

                        // 3. Permisos de Creación de Productos (Habilitado para ADMIN y USER)
                        .requestMatchers("/productos/nuevo/**").hasAnyRole("ADMIN", "USER")
                        .requestMatchers(HttpMethod.POST, "/productos/**").hasAnyRole("ADMIN", "USER")

                        // 4. El resto de accesos de Lectura (GET) - Solo ADMIN
                        .requestMatchers(HttpMethod.GET, "/ventas/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.GET, "/usuarios/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.GET, "/detalles-venta/**").hasRole("ADMIN")

                        // 5. Modificaciones de Productos (Editar/Eliminar) - Solo ADMIN
                        .requestMatchers("/productos/editar/**", "/productos/eliminar/**", "/productos/actualizar/**").hasRole("ADMIN")

                        // 6. Operaciones del resto de entidades (Clientes, Ventas, etc.) - Solo ADMIN
                        .requestMatchers("/clientes/nuevo", "/clientes/editar/**", "/clientes/eliminar/**", "/clientes/actualizar/**").hasRole("ADMIN")
                        .requestMatchers("/ventas/nuevo", "/ventas/editar/**", "/ventas/eliminar/**", "/ventas/actualizar/**").hasRole("ADMIN")
                        .requestMatchers("/usuarios/nuevo", "/usuarios/editar/**", "/usuarios/eliminar/**", "/usuarios/actualizar/**").hasRole("ADMIN")
                        .requestMatchers("/detalles-venta/nuevo", "/detalles-venta/editar/**", "/detalles-venta/eliminar/**", "/detalles-venta/actualizar/**").hasRole("ADMIN")

                        // 7. Regla de respaldo para cualquier otra petición POST que no se haya filtrado arriba
                        .requestMatchers(HttpMethod.POST, "/**").hasRole("ADMIN")

                        .anyRequest().authenticated()
                )
                .formLogin(form -> form
                        .loginPage("/login")
                        .loginProcessingUrl("/login")
                        .usernameParameter("username")
                        .passwordParameter("password")
                        .defaultSuccessUrl("/", true)
                        .failureUrl("/login?error")
                        .permitAll()
                )
                .logout(logout -> logout
                        .logoutUrl("/logout")
                        .logoutSuccessUrl("/login?logout")
                        .invalidateHttpSession(true)
                        .permitAll()
                )
                .exceptionHandling(ex -> ex
                        .accessDeniedPage("/acceso-denegado")
                );

        return http.build();
    }
}