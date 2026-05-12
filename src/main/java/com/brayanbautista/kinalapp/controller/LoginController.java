package com.brayanbautista.kinalapp.controller;

import com.brayanbautista.kinalapp.entity.Usuario;
import com.brayanbautista.kinalapp.service.IUsuarioService;
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

    public LoginController(IUsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }
    @GetMapping("/login")
    public String loginPage(@RequestParam(value = "error", required = false) String error,
                            @RequestParam(value = "logout", required = false) String logout,
                            Model model) {
        if (error != null) {
            model.addAttribute("error", "Usuario o contraseña incorrectos, o usuario inactivo.");
        }
        if (logout != null) {
            model.addAttribute("logout", true);
        }
        model.addAttribute("titulo", "Iniciar Sesión");
        return "login";
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
            if (usuarioService.existePorUsername(usuario.getUsername())) {
                ra.addFlashAttribute("error", "El nombre de usuario ya está en uso.");
                return "redirect:/registro";
            }

            usuario.setEstado(1);
            usuario.setRol("USER");

            usuarioService.guardar(usuario);

            ra.addFlashAttribute("success", "Registro exitoso. Ahora puedes iniciar sesión.");
            return "redirect:/login";
        } catch (Exception e) {
            ra.addFlashAttribute("error", "Error al registrar: " + e.getMessage());
            return "redirect:/registro";
        }
    }
}
