package com.brayanbautista.kinalapp.controller;

import com.brayanbautista.kinalapp.entity.Usuario;
import com.brayanbautista.kinalapp.service.IUsuarioService;
import com.brayanbautista.kinalapp.util.RouteEncryptionUtil;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/usuarios")
public class UsuarioController {

    private final IUsuarioService usuarioService;

    public UsuarioController(IUsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public String listar(Model model) {
        model.addAttribute("usuarios", usuarioService.listarTodos());
        return "usuarios/lista";
    }

    @GetMapping("/nuevo")
    @PreAuthorize("hasRole('ADMIN')")
    public String mostrarFormularioNuevo(Model model) {
        model.addAttribute("usuario", new Usuario());
        return "usuarios/formulario";
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public String guardar(@ModelAttribute Usuario usuario, RedirectAttributes ra) {
        try {
            usuarioService.guardar(usuario);
            ra.addFlashAttribute("successMsg", "Usuario guardado.");
        } catch (Exception e) {
            ra.addFlashAttribute("errorMsg", "Error: " + e.getMessage());
        }
        return "redirect:/usuarios";
    }

    @GetMapping("/editar/{token}")
    @PreAuthorize("hasRole('ADMIN')")
    public String mostrarFormularioEditar(@PathVariable String token, Model model, RedirectAttributes ra) {
        Long id = RouteEncryptionUtil.decryptLong(token);
        return usuarioService.buscarPorId(id).map(u -> {
            model.addAttribute("usuario", u);
            model.addAttribute("idToken", token);
            return "usuarios/formulario";
        }).orElseGet(() -> {
            ra.addFlashAttribute("errorMsg", "Usuario no encontrado.");
            return "redirect:/usuarios";
        });
    }

    @PostMapping("/actualizar/{token}")
    @PreAuthorize("hasRole('ADMIN')")
    public String actualizar(@PathVariable String token, @ModelAttribute Usuario usuario, RedirectAttributes ra) {
        try {
            Long id = RouteEncryptionUtil.decryptLong(token);
            usuarioService.actualizar(id, usuario);
            ra.addFlashAttribute("successMsg", "Usuario actualizado.");
        } catch (Exception e) {
            ra.addFlashAttribute("errorMsg", "Error: " + e.getMessage());
        }
        return "redirect:/usuarios";
    }

    @GetMapping("/eliminar/{token}")
    @PreAuthorize("hasRole('ADMIN')")
    public String eliminar(@PathVariable String token, RedirectAttributes ra) {
        try {
            Long id = RouteEncryptionUtil.decryptLong(token);
            usuarioService.eliminar(id);
            ra.addFlashAttribute("successMsg", "Usuario eliminado.");
        } catch (Exception e) {
            ra.addFlashAttribute("errorMsg", "Error: " + e.getMessage());
        }
        return "redirect:/usuarios";
    }
}