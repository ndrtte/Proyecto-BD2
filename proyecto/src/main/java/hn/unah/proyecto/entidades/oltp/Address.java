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
@Table(name = "Address")
public class Address {
    
    @Id
    @Column(name = "address_id")
    private Integer id;

    @Column(name = "address")
    private String direccion;

    // @Column(name = "address2")
    // private String direccion2;

    @Column(name = "district")
    private String distrito;

    @JoinColumn(name = "city_id", referencedColumnName = "city_id")
    @ManyToOne
    private City ciudad;

    // @Column(name = "postal_code")
    // private String codigoPostal;

    @Column(name = "phone")
    private String telefono;


    @Column(name = "last_update")
    private Date ultimaActualizacion;
}
