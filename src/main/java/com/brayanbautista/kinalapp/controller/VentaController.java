package com.brayanbautista.kinalapp.controller;

import com.brayanbautista.kinalapp.entity.Cliente;
import com.brayanbautista.kinalapp.entity.Usuario;
import com.brayanbautista.kinalapp.entity.Venta;
import com.brayanbautista.kinalapp.service.IClientesService;
import com.brayanbautista.kinalapp.service.IUsuarioService;
import com.brayanbautista.kinalapp.service.IVentaService;
import com.brayanbautista.kinalapp.util.RouteEncryptionUtil;
import org.springframework.security.access.prepost.PreAuthorize;
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

    public VentaController(IVentaService ventaService, IClientesService clienteService, IUsuarioService usuarioService) {
        this.ventaService = ventaService;
        this.clienteService = clienteService;
        this.usuarioService = usuarioService;
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public String listar(Model model) {
        model.addAttribute("ventas", ventaService.listarTodos());
        return "ventas/lista";
    }

    @GetMapping("/nuevo")
    @PreAuthorize("hasRole('ADMIN')")
    public String mostrarFormularioNuevo(Model model) {
        model.addAttribute("venta", new Venta());
        model.addAttribute("clientes", clienteService.listarTodos());
        model.addAttribute("usuarios", usuarioService.listarTodos());
        return "ventas/formulario";
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public String guardar(@RequestParam String clienteDpi, @RequestParam Long usuarioId,
                          @RequestParam BigDecimal total, @RequestParam int estado,
                          RedirectAttributes ra) {
        try {
            Cliente cliente = clienteService.buscarPorDPI(clienteDpi).orElseThrow();
            Usuario usuario = usuarioService.buscarPorId(usuarioId).orElseThrow();
            Venta venta = new Venta(System.currentTimeMillis(), total, estado, cliente, usuario);
            ventaService.guardar(venta);
            ra.addFlashAttribute("successMsg", "Venta registrada.");
        } catch (Exception e) {
            ra.addFlashAttribute("errorMsg", "Error: " + e.getMessage());
        }
        return "redirect:/ventas";
    }

    @GetMapping("/editar/{token}")
    @PreAuthorize("hasRole('ADMIN')")
    public String mostrarFormularioEditar(@PathVariable String token, Model model, RedirectAttributes ra) {
        Long id = RouteEncryptionUtil.decryptLong(token);
        return ventaService.buscarPorId(id).map(v -> {
            model.addAttribute("venta", v);
            model.addAttribute("clientes", clienteService.listarTodos());
            model.addAttribute("usuarios", usuarioService.listarTodos());
            model.addAttribute("idToken", token);
            return "ventas/formulario";
        }).orElseGet(() -> {
            ra.addFlashAttribute("errorMsg", "Venta no encontrada.");
            return "redirect:/ventas";
        });
    }

    @PostMapping("/actualizar/{token}")
    @PreAuthorize("hasRole('ADMIN')")
    public String actualizar(@PathVariable String token, @RequestParam String clienteDpi,
                             @RequestParam Long usuarioId, @RequestParam BigDecimal total,
                             @RequestParam int estado, @RequestParam(required = false) Long fechaVenta,
                             RedirectAttributes ra) {
        try {
            Long id = RouteEncryptionUtil.decryptLong(token);
            Cliente cliente = clienteService.buscarPorDPI(clienteDpi).orElseThrow();
            Usuario usuario = usuarioService.buscarPorId(usuarioId).orElseThrow();
            long fecha = (fechaVenta != null && fechaVenta > 0) ? fechaVenta : System.currentTimeMillis();
            Venta venta = new Venta(fecha, total, estado, cliente, usuario);
            ventaService.actualizar(id, venta);
            ra.addFlashAttribute("successMsg", "Venta actualizada.");
        } catch (Exception e) {
            ra.addFlashAttribute("errorMsg", "Error: " + e.getMessage());
        }
        return "redirect:/ventas";
    }

    @GetMapping("/eliminar/{token}")
    @PreAuthorize("hasRole('ADMIN')")
    public String eliminar(@PathVariable String token, RedirectAttributes ra) {
        try {
            Long id = RouteEncryptionUtil.decryptLong(token);
            ventaService.eliminar(id);
            ra.addFlashAttribute("successMsg", "Venta eliminada.");
        } catch (Exception e) {
            ra.addFlashAttribute("errorMsg", "Error: " + e.getMessage());
        }
        return "redirect:/ventas";
    }
}