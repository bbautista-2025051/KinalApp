package com.brayanbautista.kinalapp.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {
    // Spring Security ya maneja la autenticación/autorización de rutas.
    // No se necesita un interceptor adicional; el filtro de seguridad
    // configurado en SecurityConfig protege todas las rutas correctamente.
}
