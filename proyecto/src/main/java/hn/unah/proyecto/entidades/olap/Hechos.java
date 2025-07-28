package hn.unah.proyecto.entidades.olap;

import jakarta.annotation.Generated;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "tbl_hechos_renta")
public class Hechos {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_hechos")
    private Integer id_hechos;

    @ManyToOne
    @JoinColumn(name = "id_renta")
    private DimRenta renta;

    // @ManyToOne 
    // @JoinColumn(name = "id_cliente")
    // private dimCliente cliente;

    @ManyToOne 
    @JoinColumn(name = "id_empleado")
    private DimEmpleado empleado;

    @ManyToOne 
    @JoinColumn(name = "id_pelicula")
    private DimPelicula pelicula;

    @ManyToOne 
    @JoinColumn(name = "id_tienda")
    private DimTienda tienda;

    @ManyToOne 
    @JoinColumn(name = "id_fecha")
    private DimTiempo tiempo;

    // @ManyToOne 
    // @JoinColumn(name = "id_pago")
    // private dimPago pago;

    @Column(name = "ingresos")
    private Double montoPago;

    @Column(name = "cantidad")
    private Integer cantidad;

    @Column(name = "tiempo_renta")
    private Double tiempoRenta;

    @Column(name = "unidad_tiempo")
    private String unidadTiempo;
}