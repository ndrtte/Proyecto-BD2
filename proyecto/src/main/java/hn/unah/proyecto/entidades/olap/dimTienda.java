package hn.unah.proyecto.entidades.olap;

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
@Table(name = "tbl_tienda")
public class dimTienda {
    
    @Id
    @Column(name = "id_tienda")
    private Integer idTienda;

    @Column(name = "nombre_tienda")
    private String nombreTienda;

}
