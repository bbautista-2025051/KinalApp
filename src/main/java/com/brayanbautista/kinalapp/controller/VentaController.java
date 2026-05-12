package com.brayanbautista.kinalapp.controller;

import com.brayanbautista.kinalapp.entity.Cliente;
import com.brayanbautista.kinalapp.entity.Usuario;
import com.brayanbautista.kinalapp.entity.Venta;
import com.brayanbautista.kinalapp.service.IClientesService;
import com.brayanbautista.kinalapp.service.IUsuarioService;
import com.brayanbautista.kinalapp.service.IVentaService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.math.BigDecimal;

@Controller
@RequestMapping("/ventas")
public class VentaController {

    private final IVentaService ventaService;
    private final IClientesService clienteService;
    private final IUsuarioService usuarioService;

    public VentaController(IVentaService ventaService,
                           IClientesService clienteService,
                           IUsuarioService usuarioService) {
        this.ventaService   = ventaService;
        this.clienteService = clienteService;
        this.usuarioService = usuarioService;
    }

    @GetMapping
    public String listar(Model model) {
        model.addAttribute("ventas", ventaService.listarTodos());
        return "ventas/lista";
    }

    @GetMapping("/nuevo")
    public String mostrarFormularioNuevo(Model model) {
        model.addAttribute("venta",    new Venta());
        model.addAttribute("clientes", clienteService.listarTodos());
        model.addAttribute("usuarios", usuarioService.listarTodos());
        return "ventas/formulario";
    }

    @PostMapping
    public String guardar(@RequestParam String clienteDpi,
                          @RequestParam Long usuarioId,
                          @RequestParam BigDecimal total,
                          @RequestParam int estado,
                          RedirectAttributes ra) {
        try {
            Cliente cliente = clienteService.buscarPorDPI(clienteDpi)
                    .orElseThrow(() -> new RuntimeException("Cliente con DPI " + clienteDpi + " no encontrado"));
            Usuario usuario = usuarioService.buscarPorId(usuarioId)
                    .orElseThrow(() -> new RuntimeException("Usuario con ID " + usuarioId + " no encontrado"));

            Venta venta = new Venta(System.currentTimeMillis(), total, estado, cliente, usuario);
            ventaService.guardar(venta);
            ra.addFlashAttribute("successMsg", "Venta registrada correctamente.");
        } catch (Exception e) {
            ra.addFlashAttribute("errorMsg", "Error al guardar la venta: " + e.getMessage());
        }
        return "redirect:/ventas";
    }

    @GetMapping("/editar/{id}")
    public String mostrarFormularioEditar(@PathVariable Long id, Model model, RedirectAttributes ra) {
        return ventaService.buscarPorId(id).map(v -> {
            model.addAttribute("venta",    v);
            model.addAttribute("clientes", clienteService.listarTodos());
            model.addAttribute("usuarios", usuarioService.listarTodos());
            return "ventas/formulario";
        }).orElseGet(() -> {
            ra.addFlashAttribute("errorMsg", "Venta no encontrada.");
            return "redirect:/ventas";
        });
    }

    @PostMapping("/actualizar/{id}")
    public String actualizar(@PathVariable Long id,
                             @RequestParam String clienteDpi,
                             @RequestParam Long usuarioId,
                             @RequestParam BigDecimal total,
                             @RequestParam int estado,
                             @RequestParam(required = false, defaultValue = "0") long fechaVenta,
                             RedirectAttributes ra) {
        try {
            Cliente cliente = clienteService.buscarPorDPI(clienteDpi)
                    .orElseThrow(() -> new RuntimeException("Cliente no encontrado"));
            Usuario usuario = usuarioService.buscarPorId(usuarioId)
                    .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

            long fecha = fechaVenta > 0 ? fechaVenta : System.currentTimeMillis();
            Venta venta = new Venta(fecha, total, estado, cliente, usuario);
            ventaService.actualizar(id, venta);
            ra.addFlashAttribute("successMsg", "Venta actualizada correctamente.");
        } catch (Exception e) {
            ra.addFlashAttribute("errorMsg", "Error al actualizar: " + e.getMessage());
        }
        return "redirect:/ventas";
    }

    @GetMapping("/eliminar/{id}")
    public String eliminar(@PathVariable Long id, RedirectAttributes ra) {
        try {
            ventaService.eliminar(id);
            ra.addFlashAttribute("successMsg", "Venta eliminada correctamente.");
        } catch (Exception e) {
            ra.addFlashAttribute("errorMsg", "Error al eliminar: " + e.getMessage());
        }
        return "redirect:/ventas";
    }
}
