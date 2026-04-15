package com.brayanbautista.kinalapp.repository;

import com.brayanbautista.kinalapp.entity.DetalleVenta;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface DetalleVentaRepository extends JpaRepository<DetalleVenta, Long> {
    List<DetalleVenta> findByVenta_CodigoVenta(Long codigoVenta);
}