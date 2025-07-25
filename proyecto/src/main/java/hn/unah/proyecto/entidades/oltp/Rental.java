package hn.unah.proyecto.entidades.oltp;

import java.util.Date;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
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
@Table(name = "Rental")
public class Rental {

    @Id
    @Column(name = "rental_id")
    private Integer id;

    @Column(name = "rental_date")
    private Date fechaRenta;

    @ManyToOne
    @JoinColumn(name = "inventory_id")
    private Inventory inventario;

    @ManyToOne
    @JoinColumn(name = "customer_id")
    private Customer cliente;

    @Column(name = "return_date")
    private Date fechaDevolucion;

    @ManyToOne
    @JoinColumn(name = "staff_id")
    private Staff empleado;

    @Column(name = "last_update")
    private Date ultimaActualizacion;
}
