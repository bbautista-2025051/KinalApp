package com.brayanbautista.kinalapp.controller;

import com.brayanbautista.kinalapp.entity.Venta;
import com.brayanbautista.kinalapp.service.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Controller
public class ViewController {

    private final IClientesService clienteService;
    private final IProductoService productoService;
    private final IVentaService ventaService;
    private final IUsuarioService usuarioService;
    private final IDetalleVentaService detalleVentaService;

    public ViewController(IClientesService clienteService,
                          IProductoService productoService,
                          IVentaService ventaService,
                          IUsuarioService usuarioService,
                          IDetalleVentaService detalleVentaService) {
        this.clienteService = clienteService;
        this.productoService = productoService;
        this.ventaService = ventaService;
        this.usuarioService = usuarioService;
        this.detalleVentaService = detalleVentaService;
    }

    @GetMapping("/")
    public String home(Model model) {
        int clientesCount = clienteService.listarTodos().size();
        int productosCount = productoService.listarTodos().size();
        int ventasCount = ventaService.listarTodos().size();
        int usuariosCount = usuarioService.listarTodos().size();
        int detallesCount = detalleVentaService.listarTodos().size();

        int clientesActivos = clienteService.obtenerPorEstado(1).size();
        int productosActivos = productoService.obtenerPorEstado(1).size();
        int usuariosActivos = usuarioService.obtenerPorEstado(1).size();

        List<Venta> ventas = ventaService.listarTodos();
        List<Venta> ventasRecientes = ventas.stream()
                .sorted(Comparator.comparing(Venta::getFechaVenta).reversed())
                .limit(5)
                .collect(Collectors.toList());

        model.addAttribute("clientesCount", clientesCount);
        model.addAttribute("clientesActivos", clientesActivos);
        model.addAttribute("productosCount", productosCount);
        model.addAttribute("productosActivos", productosActivos);
        model.addAttribute("ventasCount", ventasCount);
        model.addAttribute("usuariosCount", usuariosCount);
        model.addAttribute("usuariosActivos", usuariosActivos);
        model.addAttribute("detallesCount", detallesCount);
        model.addAttribute("ventasRecientes", ventasRecientes);

        return "index";
    }
}