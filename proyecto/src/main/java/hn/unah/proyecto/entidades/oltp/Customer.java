package hn.unah.proyecto.entidades.oltp;

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
@Table(name = "Customer")
public class Customer {
    @Id
    @Column(name = "customer_id")
    private Integer id;

    @JoinColumn(name = "store_id")
    @ManyToOne
    private Store tienda;

    @Column(name = "first_name")
    private String nombre;

    @Column(name = "last_name")
    private String apellido;

    @ManyToOne
    @JoinColumn(name = "address_id")
    private Address direccion;

}


// CUSTOMER_ID	NUMBER(38,0)
// STORE_ID	NUMBER(38,0)
// FIRST_NAME	VARCHAR2(45 BYTE)
// LAST_NAME	VARCHAR2(45 BYTE)
// EMAIL	VARCHAR2(50 BYTE)
// ADDRESS_ID	NUMBER(38,0)
// ACTIVE	CHAR(1 BYTE)
// CREATE_DATE	DATE
// LAST_UPDATE	DATE