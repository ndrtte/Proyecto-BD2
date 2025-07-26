package hn.unah.proyecto.entidades.olap;

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
@Table(name = "tbl_tienda")
public class DimTienda {
    
    @Id
    @Column(name = "id_tienda")
    private Integer idTienda;

    @Column(name = "nombre_tienda")
    private String nombreTienda;

    @ManyToOne
    @JoinColumn(name = "id_ciudad")
    private DimCiudad ciudad;
}
