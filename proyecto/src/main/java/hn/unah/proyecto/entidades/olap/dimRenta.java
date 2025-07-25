package hn.unah.proyecto.entidades.olap;

import java.sql.Date;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "tbl_renta")
public class dimRenta {
    
    @Id
    @Column(name = "id_renta")
    private Integer idRenta;

    @Column(name = "fecha_renta")
    private Date fechaRenta;

    @Column(name = "fecha_devolucion")
    private Date fecha_devolucion;
}
