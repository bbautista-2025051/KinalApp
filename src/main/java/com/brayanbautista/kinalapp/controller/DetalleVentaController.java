package com.brayanbautista.kinalapp.controller;

import com.brayanbautista.kinalapp.entity.DetalleVenta;
import com.brayanbautista.kinalapp.service.IDetalleVentaService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/detalles-venta")
public class DetalleVentaController {

    private final IDetalleVentaService detalleVentaService;

    public DetalleVentaController(IDetalleVentaService detalleVentaService) {
        this.detalleVentaService = detalleVentaService;
    }

    @GetMapping
    public String listar(Model model) {
        model.addAttribute("detalles", detalleVentaService.listarTodos());
        return "detalles/lista";
    }

    @GetMapping("/nuevo")
    public String mostrarFormularioNuevo(Model model) {
        model.addAttribute("detalle", new DetalleVenta());
        return "detalles/formulario";
    }

    @PostMapping
    public String guardar(@ModelAttribute DetalleVenta detalle) {
        detalleVentaService.guardar(detalle);
        return "redirect:/detalles-venta";
    }

    @GetMapping("/editar/{id}")
    public String mostrarFormularioEditar(@PathVariable Long id, Model model) {
        DetalleVenta detalle = detalleVentaService.buscarPorId(id)
                .orElseThrow(() -> new IllegalArgumentException("Detalle no encontrado"));
        model.addAttribute("detalle", detalle);
        return "detalles/formulario";
    }

    @PostMapping("/actualizar/{id}")
    public String actualizar(@PathVariable Long id, @ModelAttribute DetalleVenta detalle) {
        detalleVentaService.actualizar(id, detalle);
        return "redirect:/detalles-venta";
    }

    @GetMapping("/eliminar/{id}")
    public String eliminar(@PathVariable Long id) {
        detalleVentaService.eliminar(id);
        return "redirect:/detalles-venta";
    }
}