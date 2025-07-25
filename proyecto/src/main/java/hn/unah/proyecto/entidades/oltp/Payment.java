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
@Table(name = "Payment")
public class Payment {
    @Id
    @Column(name = "payment_id")
    private Integer id;

    @JoinColumn(name = "customer_id")
    @ManyToOne
    private Customer cliente;

    @JoinColumn(name = "staff_id")
    @ManyToOne
    private Staff empleado;

    @JoinColumn(name = "rental_id")
    @ManyToOne
    private Rental renta;

    @Column(name = "amount")
    private Double monto;

    @Column(name = "payment_date")
    private Date fechaPago;

    @Column(name = "last_update")
    private Date ultimaActualizacion;
}