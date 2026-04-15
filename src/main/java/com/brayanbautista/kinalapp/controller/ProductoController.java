package com.brayanbautista.kinalapp.controller;

import com.brayanbautista.kinalapp.entity.Producto;
import com.brayanbautista.kinalapp.service.IProductoService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/productos")
public class ProductoController {

    private final IProductoService productoService;

    public ProductoController(IProductoService productoService) {
        this.productoService = productoService;
    }

    @GetMapping
    public String listar(Model model) {
        model.addAttribute("productos", productoService.listarTodos());
        return "productos/lista";
    }

    @GetMapping("/nuevo")
    public String mostrarFormularioNuevo(Model model) {
        model.addAttribute("producto", new Producto());
        return "productos/formulario";
    }

    @PostMapping
    public String guardar(@ModelAttribute Producto producto, RedirectAttributes ra) {
        try {
            productoService.guardar(producto);
            ra.addFlashAttribute("successMsg", "Producto guardado correctamente.");
        } catch (Exception e) {
            ra.addFlashAttribute("errorMsg", "Error al guardar: " + e.getMessage());
        }
        return "redirect:/productos";
    }

    @GetMapping("/editar/{id}")
    public String mostrarFormularioEditar(@PathVariable Long id, Model model, RedirectAttributes ra) {
        return productoService.buscarPorId(id).map(p -> {
            model.addAttribute("producto", p);
            return "productos/formulario";
        }).orElseGet(() -> {
            ra.addFlashAttribute("errorMsg", "Producto no encontrado.");
            return "redirect:/productos";
        });
    }

    @PostMapping("/actualizar/{id}")
    public String actualizar(@PathVariable Long id, @ModelAttribute Producto producto, RedirectAttributes ra) {
        try {
            productoService.actualizar(id, producto);
            ra.addFlashAttribute("successMsg", "Producto actualizado correctamente.");
        } catch (Exception e) {
            ra.addFlashAttribute("errorMsg", "Error al actualizar: " + e.getMessage());
        }
        return "redirect:/productos";
    }

    @GetMapping("/eliminar/{id}")
    public String eliminar(@PathVariable Long id, RedirectAttributes ra) {
        try {
            productoService.eliminar(id);
            ra.addFlashAttribute("successMsg", "Producto eliminado correctamente.");
        } catch (Exception e) {
            ra.addFlashAttribute("errorMsg", "Error al eliminar: " + e.getMessage());
        }
        return "redirect:/productos";
    }
}
