package com.brayanbautista.kinalapp.repository;

import com.brayanbautista.kinalapp.entity.Venta;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface VentaRepository extends JpaRepository<Venta, Long> {
    List<Venta> findByEstado(int estado);
    List<Venta> findByFechaVentaBetween(long inicio, long fin);
}