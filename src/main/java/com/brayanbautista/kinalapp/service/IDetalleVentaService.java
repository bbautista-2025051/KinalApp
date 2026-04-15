package com.brayanbautista.kinalapp.service;

import com.brayanbautista.kinalapp.entity.DetalleVenta;
import java.util.List;
import java.util.Optional;

public interface IDetalleVentaService {
    List<DetalleVenta> listarTodos();
    DetalleVenta guardar(DetalleVenta detalle);
    DetalleVenta actualizar(Long id, DetalleVenta detalle);
    Optional<DetalleVenta> buscarPorId(Long id);
    void eliminar(Long id);
    boolean existePorId(Long id);
    List<DetalleVenta> obtenerPorVenta(Long ventaId);
}