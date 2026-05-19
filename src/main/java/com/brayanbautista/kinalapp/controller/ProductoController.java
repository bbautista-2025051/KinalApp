package com.brayanbautista.kinalapp.controller;

import com.brayanbautista.kinalapp.entity.Producto;
import com.brayanbautista.kinalapp.service.IProductoService;
import com.brayanbautista.kinalapp.util.RouteEncryptionUtil;
import org.springframework.security.access.prepost.PreAuthorize;
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

    // CORRECCIÓN: Permitir que USER también acceda a la vista del formulario nuevo
    @GetMapping("/nuevo")
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    public String mostrarFormularioNuevo(Model model) {
        model.addAttribute("producto", new Producto());
        return "productos/formulario";
    }

    // CORRECCIÓN: Permitir que USER también pueda enviar el formulario para guardar (POST)
    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    public String guardar(@ModelAttribute Producto producto, RedirectAttributes ra) {
        try {
            productoService.guardar(producto);
            ra.addFlashAttribute("successMsg", "Producto guardado correctamente.");
        } catch (Exception e) {
            ra.addFlashAttribute("errorMsg", "Error al guardar: " + e.getMessage());
        }
        return "redirect:/productos";
    }

    // MANTIENE SEGURIDAD: Solo ADMIN puede ver el formulario de edición
    @GetMapping("/editar/{token}")
    @PreAuthorize("hasRole('ADMIN')")
    public String mostrarFormularioEditar(@PathVariable String token, Model model, RedirectAttributes ra) {
        Long id = RouteEncryptionUtil.decryptLong(token);
        return productoService.buscarPorId(id).map(p -> {
            model.addAttribute("producto", p);
            model.addAttribute("idToken", token);
            return "productos/formulario";
        }).orElseGet(() -> {
            ra.addFlashAttribute("errorMsg", "Producto no encontrado.");
            return "redirect:/productos";
        });
    }

    // MANTIENE SEGURIDAD: Solo ADMIN puede procesar la actualización
    @PostMapping("/actualizar/{token}")
    @PreAuthorize("hasRole('ADMIN')")
    public String actualizar(@PathVariable String token, @ModelAttribute Producto producto, RedirectAttributes ra) {
        try {
            Long id = RouteEncryptionUtil.decryptLong(token);
            productoService.actualizar(id, producto);
            ra.addFlashAttribute("successMsg", "Producto actualizado correctamente.");
        } catch (Exception e) {
            ra.addFlashAttribute("errorMsg", "Error al actualizar: " + e.getMessage());
        }
        return "redirect:/productos";
    }

    // MANTIENE SEGURIDAD: Solo ADMIN puede eliminar un producto
    @GetMapping("/eliminar/{token}")
    @PreAuthorize("hasRole('ADMIN')")
    public String eliminar(@PathVariable String token, RedirectAttributes ra) {
        try {
            Long id = RouteEncryptionUtil.decryptLong(token);
            productoService.eliminar(id);
            ra.addFlashAttribute("successMsg", "Producto eliminado correctamente.");
        } catch (Exception e) {
            ra.addFlashAttribute("errorMsg", "Error al eliminar: " + e.getMessage());
        }
        return "redirect:/productos";
    }
}