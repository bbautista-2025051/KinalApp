package com.brayanbautista.kinalapp.service;

import com.brayanbautista.kinalapp.entity.Venta;
import java.util.List;
import java.util.Optional;

public interface IVentaService {
    List<Venta> listarTodos();
    Venta guardar(Venta venta);
    Venta actualizar(Long id, Venta venta);
    Optional<Venta> buscarPorId(Long id);
    void eliminar(Long id);
    boolean existePorId(Long id);
    List<Venta> obtenerPorEstado(int estado);
    List<Venta> obtenerPorRangoFechas(long inicio, long fin);  // cambio a long
}