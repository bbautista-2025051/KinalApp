package com.brayanbautista.kinalapp.service;

import com.brayanbautista.kinalapp.entity.DetalleVenta;
import com.brayanbautista.kinalapp.entity.Producto;
import com.brayanbautista.kinalapp.repository.DetalleVentaRepository;
import com.brayanbautista.kinalapp.repository.ProductoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class DetalleVentaService implements IDetalleVentaService {

    private final DetalleVentaRepository detalleVentaRepository;
    private final ProductoRepository productoRepository; // Inyectado para validar stock

    public DetalleVentaService(DetalleVentaRepository detalleVentaRepository,
                               ProductoRepository productoRepository) {
        this.detalleVentaRepository = detalleVentaRepository;
        this.productoRepository = productoRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public List<DetalleVenta> listarTodos() {
        return detalleVentaRepository.findAll();
    }

    @Override
    public DetalleVenta guardar(DetalleVenta detalle) {
        validarDetalle(detalle);
        validarStock(detalle); // Verifica stock suficiente

        if (detalle.getSubtotal() == null) {
            detalle.setSubtotal(detalle.getPrecioUnitario().multiply(BigDecimal.valueOf(detalle.getCantidad())));
        }

        // Guardar detalle
        DetalleVenta saved = detalleVentaRepository.save(detalle);

        // Actualizar stock del producto
        actualizarStockProducto(detalle.getProducto(), -detalle.getCantidad());

        return saved;
    }

    @Override
    public DetalleVenta actualizar(Long id, DetalleVenta detalle) {
        if (!detalleVentaRepository.existsById(id)) {
            throw new RuntimeException("Detalle de venta no encontrado con ID: " + id);
        }

        // Obtener detalle original para ajustar stock
        DetalleVenta original = detalleVentaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("No se pudo cargar el detalle original"));

        detalle.setCodigoDetalleVenta(id);
        validarDetalle(detalle);
        validarStock(detalle); // Verifica stock suficiente (considerando el detalle actualizado)

        if (detalle.getSubtotal() == null) {
            detalle.setSubtotal(detalle.getPrecioUnitario().multiply(BigDecimal.valueOf(detalle.getCantidad())));
        }

        // Ajustar stock: revertir la cantidad original y luego aplicar la nueva
        actualizarStockProducto(original.getProducto(), +original.getCantidad());
        actualizarStockProducto(detalle.getProducto(), -detalle.getCantidad());

        return detalleVentaRepository.save(detalle);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<DetalleVenta> buscarPorId(Long id) {
        return detalleVentaRepository.findById(id);
    }

    @Override
    public void eliminar(Long id) {
        DetalleVenta detalle = detalleVentaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Detalle de venta no encontrado con ID: " + id));
        // Revertir stock
        actualizarStockProducto(detalle.getProducto(), +detalle.getCantidad());
        detalleVentaRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existePorId(Long id) {
        return detalleVentaRepository.existsById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<DetalleVenta> obtenerPorVenta(Long ventaId) {
        return detalleVentaRepository.findByVenta_CodigoVenta(ventaId);
    }

    private void validarDetalle(DetalleVenta detalle) {
        if (detalle.getCantidad() <= 0) {
            throw new IllegalArgumentException("La cantidad debe ser mayor que cero");
        }
        if (detalle.getPrecioUnitario() == null || detalle.getPrecioUnitario().compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("El precio unitario debe ser mayor que cero");
        }
        if (detalle.getProducto() == null) {
            throw new IllegalArgumentException("El producto es obligatorio");
        }
        if (detalle.getVenta() == null) {
            throw new IllegalArgumentException("La venta es obligatoria");
        }
    }

    private void validarStock(DetalleVenta detalle) {
        Producto producto = detalle.getProducto();
        // Si el producto ya tiene ID (persistido), cargamos la versión actualizada de la BD
        if (producto.getCodigoProducto() != null) {
            producto = productoRepository.findById(producto.getCodigoProducto())
                    .orElseThrow(() -> new IllegalArgumentException("Producto no encontrado"));
        }
        if (producto.getStock() < detalle.getCantidad()) {
            throw new IllegalArgumentException("Stock insuficiente. Disponible: " + producto.getStock());
        }
    }

    private void actualizarStockProducto(Producto producto, int cantidadAjuste) {
        // Asegurar que el producto está gestionado por el contexto de persistencia
        Producto managedProducto = productoRepository.findById(producto.getCodigoProducto())
                .orElseThrow(() -> new RuntimeException("Producto no encontrado"));
        int nuevoStock = managedProducto.getStock() + cantidadAjuste;
        if (nuevoStock < 0) {
            throw new RuntimeException("Stock negativo no permitido");
        }
        managedProducto.setStock(nuevoStock);
        productoRepository.save(managedProducto);
    }
}