package hn.unah.proyecto.dto;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TiempoDTO {
    
    private Integer idTiempo;
    private Date fecha;
    private String diaSemana;
    private Integer mes;
    private Integer anio;
    private Integer trimestre;

}

