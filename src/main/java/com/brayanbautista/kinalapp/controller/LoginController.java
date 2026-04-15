package com.brayanbautista.kinalapp.controller;

import com.brayanbautista.kinalapp.entity.Usuario;
import com.brayanbautista.kinalapp.service.IUsuarioService;
import jakarta.servlet.http.HttpSession;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class LoginController {

    private final IUsuarioService usuarioService;
    private final PasswordEncoder passwordEncoder;

    public LoginController(IUsuarioService usuarioService, PasswordEncoder passwordEncoder) {
        this.usuarioService = usuarioService;
        this.passwordEncoder = passwordEncoder;
    }

    @GetMapping("/login")
    public String loginPage(Model model) {
        model.addAttribute("titulo", "Iniciar Sesión");
        return "login";
    }

    @PostMapping("/login")
    public String processLogin(@RequestParam String username,
                               @RequestParam String password,
                               HttpSession session,
                               RedirectAttributes ra) {
        var optUser = usuarioService.listarTodos().stream()
                .filter(u -> u.getUsername().equals(username))
                .findFirst();

        if (optUser.isPresent()) {
            Usuario user = optUser.get();
            if (passwordEncoder.matches(password, user.getPassword())) {
                if (user.getEstado() == 1) {
                    session.setAttribute("usuario", user);
                    return "redirect:/";
                } else {
                    ra.addFlashAttribute("error", "Usuario inactivo. Contacte al administrador.");
                }
            } else {
                ra.addFlashAttribute("error", "Usuario o contraseña incorrectos.");
            }
        } else {
            ra.addFlashAttribute("error", "Usuario o contraseña incorrectos.");
        }
        return "redirect:/login";
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/login?logout";
    }

    // ========== REGISTRO ==========
    @GetMapping("/registro")
    public String registroPage(Model model) {
        model.addAttribute("usuario", new Usuario());
        return "registro";
    }

    @PostMapping("/registro")
    public String procesarRegistro(@ModelAttribute Usuario usuario,
                                   RedirectAttributes ra) {
        try {
            // Validar que username no exista
            if (usuarioService.existePorUsername(usuario.getUsername())) {
                ra.addFlashAttribute("error", "El nombre de usuario ya está en uso.");
                return "redirect:/registro";
            }

            // Establecer valores por defecto
            usuario.setEstado(1); // Activo por defecto
            usuario.setRol("USER"); // Rol por defecto

            // Guardar (el servicio encripta la contraseña)
            usuarioService.guardar(usuario);

            ra.addFlashAttribute("success", "Registro exitoso. Ahora puedes iniciar sesión.");
            return "redirect:/login";
        } catch (Exception e) {
            ra.addFlashAttribute("error", "Error al registrar: " + e.getMessage());
            return "redirect:/registro";
        }
    }
}