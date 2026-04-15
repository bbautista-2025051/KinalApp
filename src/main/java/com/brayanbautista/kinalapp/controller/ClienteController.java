package com.brayanbautista.kinalapp.controller;

import com.brayanbautista.kinalapp.entity.Cliente;
import com.brayanbautista.kinalapp.service.IClientesService;
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
    public String mostrarFormularioNuevo(Model model) {
        model.addAttribute("cliente", new Cliente());
        return "clientes/formulario";
    }

    @PostMapping
    public String guardar(@ModelAttribute Cliente cliente, RedirectAttributes ra) {
        try {
            clienteService.guardar(cliente);
            ra.addFlashAttribute("successMsg", "Cliente guardado correctamente.");
        } catch (Exception e) {
            ra.addFlashAttribute("errorMsg", "Error al guardar: " + e.getMessage());
        }
        return "redirect:/clientes";
    }

    @GetMapping("/editar/{dpi}")
    public String mostrarFormularioEditar(@PathVariable String dpi, Model model, RedirectAttributes ra) {
        return clienteService.buscarPorDPI(dpi).map(c -> {
            model.addAttribute("cliente", c);
            return "clientes/formulario";
        }).orElseGet(() -> {
            ra.addFlashAttribute("errorMsg", "Cliente no encontrado.");
            return "redirect:/clientes";
        });
    }

    @PostMapping("/actualizar/{dpi}")
    public String actualizar(@PathVariable String dpi, @ModelAttribute Cliente cliente, RedirectAttributes ra) {
        try {
            clienteService.actualizar(dpi, cliente);
            ra.addFlashAttribute("successMsg", "Cliente actualizado correctamente.");
        } catch (Exception e) {
            ra.addFlashAttribute("errorMsg", "Error al actualizar: " + e.getMessage());
        }
        return "redirect:/clientes";
    }

    @GetMapping("/eliminar/{dpi}")
    public String eliminar(@PathVariable String dpi, RedirectAttributes ra) {
        try {
            clienteService.eliminar(dpi);
            ra.addFlashAttribute("successMsg", "Cliente eliminado correctamente.");
        } catch (Exception e) {
            ra.addFlashAttribute("errorMsg", "Error al eliminar: " + e.getMessage());
        }
        return "redirect:/clientes";
    }
}
