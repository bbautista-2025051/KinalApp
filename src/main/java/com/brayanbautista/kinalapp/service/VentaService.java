package com.brayanbautista.kinalapp.service;

import com.brayanbautista.kinalapp.entity.Venta;
import com.brayanbautista.kinalapp.repository.VentaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class VentaService implements IVentaService {

    private final VentaRepository ventaRepository;

    public VentaService(VentaRepository ventaRepository) {
        this.ventaRepository = ventaRepository;
    }

    @Override
    public Venta guardar(Venta venta) {
        validarVenta(venta);
        if (venta.getEstado() != 0 && venta.getEstado() != 1) {
            venta.setEstado(1);
        }
        if (venta.getFechaVenta() <= 0) {
            venta.setFechaVenta(System.currentTimeMillis());
        }
        return ventaRepository.save(venta);
    }

    @Override
    public Venta actualizar(Long id, Venta venta) {
        if (!ventaRepository.existsById(id)) {
            throw new RuntimeException("Venta no encontrada con ID: " + id);
        }
        venta.setCodigoVenta(id);
        validarVenta(venta);  // ← Se agregó la validación
        return ventaRepository.save(venta);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Venta> obtenerPorRangoFechas(long inicio, long fin) {
        if (inicio > fin) {
            throw new IllegalArgumentException("La fecha de inicio no puede ser mayor a la de fin");
        }
        return ventaRepository.findByFechaVentaBetween(inicio, fin);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Venta> listarTodos() {
        return ventaRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Venta> buscarPorId(Long id) {
        return ventaRepository.findById(id);
    }

    @Override
    public void eliminar(Long id) {
        ventaRepository.deleteById(id);
    }

    @Override
    public boolean existePorId(Long id) {
        return ventaRepository.existsById(id);
    }

    @Override
    public List<Venta> obtenerPorEstado(int estado) {
        return ventaRepository.findByEstado(estado);
    }

    private void validarVenta(Venta venta) {
        if (venta.getCliente() == null || venta.getUsuario() == null) {
            throw new IllegalArgumentException("Cliente y Usuario son obligatorios para la venta");
        }
    }
}