package com.brayanbautista.kinalapp.controller;

import com.brayanbautista.kinalapp.entity.DetalleVenta;
import com.brayanbautista.kinalapp.service.IDetalleVentaService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/detalles-venta")
public class DetalleVentaController {

    private final IDetalleVentaService detalleVentaService;

    public DetalleVentaController(IDetalleVentaService detalleVentaService) {
        this.detalleVentaService = detalleVentaService;
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public String listar(Model model) {
        model.addAttribute("detalles", detalleVentaService.listarTodos());
        return "detalles-venta/lista";
    }

    @GetMapping("/nuevo")
    @PreAuthorize("hasRole('ADMIN')")
    public String mostrarFormularioNuevo(Model model) {
        model.addAttribute("detalle", new DetalleVenta());
        return "detalles-venta/formulario";
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public String guardar(@ModelAttribute DetalleVenta detalle, RedirectAttributes ra) {
        try {
            detalleVentaService.guardar(detalle);
            ra.addFlashAttribute("successMsg", "Detalle guardado.");
        } catch (Exception e) {
            ra.addFlashAttribute("errorMsg", "Error: " + e.getMessage());
        }
        return "redirect:/detalles-venta";
    }

    @GetMapping("/editar/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public String mostrarFormularioEditar(@PathVariable Long id, Model model, RedirectAttributes ra) {
        return detalleVentaService.buscarPorId(id).map(d -> {
            model.addAttribute("detalle", d);
            return "detalles-venta/formulario";
        }).orElseGet(() -> {
            ra.addFlashAttribute("errorMsg", "Detalle no encontrado.");
            return "redirect:/detalles-venta";
        });
    }

    @PostMapping("/actualizar/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public String actualizar(@PathVariable Long id, @ModelAttribute DetalleVenta detalle, RedirectAttributes ra) {
        try {
            detalleVentaService.actualizar(id, detalle);
            ra.addFlashAttribute("successMsg", "Detalle actualizado.");
        } catch (Exception e) {
            ra.addFlashAttribute("errorMsg", "Error: " + e.getMessage());
        }
        return "redirect:/detalles-venta";
    }

    @GetMapping("/eliminar/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public String eliminar(@PathVariable Long id, RedirectAttributes ra) {
        try {
            detalleVentaService.eliminar(id);
            ra.addFlashAttribute("successMsg", "Detalle eliminado.");
        } catch (Exception e) {
            ra.addFlashAttribute("errorMsg", "Error: " + e.getMessage());
        }
        return "redirect:/detalles-venta";
    }
}