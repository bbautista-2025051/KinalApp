package com.brayanbautista.kinalapp.controller;

import com.brayanbautista.kinalapp.entity.Usuario;
import com.brayanbautista.kinalapp.service.IUsuarioService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/usuarios")
public class UsuarioController {

    private final IUsuarioService usuarioService;

    public UsuarioController(IUsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    @GetMapping
    public String listar(Model model) {
        model.addAttribute("usuarios", usuarioService.listarTodos());
        return "usuarios/lista";
    }

    @GetMapping("/nuevo")
    public String mostrarFormularioNuevo(Model model) {
        model.addAttribute("usuario", new Usuario());
        return "usuarios/formulario";
    }

    @PostMapping
    public String guardar(@ModelAttribute Usuario usuario) {
        usuarioService.guardar(usuario);
        return "redirect:/usuarios";
    }

    @GetMapping("/editar/{id}")
    public String mostrarFormularioEditar(@PathVariable Long id, Model model) {
        Usuario usuario = usuarioService.buscarPorId(id)
                .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado"));
        model.addAttribute("usuario", usuario);
        return "usuarios/formulario";
    }

    @PostMapping("/actualizar/{id}")
    public String actualizar(@PathVariable Long id, @ModelAttribute Usuario usuario) {
        usuarioService.actualizar(id, usuario);
        return "redirect:/usuarios";
    }

    @GetMapping("/eliminar/{id}")
    public String eliminar(@PathVariable Long id) {
        usuarioService.eliminar(id);
        return "redirect:/usuarios";
    }
}