package com.brayanbautista.kinalapp.controller;

import com.brayanbautista.kinalapp.entity.Cliente;
import com.brayanbautista.kinalapp.service.IClientesService;
import com.brayanbautista.kinalapp.util.RouteEncryptionUtil;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/clientes")
public class ClienteController {

    private final IClientesService clienteService;

    public ClienteController(IClientesService clienteService) {
        this.clienteService = clienteService;
    }

    @GetMapping
    public String listar(Model model) {
        model.addAttribute("clientes", clienteService.listarTodos());
        return "clientes/lista";
    }

    @GetMapping("/nuevo")
    @PreAuthorize("hasRole('ADMIN')")
    public String mostrarFormularioNuevo(Model model) {
        model.addAttribute("cliente", new Cliente());
        return "clientes/formulario";
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public String guardar(@ModelAttribute Cliente cliente, RedirectAttributes ra) {
        try {
            clienteService.guardar(cliente);
            ra.addFlashAttribute("successMsg", "Cliente guardado correctamente.");
        } catch (Exception e) {
            ra.addFlashAttribute("errorMsg", "Error al guardar: " + e.getMessage());
        }
        return "redirect:/clientes";
    }

    @GetMapping("/editar/{token}")
    @PreAuthorize("hasRole('ADMIN')")
    public String mostrarFormularioEditar(@PathVariable String token, Model model, RedirectAttributes ra) {
        String dpi = RouteEncryptionUtil.decrypt(token);
        return clienteService.buscarPorDPI(dpi).map(c -> {
            model.addAttribute("cliente", c);
            model.addAttribute("dpiToken", token);
            return "clientes/formulario";
        }).orElseGet(() -> {
            ra.addFlashAttribute("errorMsg", "Cliente no encontrado.");
            return "redirect:/clientes";
        });
    }

    @PostMapping("/actualizar/{token}")
    @PreAuthorize("hasRole('ADMIN')")
    public String actualizar(@PathVariable String token, @ModelAttribute Cliente cliente, RedirectAttributes ra) {
        try {
            String dpi = RouteEncryptionUtil.decrypt(token);
            clienteService.actualizar(dpi, cliente);
            ra.addFlashAttribute("successMsg", "Cliente actualizado correctamente.");
        } catch (Exception e) {
            ra.addFlashAttribute("errorMsg", "Error al actualizar: " + e.getMessage());
        }
        return "redirect:/clientes";
    }

    @GetMapping("/eliminar/{token}")
    @PreAuthorize("hasRole('ADMIN')")
    public String eliminar(@PathVariable String token, RedirectAttributes ra) {
        try {
            String dpi = RouteEncryptionUtil.decrypt(token);
            clienteService.eliminar(dpi);
            ra.addFlashAttribute("successMsg", "Cliente eliminado correctamente.");
        } catch (Exception e) {
            ra.addFlashAttribute("errorMsg", "Error al eliminar: " + e.getMessage());
        }
        return "redirect:/clientes";
    }
}