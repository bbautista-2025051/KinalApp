package com.brayanbautista.kinalapp.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.util.List;
import com.brayanbautista.kinalapp.util.RouteEncryptionUtil;

@Entity
@Table(name = "productos")
public class Producto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "codigo_producto")
    private Long codigoProducto;

    @Column(nullable = false)
    private String nombreProducto;

    @Column(precision = 10, scale = 2, nullable = false)
    private BigDecimal precio;

    @Column(nullable = false)
    private int stock;

    @Column(nullable = false)
    private int estado;

    @OneToMany(mappedBy = "producto")
    private List<DetalleVenta> detallesVenta;

    public Producto() {}

    public Producto(String nombreProducto, BigDecimal precio, int stock, int estado) {
        this.nombreProducto = nombreProducto;
        this.precio = precio;
        this.stock = stock;
        this.estado = estado;
    }

    // Getters y setters
    public Long getCodigoProducto() { return codigoProducto; }
    public void setCodigoProducto(Long codigoProducto) { this.codigoProducto = codigoProducto; }
    public String getNombreProducto() { return nombreProducto; }
    public void setNombreProducto(String nombreProducto) { this.nombreProducto = nombreProducto; }
    public BigDecimal getPrecio() { return precio; }
    public void setPrecio(BigDecimal precio) { this.precio = precio; }
    public int getStock() { return stock; }
    public void setStock(int stock) { this.stock = stock; }
    public int getEstado() { return estado; }
    public void setEstado(int estado) { this.estado = estado; }
    public List<DetalleVenta> getDetallesVenta() { return detallesVenta; }
    public void setDetallesVenta(List<DetalleVenta> detallesVenta) { this.detallesVenta = detallesVenta; }

    public String getEncryptedId() {
        return RouteEncryptionUtil.encryptLong(this.codigoProducto);
    }
}