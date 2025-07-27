package hn.unah.proyecto.entidades.olap;

import java.util.Date;

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
@Table(name = "tbl_tiempo")
public class DimTiempo {
    @Id
    @Column(name = "id_fecha")
    private Integer idTiempo;

    @Column(name = "fecha")
    private Date fecha;
    
    @Column(name = "dia_semana")
    private String diaSemana;

    @Column(name = "mes")
    private Integer mes;

    @Column(name = "anio")
    private Integer anio;

    @Column(name = "trimestre")
    private Integer trimestre;
}